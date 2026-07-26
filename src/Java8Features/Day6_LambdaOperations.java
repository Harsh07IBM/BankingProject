package org.example;

@FunctionalInterface
interface Operation {
    int execute(int a, int b);
}

public class Day6_LambdaOperations {

    public static void main(String[] args) {

        Operation addition =
                (a, b) -> a + b;

        Operation multiplication =
                (a, b) -> a * b;

        Operation subtraction =
                (a, b) -> a - b;

        System.out.println(
                "Addition: "
                        + addition.execute(6, 3)
        );

        System.out.println(
                "Multiplication: "
                        + multiplication.execute(4, 5)
        );

        System.out.println(
                "Subtraction: "
                        + subtraction.execute(10, 4)
        );
    }
}
