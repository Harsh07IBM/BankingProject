package org.example;

public class Day5_ExceptionHandling {

    public static void main(String[] args) {

        try {

            int firstNumber = 10;
            int secondNumber = 0;

            int result = firstNumber / secondNumber;

            System.out.println(
                    "Result: " + result
            );

        } catch (ArithmeticException e) {

            System.out.println(
                    "Exception occurred: Cannot divide by zero"
            );

        } finally {

            System.out.println(
                    "Finally block is executed"
            );
        }
    }
}
