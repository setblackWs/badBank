package ch.engenius.bank;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BankTest {

    private Bank bank;

    @BeforeEach
    void setUp() {
        bank = new Bank();
    }

    @Test
    void testRegisterAccount() {
        Account account = bank.registerAccount(1, new BigDecimal("1000"));
        assertEquals(new BigDecimal("1000"), account.getBalance());
    }

    @Test
    void testRegisterDuplicateAccount() {
        bank.registerAccount(1, new BigDecimal("1000"));
        assertThrows(IllegalArgumentException.class, () -> bank.registerAccount(1, new BigDecimal("500")));
    }

    @Test
    void testRegisterNegativeAmount() {
        assertThrows(IllegalArgumentException.class, () -> bank.registerAccount(1, new BigDecimal("-500")));
    }

    @Test
    void testGetAccount() {
        Account account = bank.registerAccount(1, new BigDecimal("1000"));
        assertEquals(account, bank.getAccount(1));
    }

    @Test
    void testGetNonExistentAccount() {
        assertThrows(IllegalArgumentException.class, () -> bank.getAccount(1));
    }

    @Test
    void testTransfer() {
        bank.registerAccount(1, new BigDecimal("1000"));
        bank.registerAccount(2, new BigDecimal("500"));
        bank.transfer(1, 2, new BigDecimal("500"));
        assertEquals(new BigDecimal("500"), bank.getAccount(1).getBalance());
        assertEquals(new BigDecimal("1000"), bank.getAccount(2).getBalance());
    }

    @Test
    void testTransferSameAccount() {
        bank.registerAccount(1, new BigDecimal("1000"));
        assertThrows(IllegalArgumentException.class, () -> bank.transfer(1, 1, new BigDecimal("500")));
    }

    @Test
    void testTransferNonExistentAccounts() {
        assertThrows(IllegalArgumentException.class, () -> bank.transfer(1, 2, new BigDecimal("500")));
    }

    @Test
    void testTransferInsufficientFunds() {
        bank.registerAccount(1, new BigDecimal("500"));
        bank.registerAccount(2, new BigDecimal("500"));
        assertThrows(IllegalStateException.class, () -> bank.transfer(1, 2, new BigDecimal("1000")));
    }

    @Test
    void testTransferLocking() throws InterruptedException {
        Bank bank = new Bank();
        Account account = bank.registerAccount(1, new BigDecimal("1000.00"));

        Thread thread1 = new Thread(() -> {
            account.withdraw(new BigDecimal("500.00"));
        });

        Thread thread2 = new Thread(() -> {
            assertThrows(IllegalStateException.class, () -> account.withdraw(new BigDecimal("800.00")));
        });

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();

        assertEquals(new BigDecimal("500.00"), account.getBalance());
    }
}
