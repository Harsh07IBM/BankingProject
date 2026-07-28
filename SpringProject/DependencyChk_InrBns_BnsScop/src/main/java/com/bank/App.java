package com.bank;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        // Dependency Check demo
        BankAccount account = (BankAccount) context.getBean("bankAccount");
        account.display();

        // Inner Bean demo
        Customer customer = (Customer) context.getBean("customer");
        customer.display();

        // Scope demo
        BankAccount acc1 = (BankAccount) context.getBean("singletonAccount");
        BankAccount acc2 = (BankAccount) context.getBean("singletonAccount");
        System.out.println("Singleton same object? " + (acc1 == acc2));

        BankAccount acc3 = (BankAccount) context.getBean("prototypeAccount");
        BankAccount acc4 = (BankAccount) context.getBean("prototypeAccount");
        System.out.println("Prototype same object? " + (acc3 == acc4));
    }
}
