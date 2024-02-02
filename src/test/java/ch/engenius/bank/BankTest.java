package ch.engenius.bank;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class BankTest {
    private Bank bank;

    @BeforeEach
    public void setUp() {
        bank = new Bank();
    }

    @Test
    void givenAccountNumberAndDeposit_whenRegisterAccount_accountShouldBeAddedToBank() throws InterruptedException {
        int accountNumber = 1;
        BigDecimal initialDeposit = new BigDecimal("500");

        bank.registerAccount(accountNumber, initialDeposit);

        assertNotNull(bank.getAccount(accountNumber), "Registered account should not be null");
        assertEquals(initialDeposit, bank.getAccount(accountNumber).getMoney(), "Initial deposit should match");
    }

    @Test
    public void getNonExistentAccount_returnsNull() {
        int nonExistentAccountNumber = 99;
        assertNull(bank.getAccount(nonExistentAccountNumber), "Getting a non-existent account should return null");
    }

    @Test
    void testTransferWithThreads() throws InterruptedException {
        Account account = new Account();
        BigDecimal initialBalance = new BigDecimal("1000");
        account.deposit(initialBalance);

        Thread thread1 = new Thread(() -> {
            try {
                account.withdraw(new BigDecimal("700"));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
               account.deposit(new BigDecimal("100"));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();

        assertEquals(new BigDecimal("400"), account.getMoney());
    }
}
