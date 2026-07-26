package org.example;

interface WelcomeMessage {
    void show();
}

public class Day6_SimpleLambda {

    public static void main(String[] args) {

        WelcomeMessage message =
                () -> System.out.println("Welcome");

        message.show();
    }
}
