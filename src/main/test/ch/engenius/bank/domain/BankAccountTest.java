package ch.engenius.bank.domain;

import ch.engenius.bank.exceptions.NotEnoughCreditsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BankAccountTest {

    private static final BigDecimal DEFAULT_MONEY_AMOUNT_TEN = BigDecimal.TEN;
    private static final BigDecimal WITHDRAW_MONEY_AMOUNT_FIVE = BigDecimal.valueOf(5);
    private static final BigDecimal MONEY_DEPOSIT = BigDecimal.TEN;
    private final BankAccount bankAccount = new BankAccount();

    @BeforeEach
    public void setUp() {
        bankAccount.setAccountNumber(UUID.randomUUID());
        bankAccount.setMoneyAmount(DEFAULT_MONEY_AMOUNT_TEN);
    }

    @Test
    public void shouldThrowNotEnoughCreditsExceptionWhenTryingToWithdraw() {
        try {
            bankAccount.withdraw(DEFAULT_MONEY_AMOUNT_TEN.multiply(BigDecimal.TEN));
        } catch (NotEnoughCreditsException e) {
            assertEquals("Not enough credits on bank account.", e.getMessage());
        }
    }

    @Test
    public void shouldWithdrawSuccessfully() {
        bankAccount.withdraw(WITHDRAW_MONEY_AMOUNT_FIVE);

        assertEquals(DEFAULT_MONEY_AMOUNT_TEN.subtract(WITHDRAW_MONEY_AMOUNT_FIVE), bankAccount.getMoneyAmount());
    }

    @Test
    public void shouldDepositMoneySuccessfully() {
        bankAccount.deposit(MONEY_DEPOSIT);

        assertEquals(DEFAULT_MONEY_AMOUNT_TEN.add(MONEY_DEPOSIT), bankAccount.getMoneyAmount());
    }
}
