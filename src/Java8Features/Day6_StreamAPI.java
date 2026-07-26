package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day6_StreamAPI {

    public static void main(String[] args) {

        List<String> names = new ArrayList<>();

        names.add("Harsh");
        names.add("Ravi");
        names.add("Sam");
        names.add("Aman");

        List<String> result = names.stream()
                .filter(name -> name.startsWith("S"))
                .collect(Collectors.toList());

        System.out.println(result);
    }
}
