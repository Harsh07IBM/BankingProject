package Junit;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day7_NestedBankAccountTests {

    @Nested
    class DepositScenarios {

        @Test
        void testSuccessfulDeposit() {

            Day7_BankAccount account =
                    new Day7_BankAccount(
                            "ACC200",
                            500.0
                    );

            account.deposit(200.0);

            assertEquals(700.0, account.getBalance());
        }

        @Test
        void testIncorrectDepositResult() {

            Day7_BankAccount account =
                    new Day7_BankAccount(
                            "ACC200",
                            500.0
                    );

            account.deposit(200.0);

            assertEquals(800.0, account.getBalance());
        }
    }

    @Nested
    class WithdrawalScenarios {

        @Test
        void testSuccessfulWithdrawal() {

            Day7_BankAccount account =
                    new Day7_BankAccount(
                            "ACC200",
                            500.0
                    );

            account.withdraw(100.0);

            assertEquals(400.0, account.getBalance());
        }

        @Test
        void testIncorrectWithdrawalResult() {

            Day7_BankAccount account =
                    new Day7_BankAccount(
                            "ACC200",
                            500.0
                    );

            account.withdraw(100.0);

            assertEquals(300.0, account.getBalance());
        }
    }
}
