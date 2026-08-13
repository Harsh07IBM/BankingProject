package com.example.orderservice.repository;

import com.example.orderservice.model.Order;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple JDBC-based repository for persisting Order objects.
 * Demonstrates how a real database layer works with Testcontainers.
 */
public class OrderRepository {

    private final String jdbcUrl;
    private final String username;
    private final String password;

    public OrderRepository(String jdbcUrl, String username, String password) {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
    }

    /**
     * Creates the orders table if it does not already exist.
     */
    public void createTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS orders ("
                + "order_id VARCHAR(50) PRIMARY KEY, "
                + "amount DOUBLE PRECISION NOT NULL"
                + ")";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

    /**
     * Saves an order to the database. Uses UPSERT to handle duplicates.
     */
    public void save(Order order) throws SQLException {
        String sql = "INSERT INTO orders (order_id, amount) VALUES (?, ?) "
                + "ON CONFLICT (order_id) DO UPDATE SET amount = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, order.getOrderId());
            pstmt.setDouble(2, order.getAmount());
            pstmt.setDouble(3, order.getAmount());
            pstmt.executeUpdate();
        }
    }

    /**
     * Finds an order by its ID. Returns null if not found.
     */
    public Order findById(String orderId) throws SQLException {
        String sql = "SELECT order_id, amount FROM orders WHERE order_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, orderId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Order(rs.getString("order_id"), rs.getDouble("amount"));
                }
            }
        }
        return null;
    }

    /**
     * Returns all orders from the database.
     */
    public List<Order> findAll() throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT order_id, amount FROM orders ORDER BY order_id";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                orders.add(new Order(rs.getString("order_id"), rs.getDouble("amount")));
            }
        }
        return orders;
    }

    /**
     * Deletes an order by its ID. Returns true if a row was deleted.
     */
    public boolean deleteById(String orderId) throws SQLException {
        String sql = "DELETE FROM orders WHERE order_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, orderId);
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Returns the total number of orders.
     */
    public long count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM orders";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            rs.next();
            return rs.getLong(1);
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(jdbcUrl, username, password);
    }
}
