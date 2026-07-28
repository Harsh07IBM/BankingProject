import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.orderservice.service.OrderService;
import com.example.orderservice.service.OrderServiceImpl;
import com.example.orderservice.service.PaymentService;

public class OrderServiceImplTest {

    private OrderService orderService;
    private PaymentService paymentService;

    @BeforeEach
    public void setUp() {
        paymentService = mock(PaymentService.class);
        orderService = new OrderServiceImpl(paymentService);
    }

    @Test
    public void testProcessOrderWithCreditCardPayment() {
        double amount = 100.0;
        orderService.processOrder(amount);

        verify(paymentService, times(1)).processPayment(amount);
    }

    @Test
    public void testProcessOrderWithDebitCardPayment() {
        double amount = 200.0;
        orderService.processOrder(amount);

        verify(paymentService, times(1)).processPayment(amount);
    }
}