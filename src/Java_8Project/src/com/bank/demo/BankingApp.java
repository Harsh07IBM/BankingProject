package com.bank.demo;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class BankingApp {
    public static void main(String[] args) {
        // 1. Transactions with Streams + Lambdas
        List<Transaction> transactions = Arrays.asList(
                new Transaction(500, LocalDate.now()),
                new Transaction(2500, LocalDate.now()),
                new Transaction(1200, LocalDate.now())
        );

        List<Transaction> highValue = transactions.stream()
                .filter(t -> t.getAmount() > 1000)
                .collect(Collectors.toList());

        highValue.forEach(t -> System.out.println("High Value Txn: " + t.getAmount()));

        // 2. Optional for Customer PAN
        Customer c1 = new Customer("Ravi", null);
        c1.getPanCard().ifPresentOrElse(
                val -> System.out.println("PAN: " + val),
                () -> System.out.println("PAN not available")
        );

        // 3. Date-Time API for EMI scheduling
        LocalDate today = LocalDate.now();
        LocalDate nextEmi = today.plusMonths(1);
        System.out.println("Next EMI due on: " + nextEmi);

        // 4. Default Methods in Interfaces
        Payment p = new CreditCardPayment();
        p.validate();
        Payment.log("Payment processed successfully");

        // 5. Method References for Logging
        List<String> logs = Arrays.asList("Debit", "Credit", "Transfer");
        logs.forEach(System.out::println);
    }
}
