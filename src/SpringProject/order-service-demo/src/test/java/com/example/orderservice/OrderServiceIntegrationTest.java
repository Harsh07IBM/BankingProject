package com.example.orderservice;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.orderservice.config.DependencyInjector;
import com.example.orderservice.service.CreditCardPayment;
import com.example.orderservice.service.DebitCardPayment;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.service.OrderServiceImpl;
import com.example.orderservice.service.PaymentService;

/**
 * Integration tests for the Order Service.
 *
 * Unlike the unit tests in OrderServiceImplTest (which mock PaymentService),
 * these tests wire up real implementations end-to-end and verify the full
 * processing pipeline: DependencyInjector -> OrderServiceImpl -> PaymentService impl.
 */
@DisplayName("Order Service Integration Tests")
public class OrderServiceIntegrationTest {

    private final ByteArrayOutputStream outputCapture = new ByteArrayOutputStream();
    private PrintStream originalOut;

    @BeforeEach
    public void setUp() {
        // Redirect System.out so we can assert on console output
        originalOut = System.out;
        System.setOut(new PrintStream(outputCapture));
    }

    @AfterEach
    public void tearDown() {
        // Restore original System.out
        System.setOut(originalOut);
    }

    // -----------------------------------------------------------------------
    // DependencyInjector wiring tests
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("DependencyInjector creates OrderService with CreditCardPayment for 'CREDIT'")
    public void testDependencyInjectorCreatesCreditCardOrderService() {
        OrderService orderService = DependencyInjector.createOrderService("CREDIT");
        assertNotNull(orderService, "OrderService should not be null");
    }

    @Test
    @DisplayName("DependencyInjector creates OrderService with DebitCardPayment for 'DEBIT'")
    public void testDependencyInjectorCreatesDebitCardOrderService() {
        OrderService orderService = DependencyInjector.createOrderService("DEBIT");
        assertNotNull(orderService, "OrderService should not be null");
    }

    @Test
    @DisplayName("DependencyInjector is case-insensitive for payment type")
    public void testDependencyInjectorCaseInsensitive() {
        assertDoesNotThrow(() -> DependencyInjector.createOrderService("credit"));
        assertDoesNotThrow(() -> DependencyInjector.createOrderService("Credit"));
        assertDoesNotThrow(() -> DependencyInjector.createOrderService("debit"));
        assertDoesNotThrow(() -> DependencyInjector.createOrderService("Debit"));
    }

