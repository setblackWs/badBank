package ch.engenius.bank.domain;

import ch.engenius.bank.exception.AccountException;
import ch.engenius.bank.model.AccountNumber;
import ch.engenius.bank.model.Money;
import lombok.NonNull;
import lombok.Synchronized;

@NonNull
public class Account {
    private Money money;
    private final AccountNumber accountNumber;

    public Account(AccountNumber accountNumber, Money money) {
        this.accountNumber = accountNumber;
        this.money = money;
    }

    @Synchronized
    public void withdraw(Money amount) {
        if (money.getAmount().compareTo(amount.getAmount()) < 0) {
            throw new AccountException("Not enough credit on account");
        }

        this.money = money.subtract(amount);
    }

    @Synchronized
    public void deposit(Money amount) {
        this.money = money.add(amount);
    }

    public Money getMoney() {
        return money;
    }

    public AccountNumber getAccountNumber() {
        return accountNumber;
    }
}
