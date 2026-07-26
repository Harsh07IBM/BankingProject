package org.example;

@FunctionalInterface
interface MessagePrinter {
    void display();
}

public class Day6_ZeroParameterLambda {

    public static void main(String[] args) {

        MessagePrinter printer =
                () -> System.out.println(
                        "This is a zero parameter lambda"
                );

        printer.display();
    }
}
