package Junit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day7_BankAccountFailTests {

    @Test
    void testWrongDepositBalance() {

        Day7_BankAccount account =
                new Day7_BankAccount(
                        "ACC101",
                        500.0
                );

        account.deposit(200.0);

        assertEquals(
                800.0,
                account.getBalance(),
                "Expected 800 but got "
                        + account.getBalance()
        );
    }

    @Test
    void testWrongWithdrawBalance() {

        Day7_BankAccount account =
                new Day7_BankAccount(
                        "ACC101",
                        500.0
                );

        account.withdraw(200.0);

        assertEquals(
                400.0,
                account.getBalance(),
                "Expected 400 but got "
                        + account.getBalance()
        );
    }

    @Test
    void testExpectedExceptionFails() {

        Day7_BankAccount account =
                new Day7_BankAccount(
                        "ACC101",
                        100.0
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> account.withdraw(50.0)
        );
    }
}
