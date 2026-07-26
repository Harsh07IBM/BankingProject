package org.example;

@FunctionalInterface
interface Action {

    abstract void perform(int value);

    default void showMessage() {
        System.out.println("Welcome to Java");
    }

    default void displayInfo() {
        System.out.println("This is another default method");
    }

    static void showStaticMessage() {
        System.out.println("This is a static method");
    }
}

public class Day6_FunctionalInterface {

    public static void main(String[] args) {

        Action action =
                value -> System.out.println(
                        "Result: " + (value * 2)
                );

        action.perform(50);

        action.showMessage();

        action.displayInfo();

        Action.showStaticMessage();
    }
}
