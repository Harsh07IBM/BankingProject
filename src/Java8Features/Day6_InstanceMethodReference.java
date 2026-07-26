package org.example;

import java.util.function.Supplier;

public class Day6_InstanceMethodReference {

    public static void main(String[] args) {

        String message = "HELLO JAVA";

        Supplier<String> result =
                message::toLowerCase;

        System.out.println(
                result.get()
        );
    }
}
