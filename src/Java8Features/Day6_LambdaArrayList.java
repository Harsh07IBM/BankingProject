package org.example;

import java.util.ArrayList;

public class Day6_LambdaArrayList {

    public static void main(String[] args) {

        ArrayList<Integer> numbers =
                new ArrayList<>();

        numbers.add(11);
        numbers.add(24);
        numbers.add(37);
        numbers.add(48);
        numbers.add(55);

        numbers.forEach(number -> {

            if (number % 2 == 0) {
                System.out.println(
                        number + " is Even"
                );
            } else {
                System.out.println(
                        number + " is Odd"
                );
            }
        });
    }
}
