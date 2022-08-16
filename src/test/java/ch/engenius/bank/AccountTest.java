package ch.engenius.bank;

import ch.engenius.bank.domain.Account;
import ch.engenius.bank.model.AccountNumber;
import ch.engenius.bank.model.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountTest {
    private Account account;
    private final Money initialAccountMoney = new Money(BigDecimal.valueOf(100));
    private final AccountNumber defaultAccountNumber = new AccountNumber(1);

    @BeforeEach
    public void setUp() {
        account = new Account(defaultAccountNumber, initialAccountMoney);
    }

    @Test
    void shouldWithdrawMoney_whenAmountProvided() {
        final BigDecimal withdrawAmount = BigDecimal.valueOf(10);
        Money withdrawMoney = new Money(withdrawAmount);

        account.withdraw(withdrawMoney);
        assertEquals(initialAccountMoney.getAmount().subtract(withdrawMoney.getAmount()), account.getMoney().getAmount());
    }

    @Test
    void shouldDepositMoney_whenAmountProvided() {
        final BigDecimal depositAmount = BigDecimal.valueOf(10);
        Money depositMoney = new Money(depositAmount);

        account.deposit(depositMoney);
        assertEquals(initialAccountMoney.getAmount().add(depositMoney.getAmount()), account.getMoney().getAmount());
    }
}