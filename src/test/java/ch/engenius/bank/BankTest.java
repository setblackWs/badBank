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
    private final BigDecimal initialAccountMoney = BigDecimal.valueOf(100);

    @BeforeEach
    public void setUp() {
        bank = new Bank();
    }

    @Test
    void shouldRegisterAccount_whenAccountNumberAndMoneyProvided() {
        Money money = new Money(initialAccountMoney);
        AccountNumber accountNumber = new AccountNumber(1);

        bank.registerAccount(accountNumber, money);

        Account account = bank.getAccount(accountNumber);
        assertNotNull(account);
    }

    @Test
    void shouldTransferMoney_whenPayerAccountAndPayeeAccountAndMoneyProvided() {
        Money initialMoney = new Money(initialAccountMoney);
        Money transferMoney = new Money(BigDecimal.valueOf(20));

        AccountNumber payerAccountNumber = new AccountNumber(1);
        bank.registerAccount(payerAccountNumber, initialMoney);

        AccountNumber payeeAccountNumber = new AccountNumber(2);
        bank.registerAccount(payeeAccountNumber, initialMoney);

        bank.transferMoney(payerAccountNumber, payeeAccountNumber, transferMoney);

        Account payer = bank.getAccount(payerAccountNumber);
        Account payee = bank.getAccount(payeeAccountNumber);

        assertEquals(payer.getMoney().getAmount(),
                initialMoney.getAmount().subtract(transferMoney.getAmount()));
        assertEquals(payee.getMoney().getAmount(),
                initialMoney.getAmount().add(transferMoney.getAmount()));
    }
}