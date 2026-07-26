package org.example;

import java.util.ArrayList;
import java.util.function.Supplier;

public class Day6_ConstructorReference {

    public static void main(String[] args) {

        Supplier<ArrayList<String>> listCreator =
                ArrayList::new;

        ArrayList<String> names =
                listCreator.get();

        names.add("Harsh");
        names.add("Ravi");

        System.out.println(names);
    }
}
