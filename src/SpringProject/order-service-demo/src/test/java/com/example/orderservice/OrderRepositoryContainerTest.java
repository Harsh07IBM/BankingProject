package com.example.orderservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.example.orderservice.model.Order;
import com.example.orderservice.repository.OrderRepository;

/**
 * Testcontainers Integration Test
 * ================================
 * This test demonstrates how Testcontainers works:
 *
 * 1. @Testcontainers  - Tells JUnit to manage container lifecycle automatically
 * 2. @Container       - Marks a container field to be started/stopped by the extension
 * 3. PostgreSQLContainer - Spins up a REAL PostgreSQL database inside Docker
 *
 * HOW IT WORKS:
 * - Before tests run, Testcontainers pulls the postgres:15 Docker image
 * - It starts a real PostgreSQL container on a random available port
 * - Our OrderRepository connects to this real database via JDBC
 * - After all tests finish, the container is automatically stopped and removed
 *
 * PREREQUISITE: Docker Desktop must be running on your machine!
 */
@Testcontainers
@DisplayName("Testcontainers Demo - OrderRepository with Real PostgreSQL")
public class OrderRepositoryContainerTest {

    // -----------------------------------------------------------------------
    // Testcontainers will automatically start this PostgreSQL container
    // before tests and stop it after tests.
    // -----------------------------------------------------------------------
    @Container
    private static final PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>("postgres:15-alpine")
                    .withDatabaseName("testdb")
                    .withUsername("testuser")
                    .withPassword("testpass");

    private OrderRepository orderRepository;

    @BeforeEach
    public void setUp() throws SQLException {
        // Connect to the real PostgreSQL container
        // Testcontainers provides the dynamic JDBC URL, username, and password
        orderRepository = new OrderRepository(
                postgresContainer.getJdbcUrl(),
                postgresContainer.getUsername(),
                postgresContainer.getPassword()
        );

        // Create the orders table (idempotent)
        orderRepository.createTable();

        // Clean slate for each test - delete all existing orders
        for (Order order : orderRepository.findAll()) {
            orderRepository.deleteById(order.getOrderId());
        }
    }

    // -----------------------------------------------------------------------
    // Basic CRUD operations against a REAL database
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("Container is running and accessible")
    public void testContainerIsRunning() {
        assertTrue(postgresContainer.isRunning(),
                "PostgreSQL container should be running");
        System.out.println("PostgreSQL container is running at: "
                + postgresContainer.getJdbcUrl());
        System.out.println("Mapped port: " + postgresContainer.getMappedPort(5432));
    }

    @Test
    @DisplayName("Save and retrieve an order from PostgreSQL")
    public void testSaveAndFindOrder() throws SQLException {
        Order order = new Order("ORD-001", 99.99);

        orderRepository.save(order);

        Order retrieved = orderRepository.findById("ORD-001");
        assertNotNull(retrieved, "Order should be found in the database");
        assertEquals("ORD-001", retrieved.getOrderId());
        assertEquals(99.99, retrieved.getAmount(), 0.001);
    }

    @Test
    @DisplayName("Find returns null for non-existent order")
    public void testFindNonExistentOrder() throws SQLException {
        Order result = orderRepository.findById("DOES-NOT-EXIST");
        assertNull(result, "Should return null for non-existent order");
    }

    @Test
    @DisplayName("Save multiple orders and retrieve all")
    public void testSaveMultipleAndFindAll() throws SQLException {
        orderRepository.save(new Order("ORD-001", 50.00));
        orderRepository.save(new Order("ORD-002", 75.50));
        orderRepository.save(new Order("ORD-003", 120.00));

        List<Order> allOrders = orderRepository.findAll();
        assertEquals(3, allOrders.size(), "Should have 3 orders in the database");
    }

    @Test
    @DisplayName("Update an existing order (upsert)")
    public void testUpdateOrder() throws SQLException {
        orderRepository.save(new Order("ORD-001", 50.00));

        // Save again with updated amount — upsert should update
        orderRepository.save(new Order("ORD-001", 150.00));

        Order updated = orderRepository.findById("ORD-001");
        assertNotNull(updated);
        assertEquals(150.00, updated.getAmount(), 0.001,
                "Amount should be updated to 150.00");

        // Should still be just 1 order, not 2
        assertEquals(1, orderRepository.count(),
                "Upsert should not create a duplicate");
    }

    @Test
    @DisplayName("Delete an order from the database")
    public void testDeleteOrder() throws SQLException {
        orderRepository.save(new Order("ORD-001", 99.99));

        boolean deleted = orderRepository.deleteById("ORD-001");
        assertTrue(deleted, "Delete should return true for existing order");

        Order result = orderRepository.findById("ORD-001");
        assertNull(result, "Order should be gone after deletion");
    }

    @Test
    @DisplayName("Count returns correct number of orders")
    public void testCountOrders() throws SQLException {
        assertEquals(0, orderRepository.count(), "Initially should have 0 orders");

        orderRepository.save(new Order("ORD-001", 10.00));
        orderRepository.save(new Order("ORD-002", 20.00));

        assertEquals(2, orderRepository.count(), "Should have 2 orders after inserts");
    }

    // -----------------------------------------------------------------------
    // End-to-end: OrderService + PaymentService + Database persistence
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("End-to-end: process order, persist, and verify in database")
    public void testEndToEndOrderProcessingWithDatabase() throws SQLException {
        // Simulate the full flow:
        // 1. Create an order
        Order order = new Order("ORD-E2E-001", 250.00);

        // 2. Persist it to the real PostgreSQL database
        orderRepository.save(order);

        // 3. Retrieve and verify
        Order fromDb = orderRepository.findById("ORD-E2E-001");
        assertNotNull(fromDb);
        assertEquals("ORD-E2E-001", fromDb.getOrderId());
        assertEquals(250.00, fromDb.getAmount(), 0.001);

        // 4. Update the order amount (e.g., after applying a discount)
        fromDb.setAmount(225.00);
        orderRepository.save(fromDb);

        // 5. Verify the update persisted
        Order updatedFromDb = orderRepository.findById("ORD-E2E-001");
        assertEquals(225.00, updatedFromDb.getAmount(), 0.001,
                "Updated amount should be persisted");
    }
}
