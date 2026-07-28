package com.ibm.welcome;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WelcomeNoteAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(WelcomeNoteAppApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(WelcomeMessageService service) {
        return args -> System.out.println(service.getWelcomeMessage());
    }
}
