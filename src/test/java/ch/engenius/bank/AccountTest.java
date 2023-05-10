package ch.engenius.bank;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

class AccountTest {

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.deposit(new BigDecimal("1000"));
    }

    @Test
    void testWithdraw() {
        account.withdraw(new BigDecimal("500"));
        assertEquals(new BigDecimal("500"), account.getBalance());
    }

    @Test
    void testWithdrawInvalidAmount() {
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(new BigDecimal("-100")));
    }

    @Test
    void testWithdrawInsufficientBalance() {
        assertThrows(IllegalStateException.class, () -> account.withdraw(new BigDecimal("1500")));
    }

    @Test
    void testDeposit() {
        account.deposit(new BigDecimal("500"));
        assertEquals(new BigDecimal("1500"), account.getBalance());
    }

    @Test
    void testDepositInvalidAmount() {
        assertThrows(IllegalArgumentException.class, () -> account.deposit(new BigDecimal("-100")));
    }
}
