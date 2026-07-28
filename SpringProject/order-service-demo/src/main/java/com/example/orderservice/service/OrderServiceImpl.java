package com.example.orderservice.service;

public class OrderServiceImpl implements OrderService {
    private final PaymentService paymentService;

    public OrderServiceImpl(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    public void processOrder(double amount) {
        System.out.println("Starting order for amount: $" + amount);
        paymentService.processPayment(amount);
        System.out.println("Order completed for amount: $" + amount);
    }
}