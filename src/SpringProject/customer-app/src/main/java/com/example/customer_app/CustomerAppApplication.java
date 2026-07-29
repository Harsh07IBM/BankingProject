package com.example.customer_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CustomerAppApplication {

    public static void main(String[] args) {

        SpringApplication.run(
                CustomerAppApplication.class,
                args
        );

        Customer customer =
        new Customer(
                101L,
                "Savings"
        	);

        System.out.println(customer);
    }
}