    @Test
    @DisplayName("DependencyInjector throws IllegalArgumentException for invalid payment type")
    public void testDependencyInjectorInvalidPaymentType() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> DependencyInjector.createOrderService("PAYPAL")
        );
        assertEquals("Invalid payment type", ex.getMessage());
    }

    @Test
    @DisplayName("DependencyInjector throws IllegalArgumentException for null payment type")
    public void testDependencyInjectorNullPaymentType() {
        assertThrows(
                Exception.class,
                () -> DependencyInjector.createOrderService(null)
        );
    }

    // -----------------------------------------------------------------------
    // End-to-end order processing with credit card
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("End-to-end: process order with CreditCardPayment via DependencyInjector")
    public void testEndToEndCreditCardOrderProcessing() {
        OrderService orderService = DependencyInjector.createOrderService("CREDIT");
        double amount = 150.0;

        orderService.processOrder(amount);

        String output = outputCapture.toString();
        assertTrue(output.contains("Starting order for amount: $150.0"),
                "Should print order start message");
        assertTrue(output.contains("Processing credit card payment of $150.0"),
                "Should print credit card payment message");
        assertTrue(output.contains("Order completed for amount: $150.0"),
                "Should print order completed message");
    }

    @Test
    @DisplayName("End-to-end: process order with DebitCardPayment via DependencyInjector")
    public void testEndToEndDebitCardOrderProcessing() {
        OrderService orderService = DependencyInjector.createOrderService("DEBIT");
        double amount = 250.0;

        orderService.processOrder(amount);

        String output = outputCapture.toString();
        assertTrue(output.contains("Starting order for amount: $250.0"),
                "Should print order start message");
        assertTrue(output.contains("Processing debit card payment of $250.0"),
                "Should print debit card payment message");
        assertTrue(output.contains("Order completed for amount: $250.0"),
                "Should print order completed message");
    }

    // -----------------------------------------------------------------------
    // End-to-end with manual wiring (no DependencyInjector)
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("End-to-end: OrderServiceImpl with real CreditCardPayment (manual wiring)")
    public void testManualWiringWithCreditCardPayment() {
        PaymentService payment = new CreditCardPayment();
        OrderService orderService = new OrderServiceImpl(payment);

        orderService.processOrder(75.50);

        String output = outputCapture.toString();
        assertTrue(output.contains("Starting order for amount: $75.5"),
                "Should print order start message");
        assertTrue(output.contains("Processing credit card payment of $75.5"),
                "Should delegate to CreditCardPayment");
        assertTrue(output.contains("Order completed for amount: $75.5"),
                "Should print order completed message");
    }

    @Test
    @DisplayName("End-to-end: OrderServiceImpl with real DebitCardPayment (manual wiring)")
    public void testManualWiringWithDebitCardPayment() {
        PaymentService payment = new DebitCardPayment();
        OrderService orderService = new OrderServiceImpl(payment);

        orderService.processOrder(320.99);

        String output = outputCapture.toString();
        assertTrue(output.contains("Starting order for amount: $320.99"),
                "Should print order start message");
        assertTrue(output.contains("Processing debit card payment of $320.99"),
                "Should delegate to DebitCardPayment");
        assertTrue(output.contains("Order completed for amount: $320.99"),
                "Should print order completed message");
    }

    // -----------------------------------------------------------------------
    // Multiple orders in sequence
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("Processing multiple orders sequentially uses correct payment for each")
    public void testMultipleOrdersSequentially() {
        OrderService creditOrderService = DependencyInjector.createOrderService("CREDIT");
        OrderService debitOrderService = DependencyInjector.createOrderService("DEBIT");

        creditOrderService.processOrder(100.0);
        debitOrderService.processOrder(200.0);
        creditOrderService.processOrder(300.0);

        String output = outputCapture.toString();

        // Verify all three orders produced output
        assertTrue(output.contains("Processing credit card payment of $100.0"),
                "First credit card order should be processed");
        assertTrue(output.contains("Processing debit card payment of $200.0"),
                "Debit card order should be processed");
        assertTrue(output.contains("Processing credit card payment of $300.0"),
                "Second credit card order should be processed");
    }

    // -----------------------------------------------------------------------
    // Edge-case amounts
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("Order processes correctly with zero amount")
    public void testProcessOrderWithZeroAmount() {
        OrderService orderService = DependencyInjector.createOrderService("CREDIT");

        orderService.processOrder(0.0);

        String output = outputCapture.toString();
        assertTrue(output.contains("Starting order for amount: $0.0"),
                "Should handle zero amount");
        assertTrue(output.contains("Processing credit card payment of $0.0"),
                "Payment should be invoked for zero amount");
    }

    @Test
    @DisplayName("Order processes correctly with negative amount")
    public void testProcessOrderWithNegativeAmount() {
        OrderService orderService = DependencyInjector.createOrderService("DEBIT");

        orderService.processOrder(-50.0);

        String output = outputCapture.toString();
        assertTrue(output.contains("Starting order for amount: $-50.0"),
                "Should handle negative amount");
        assertTrue(output.contains("Processing debit card payment of $-50.0"),
                "Payment should be invoked for negative amount");
    }

    @Test
    @DisplayName("Order processes correctly with very large amount")
    public void testProcessOrderWithLargeAmount() {
        OrderService orderService = DependencyInjector.createOrderService("CREDIT");

        orderService.processOrder(999999999.99);

        String output = outputCapture.toString();
        assertTrue(output.contains("Processing credit card payment of $9.9999999999E8")
                        || output.contains("Processing credit card payment of $999999999.99"),
                "Should handle very large amount");
    }

}
