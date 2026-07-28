package com.ibm.welcome;

import org.springframework.stereotype.Service;

@Service
public class WelcomeMessageService {

    public String getWelcomeMessage() {
        return "Welcome to Spring Boot!";
    }
}
