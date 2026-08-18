package com.example.banking.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long accountId;     // which account this transaction belongs to

    private String type;        // "CREDIT" or "DEBIT"

    private double amount;

    private double balanceAfterTransaction;

    private LocalDateTime timestamp;

    public Transaction() {}

    public Transaction(Long accountId, String type, double amount, double balanceAfter) {
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.balanceAfterTransaction = balanceAfter;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId()                          { return id; }
    public void setId(Long id)                   { this.id = id; }

    public Long getAccountId()                   { return accountId; }
    public void setAccountId(Long accountId)     { this.accountId = accountId; }

    public String getType()                      { return type; }
    public void setType(String type)             { this.type = type; }

    public double getAmount()                    { return amount; }
    public void setAmount(double amount)         { this.amount = amount; }

    public double getBalanceAfterTransaction()   { return balanceAfterTransaction; }
    public void setBalanceAfterTransaction(double b) { this.balanceAfterTransaction = b; }

    public LocalDateTime getTimestamp()           { return timestamp; }
    public void setTimestamp(LocalDateTime t)     { this.timestamp = t; }
}
