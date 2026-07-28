package com.example.orderservice.service;

public class PaymentServiceImpl implements PaymentService {
    @Override
    public void processPayment(double amount) {
        // Logic for processing payment
        System.out.println("Processing payment of amount: " + amount);
    }
}