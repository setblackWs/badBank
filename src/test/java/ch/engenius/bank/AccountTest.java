package ch.engenius.bank;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class AccountTest {
    private Account account;

    @BeforeEach
    public void setUp() {
        account = new Account();
    }

    @Test
    public void withdrawFromAccount_whenInsufficientBalance_returnsFalse() throws InterruptedException {
        BigDecimal initialBalance = new BigDecimal("100");
        account.deposit(initialBalance);

        BigDecimal withdrawAmount = new BigDecimal("150");

        boolean result = account.withdraw(withdrawAmount);

        assertFalse(result, "Withdraw should return false when there is insufficient balance");
        assertEquals(initialBalance, account.getMoney(), "Balance should remain unchanged after a failed withdraw");
    }

    @Test
    public void whenDepositToAccount_withPositiveAmount_balanceIsIncreased() throws InterruptedException {
        BigDecimal initialBalance = account.getMoney();

        BigDecimal depositAmount = new BigDecimal("100");
        account.deposit(depositAmount);

        BigDecimal expectedBalance = initialBalance.add(depositAmount);
        assertEquals(expectedBalance, account.getMoney(), "Deposit should increase the balance");
    }

    @Test
    public void whenWithdrawFromAccount_withSufficientAmount_balanceIsDecreased() throws InterruptedException {
        BigDecimal initialBalance = new BigDecimal("200");
        account.deposit(initialBalance);

        BigDecimal withdrawAmount = new BigDecimal("50");
        account.withdraw(withdrawAmount);

        BigDecimal expectedBalance = initialBalance.subtract(withdrawAmount);
        assertEquals(expectedBalance, account.getMoney(), "Withdraw should decrease the balance");
    }

    @Test
    public void whenWithdrawFromAccount_withNegativeAmount_returnFalse() throws InterruptedException {
        BigDecimal initialBalance = new BigDecimal("100");
        account.deposit(initialBalance);

        BigDecimal withdrawAmount = new BigDecimal("-50");

        boolean result = account.withdraw(withdrawAmount);

        assertFalse(result, "Amount should be greater or equal to zero");
        assertEquals(initialBalance, account.getMoney(), "Balance should remain unchanged after a failed withdraw");
    }

    @Test
    public void whenWithdrawFromAccount_withZeroAmount_returnFalse() throws InterruptedException {
        BigDecimal initialBalance = new BigDecimal("100");
        account.deposit(initialBalance);

        BigDecimal withdrawAmount = BigDecimal.ZERO;
        boolean result = account.withdraw(withdrawAmount);

        assertFalse(result, "Amount should be greater or equal to zero");
        assertEquals(initialBalance, account.getMoney(), "Balance should remain unchanged after a failed withdraw");
    }
}
