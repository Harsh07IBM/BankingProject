package org.example;

import java.util.function.Function;

public class Day6_StringMethodReference {

    public static void main(String[] args) {

        Function<String, String> converter =
                String::toLowerCase;

        System.out.println(
                converter.apply("HELLO")
        );
    }
}
