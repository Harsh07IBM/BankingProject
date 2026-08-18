package com.example.banking.service;

import com.example.banking.entity.Account;
import com.example.banking.entity.Transaction;
import com.example.banking.repository.AccountRepository;
import com.example.banking.repository.TransactionRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepo;
    private final TransactionRepository txnRepo;

    public AccountService(AccountRepository accountRepo, TransactionRepository txnRepo) {
        this.accountRepo = accountRepo;
        this.txnRepo = txnRepo;
    }

    // ==================== CREATE ACCOUNT ====================

    public Account createAccount(String name, double initialBalance) {
        Account account = new Account(name, initialBalance);
        return accountRepo.save(account);
    }

    // ==================== GET ACCOUNT — @Cacheable ====================
    //
    // @Cacheable = Cache-Aside pattern
    //   1st call → Cache MISS → hits database → stores result in Redis → returns
    //   2nd call → Cache HIT  → returns directly from Redis (no DB hit!)
    //
    // The println proves it: you'll only see "Fetching from DB" on a cache miss.

    @Cacheable(value = "accounts", key = "#accountId")
    public Account getAccount(Long accountId) {
        System.out.println(">>> Fetching account from DATABASE... (Cache MISS)");
        return accountRepo.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found: " + accountId));
    }

    // ==================== CREDIT — @CachePut ====================
    //
    // @CachePut = always runs the method AND updates Redis with the return value.
    // After credit, Redis will have the updated balance,
    // so the next GET returns the new balance from cache.

    @CachePut(value = "accounts", key = "#accountId")
    @Transactional
    public Account credit(Long accountId, double amount) {
        Account account = accountRepo.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found: " + accountId));

        account.setBalance(account.getBalance() + amount);
        accountRepo.save(account);

        // Save transaction record
        txnRepo.save(new Transaction(accountId, "CREDIT", amount, account.getBalance()));

        System.out.println(">>> Credited ₹" + amount + " → New Balance: ₹" + account.getBalance());
        return account;  // This return value goes into Redis cache
    }

    // ==================== DEBIT — @CachePut ====================

    @CachePut(value = "accounts", key = "#accountId")
    @Transactional
    public Account debit(Long accountId, double amount) {
        Account account = accountRepo.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found: " + accountId));

        if (account.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance! Available: ₹" + account.getBalance());
        }

        account.setBalance(account.getBalance() - amount);
        accountRepo.save(account);

        // Save transaction record
        txnRepo.save(new Transaction(accountId, "DEBIT", amount, account.getBalance()));

        System.out.println(">>> Debited ₹" + amount + " → New Balance: ₹" + account.getBalance());
        return account;  // This return value goes into Redis cache
    }

    // ==================== DELETE — @CacheEvict ====================
    //
    // @CacheEvict = removes the entry from Redis.
    // After delete, the cached data won't be served anymore.

    @CacheEvict(value = "accounts", key = "#accountId")
    public void deleteAccount(Long accountId) {
        accountRepo.deleteById(accountId);
        System.out.println(">>> Deleted account " + accountId + " and evicted from Redis.");
    }

    // ==================== TRANSACTION HISTORY (no cache) ====================

    public List<Transaction> getTransactions(Long accountId) {
        return txnRepo.findByAccountIdOrderByTimestampDesc(accountId);
    }
}
