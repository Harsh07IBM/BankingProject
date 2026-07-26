package org.example;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Day6_ZonedDateTime {

    public static void main(String[] args) {

        ZonedDateTime current =
                ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));

        System.out.println("India Date and Time: " + current);
    }
}
