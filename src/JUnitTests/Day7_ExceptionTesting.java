package Junit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class Day7_ExceptionTesting {

    @Test
    void testInsufficientBalance() {

        Day7_BankAccount account =
                new Day7_BankAccount(
                        "ACC101",
                        100.0
                );

        Exception exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> account.withdraw(200.0)
                );

        assertEquals(
                "Insufficient balance",
                exception.getMessage()
        );
    }
}
