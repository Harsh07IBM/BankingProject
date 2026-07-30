package com.example.BankTestDemo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BankServiceTest {

    @Test
    void testDeposit() {
        BankRepository mockRepo = Mockito.mock(BankRepository.class);

        BankAccount account = new BankAccount("123", 500.0);

        when(mockRepo.findByAccountNumber("123")).thenReturn(account);

        BankService service = new BankService(mockRepo);

        service.deposit("123", 200.0);

        System.out.println("Test deposit completed. Final balance: " + account.getBalance());
        assertEquals(700.0, account.getBalance());
        verify(mockRepo).save(account);
    }

    @Test
    void testWithdrawSuccess() {
        BankRepository mockRepo = Mockito.mock(BankRepository.class);

        BankAccount account = new BankAccount("123", 500.0);

        when(mockRepo.findByAccountNumber("123")).thenReturn(account);

        BankService service = new BankService(mockRepo);

        service.withdraw("123", 200.0);

        System.out.println("Test withdrawal completed. Final balance: " + account.getBalance());
        assertEquals(300.0, account.getBalance());
        verify(mockRepo).save(account);
    }

    @Test
    void testWithdrawInsufficientFunds() {
        BankRepository mockRepo = Mockito.mock(BankRepository.class);

        BankAccount account = new BankAccount("123", 100.0);

        when(mockRepo.findByAccountNumber("123")).thenReturn(account);

        BankService service = new BankService(mockRepo);

        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.withdraw("123", 200.0)
        );

        System.out.println("Test insufficient funds handled. Exception: " + ex.getMessage());
        assertEquals("Insufficient funds", ex.getMessage());
        verify(mockRepo, never()).save(account);
    }
}