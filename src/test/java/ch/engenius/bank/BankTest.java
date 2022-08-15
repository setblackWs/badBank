package ch.engenius.bank;

import ch.engenius.bank.domain.Account;
import ch.engenius.bank.domain.Bank;
import ch.engenius.bank.exception.BankException;
import ch.engenius.bank.model.AccountNumber;
import ch.engenius.bank.model.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class BankTest {
    private Bank bank;
    public static final int ACCOUNT_NUMBER = 1;
    private final BigDecimal initialAccountMoney = BigDecimal.valueOf(100);

    @BeforeEach
    public void setUp() {
        bank = new Bank();
    }

    @Test
    void shouldRegisterAccount_whenAccountNumberAndMoneyProvided() throws BankException {
        AccountNumber accountNumber = new AccountNumber(ACCOUNT_NUMBER);
        Money money = new Money(initialAccountMoney);

        bank.registerAccount(accountNumber, money);

        Account account = bank.getAccount(accountNumber);
        assertNotNull(account);
    }
}