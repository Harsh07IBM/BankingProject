package com.example.banking.entity;

import jakarta.persistence.*;
import java.io.Serializable;

// Serializable is needed because Redis stores objects by serializing them
@Entity
public class Account implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountHolderName;

    private double balance;

    public Account() {}

    public Account(String accountHolderName, double balance) {
        this.accountHolderName = accountHolderName;
        this.balance = balance;
    }

    // Getters and Setters
    public Long getId()                     { return id; }
    public void setId(Long id)              { this.id = id; }

    public String getAccountHolderName()    { return accountHolderName; }
    public void setAccountHolderName(String name) { this.accountHolderName = name; }

    public double getBalance()              { return balance; }
    public void setBalance(double balance)  { this.balance = balance; }
}
