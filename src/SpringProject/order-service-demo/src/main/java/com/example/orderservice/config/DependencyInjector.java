package com.example.orderservice.config;

import com.example.orderservice.service.OrderService;
import com.example.orderservice.service.OrderServiceImpl;
import com.example.orderservice.service.PaymentService;
import com.example.orderservice.service.CreditCardPayment;
import com.example.orderservice.service.DebitCardPayment;

public class DependencyInjector {

    public static OrderService createOrderService(String paymentType) {
        PaymentService paymentService;

        if ("CREDIT".equalsIgnoreCase(paymentType)) {
            paymentService = new CreditCardPayment();
        } else if ("DEBIT".equalsIgnoreCase(paymentType)) {
            paymentService = new DebitCardPayment();
        } else {
            throw new IllegalArgumentException("Invalid payment type");
        }

        return new OrderServiceImpl(paymentService);
    }
}