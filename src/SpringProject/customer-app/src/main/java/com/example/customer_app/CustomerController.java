package com.example.customer_app;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerRepository customerRepository;


    // Constructor Injection
    public CustomerController(
            CustomerRepository customerRepository) {

        this.customerRepository =
                customerRepository;
    }


    // POST /customers
    @PostMapping
    public Customer createCustomer(
            @RequestBody Customer customer) {

        return customerRepository.save(customer);
    }
}