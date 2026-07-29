package com.example.orderservice;

import com.example.orderservice.service.CreditCardPayment;
import com.example.orderservice.service.DebitCardPayment;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.service.OrderServiceImpl;
import com.example.orderservice.service.PaymentService;

public class Application {
    public static void main(String[] args) {
        PaymentService creditCardPayment = new CreditCardPayment();
        PaymentService debitCardPayment = new DebitCardPayment();

        OrderService orderServiceWithCreditCard = new OrderServiceImpl(creditCardPayment);
        OrderService orderServiceWithDebitCard = new OrderServiceImpl(debitCardPayment);

        double amount = 100.0;
        orderServiceWithCreditCard.processOrder(amount);
        orderServiceWithDebitCard.processOrder(amount);
    }
}