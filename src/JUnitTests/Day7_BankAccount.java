package Junit;

public class Day7_BankAccount {

    private String accountId;
    private double balance;

    public Day7_BankAccount(String accountId, double balance) {
        this.accountId = accountId;
        this.balance = balance;
    }

    public String getAccountId() {
        return accountId;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {

        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "Deposit amount must be positive"
            );
        }

        balance += amount;
    }

    public void withdraw(double amount) {

        if (amount > balance) {
            throw new IllegalArgumentException(
                    "Insufficient balance"
            );
        }

        balance -= amount;
    }
}
