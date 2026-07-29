package com.example.orderservice.service;

public class DebitCardPayment implements PaymentService {
    @Override
    public void processPayment(double amount) {
        System.out.println("Processing debit card payment of $" + amount);
    }
}
