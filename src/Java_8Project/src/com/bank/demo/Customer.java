package com.bank.demo;

import java.util.Optional;

public class Customer {
    private String name;
    private Optional<String> panCard; // may be missing

    public Customer(String name, String panCard) {
        this.name = name;
        this.panCard = Optional.ofNullable(panCard);
    }

    public String getName() {
        return name;
    }

    public Optional<String> getPanCard() {
        return panCard;
    }
}
