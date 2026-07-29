package com.ibm.welcome;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WelcomeMessageServiceTest {

    @Test
    void shouldReturnWelcomeMessage() {
        WelcomeMessageService service = new WelcomeMessageService();

        assertEquals("Welcome to Spring Boot!", service.getWelcomeMessage());
    }
}
