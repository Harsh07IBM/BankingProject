package org.example;

import java.util.Optional;

public class Day6_Optional {

    public static void main(String[] args) {

        String user = "Harsh";

        String name = Optional.ofNullable(user)
                .orElse("Guest");

        System.out.println(name);
    }
}
