package com.example.BankTestDemo;

public class BankService {

    private BankRepository repository;

    public BankService(BankRepository repository) {
        this.repository = repository;
    }

    public void deposit(String accountNumber, double amount) {
        BankAccount account = repository.findByAccountNumber(accountNumber);
        account.setBalance(account.getBalance() + amount);
        repository.save(account);

        System.out.println("Deposit successful for account " + accountNumber + ". Amount: " + amount);
        System.out.println("Updated balance: " + account.getBalance());
    }

    public void withdraw(String accountNumber, double amount) {
        BankAccount account = repository.findByAccountNumber(accountNumber);

        if (account.getBalance() < amount) {
            System.out.println("Withdrawal failed. Insufficient funds for account " + accountNumber);
            throw new IllegalArgumentException("Insufficient funds");
        }

        account.setBalance(account.getBalance() - amount);
        repository.save(account);

        System.out.println("Withdrawal successful for account " + accountNumber + ". Amount: " + amount);
        System.out.println("Updated balance: " + account.getBalance());
    }

}