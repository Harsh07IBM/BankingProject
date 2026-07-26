package org.example;

interface Calculation {
    int calculate(int a, int b);
}

public class Day6_LambdaCalculator {

    public static void main(String[] args) {

        Calculation addition =
                (a, b) -> a + b;

        Calculation subtraction =
                (a, b) -> a - b;

        Calculation multiplication =
                (a, b) -> a * b;

        Calculation division =
                (a, b) -> a / b;

        System.out.println("Addition: "
                + addition.calculate(20, 10));

        System.out.println("Subtraction: "
                + subtraction.calculate(20, 10));

        System.out.println("Multiplication: "
                + multiplication.calculate(20, 10));

        System.out.println("Division: "
                + division.calculate(20, 10));
    }
}
