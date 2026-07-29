package com.example.customer_app;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Customer {

    @Id
    private Long id;

    private String accountType;


    // Default constructor
    public Customer() {
    }


    // Parameterized constructor
    public Customer(Long id, String accountType) {
        this.id = id;
        this.accountType = accountType;
    }


    // Getter
    public Long getId() {
        return id;
    }


    // Setter
    public void setId(Long id) {
        this.id = id;
    }


    // Getter
    public String getAccountType() {
        return accountType;
    }


    // Setter
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }


    @Override
    public String toString() {

        return "Customer{" +
                "id=" + id +
                ", accountType='" +
                accountType + '\'' +
                '}';
    }
}