package org.example;

interface Product {
    int calculate(int first, int second);
}

public class Day6_LambdaMultiplication {

    public static void main(String[] args) {

        Product product =
                (first, second) -> first * second;

        int answer = product.calculate(10, 20);

        System.out.println("Product is: " + answer);
    }
}
