package com.example.banking.controller;

import com.example.banking.entity.Account;
import com.example.banking.entity.Transaction;
import com.example.banking.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    // POST /accounts
    @PostMapping
    public Account createAccount(@RequestBody Map<String, Object> request) {
        String name = (String) request.get("accountHolderName");
        double balance = Double.parseDouble(request.get("initialBalance").toString());
        return accountService.createAccount(name, balance);
    }

    // GET /accounts/{accountId}  ← This is the REDIS demo endpoint
    @GetMapping("/{accountId}")
    public Account getAccount(@PathVariable Long accountId) {
        return accountService.getAccount(accountId);
    }

    // POST /accounts/{accountId}/credit
    @PostMapping("/{accountId}/credit")
    public Account credit(@PathVariable Long accountId, @RequestBody Map<String, Object> request) {
        double amount = Double.parseDouble(request.get("amount").toString());
        return accountService.credit(accountId, amount);
    }

    // POST /accounts/{accountId}/debit
    @PostMapping("/{accountId}/debit")
    public Account debit(@PathVariable Long accountId, @RequestBody Map<String, Object> request) {
        double amount = Double.parseDouble(request.get("amount").toString());
        return accountService.debit(accountId, amount);
    }

    // GET /accounts/{accountId}/transactions
    @GetMapping("/{accountId}/transactions")
    public List<Transaction> getTransactions(@PathVariable Long accountId) {
        return accountService.getTransactions(accountId);
    }

    // DELETE /accounts/{accountId}  ← Demonstrates @CacheEvict
    @DeleteMapping("/{accountId}")
    public ResponseEntity<String> deleteAccount(@PathVariable Long accountId) {
        accountService.deleteAccount(accountId);
        return ResponseEntity.ok("Account " + accountId + " deleted.");
    }
}
