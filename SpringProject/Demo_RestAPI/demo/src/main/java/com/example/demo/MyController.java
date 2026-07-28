package com.example.myapp;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usersApi")
public class MyController {

    private List<String> users =
            List.of("Shiraz", "Sujal", "Vaibhav", "Harsh", "Rohit", "Sahil", "Rohit", "Sahil", "Rohit", "Sahil", "Rohit", "Sahil");

    @GetMapping
    public List<String> getUsers() {
        return users;
    }
}