package org.example;

import java.util.function.BiFunction;

public class Day6_StaticMethodReference {

    public static void main(String[] args) {

        BiFunction<Integer, Integer, Integer> maximum =
                Math::max;

        System.out.println(
                maximum.apply(15, 25)
        );
    }
}
