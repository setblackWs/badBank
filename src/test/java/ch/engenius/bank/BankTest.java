package ch.engenius.bank;

import ch.engenius.bank.domain.Account;
import ch.engenius.bank.domain.Bank;
import ch.engenius.bank.model.AccountNumber;
import ch.engenius.bank.model.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BankTest {
    private Bank bank;
    private Money initialAccountMoney;

    @BeforeEach
    public void setUp() {
        bank = new Bank();
        initialAccountMoney = new Money(BigDecimal.valueOf(100));
    }

    @Test
    void shouldRegisterAccount_whenAccountNumberAndMoneyProvided() {
        AccountNumber accountNumber = new AccountNumber(1);

        assertNotNull(bank.registerAccount(accountNumber, initialAccountMoney));
    }

    @Test
    void shouldTransferMoney_whenPayerAccountAndPayeeAccountAndMoneyProvided() {
        Money transferMoney = new Money(BigDecimal.valueOf(20));

        AccountNumber payerAccountNumber = new AccountNumber(1);
        bank.registerAccount(payerAccountNumber, initialAccountMoney);

        AccountNumber payeeAccountNumber = new AccountNumber(2);
        bank.registerAccount(payeeAccountNumber, initialAccountMoney);

        bank.transferMoney(payerAccountNumber, payeeAccountNumber, transferMoney);

        Account payer = bank.getAccount(payerAccountNumber);
        Account payee = bank.getAccount(payeeAccountNumber);

        assertEquals(payer.getMoney().getAmount(),
                initialAccountMoney.getAmount().subtract(transferMoney.getAmount()));
        assertEquals(payee.getMoney().getAmount(),
                initialAccountMoney.getAmount().add(transferMoney.getAmount()));
    }

    @Test
    void shouldGetAccount_whenAccountNumberProvided() {
        AccountNumber accountNumber = new AccountNumber(1);
        bank.registerAccount(accountNumber, initialAccountMoney);

        Account account = bank.getAccount(accountNumber);

        assertNotNull(account);
    }
}