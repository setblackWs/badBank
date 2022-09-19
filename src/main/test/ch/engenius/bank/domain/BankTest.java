package ch.engenius.bank.domain;

import ch.engenius.bank.exceptions.NotEnoughCreditsException;
import ch.engenius.bank.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BankTest {
    private static final UUID BANK_ACCOUNT_ONE_ID = UUID.nameUUIDFromBytes("first account".getBytes());
    private static final UUID BANK_ACCOUNT_TWO_ID = UUID.nameUUIDFromBytes("second account".getBytes());
    private static final BigDecimal DEFAULT_MONEY_AMOUNT_TEN = BigDecimal.TEN;
    private static final BigDecimal TOO_BIG_AMOUNT_FOR_WITHDRAW = DEFAULT_MONEY_AMOUNT_TEN.multiply(DEFAULT_MONEY_AMOUNT_TEN);

    private final Bank bank = new Bank();

    @BeforeEach
    public void setUp() {
        HashMap<UUID, BankAccount> accounts = new HashMap<>();
        BankAccount bankAccountOne = new BankAccount(BANK_ACCOUNT_ONE_ID, DEFAULT_MONEY_AMOUNT_TEN);
        BankAccount bankAccountTwo = new BankAccount(BANK_ACCOUNT_TWO_ID, DEFAULT_MONEY_AMOUNT_TEN);
        accounts.put(BANK_ACCOUNT_ONE_ID, bankAccountOne);
        accounts.put(BANK_ACCOUNT_TWO_ID, bankAccountTwo);
        bank.setAccounts(accounts);
    }

    @Test
    public void shouldRegisterNewBankAccountSuccessfully() {
        UUID bankAccountIdExpected = UUID.randomUUID();
        BigDecimal bankAccountAmountExpected = BigDecimal.TEN;
        bank.registerBankAccount(bankAccountIdExpected, bankAccountAmountExpected);

        BankAccount actualBankAccount = bank.getAccounts().get(bankAccountIdExpected);
        assertNotNull(actualBankAccount);
        assertEquals(bankAccountIdExpected, actualBankAccount.getAccountNumber());
        assertEquals(bankAccountAmountExpected, actualBankAccount.getMoneyAmount());
    }

    @Test
    public void shouldThrowNotFoundExceptionWhenBankAccountWithProvidedIdDoesNotExistWhenTransferingMoney() {
        try {
            bank.transferMoney(BANK_ACCOUNT_ONE_ID, UUID.randomUUID(), BigDecimal.ONE);
        } catch (NotFoundException e) {
            assertEquals("Bank account not found.", e.getMessage());
        }
    }

    @Test
    public void shouldThrowNotEnoughCreditsExceptionWhenBankAccountWithProvidedIdDoesNotExistWhenTransferingMoney() {
        try {
            bank.transferMoney(BANK_ACCOUNT_ONE_ID, BANK_ACCOUNT_TWO_ID, TOO_BIG_AMOUNT_FOR_WITHDRAW);
        } catch (NotEnoughCreditsException e) {
            assertEquals("Not enough credits on bank account.", e.getMessage());
        }
    }

    @Test
    public void shouldTransferMoneySuccessfully() {
        BigDecimal moneyAmount = BigDecimal.ONE;
        bank.transferMoney(BANK_ACCOUNT_ONE_ID, BANK_ACCOUNT_TWO_ID, BigDecimal.ONE);

        BankAccount bankAccountOne = bank.getAccounts().get(BANK_ACCOUNT_ONE_ID);
        BankAccount bankAccountTwo = bank.getAccounts().get(BANK_ACCOUNT_TWO_ID);

        assertEquals(0, bankAccountOne.getMoneyAmount().compareTo(DEFAULT_MONEY_AMOUNT_TEN.subtract(moneyAmount)));
        assertEquals(0, bankAccountTwo.getMoneyAmount().compareTo(DEFAULT_MONEY_AMOUNT_TEN.add(moneyAmount)));
    }

    @Test
    public void shouldThrowNotFoundExceptionWhenBankAccountWithProvidedIdDoesNotExist() {
        try {
            bank.getBankAccount(UUID.randomUUID());
        } catch (NotFoundException e) {
            assertEquals("Bank account not found.", e.getMessage());
        }
    }

    @Test
    public void shouldReturnAccountSuccessfully() {
        BankAccount bankAccountExpected = bank.getAccounts().get(BANK_ACCOUNT_ONE_ID);

        BankAccount actualBankAccount = bank.getBankAccount(BANK_ACCOUNT_ONE_ID);
        assertNotNull(actualBankAccount);
        assertEquals(bankAccountExpected.getAccountNumber(), actualBankAccount.getAccountNumber());
        assertEquals(bankAccountExpected.getMoneyAmount(), actualBankAccount.getMoneyAmount());
    }
}
