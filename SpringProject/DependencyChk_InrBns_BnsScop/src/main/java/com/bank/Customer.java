package com.bank;

public class Customer {
    private String name;
    private Address address;

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void display() {
        System.out.println("Customer: " + name + ", Address: " + address.getCity());
    }
}
