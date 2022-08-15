package ch.engenius.bank.domain;

import ch.engenius.bank.exception.AccountException;
import ch.engenius.bank.model.Money;
import lombok.NonNull;
import lombok.Synchronized;

import java.math.BigDecimal;

@NonNull
public class Account {

    private Money money;

    public Account() {
        this(new Money(BigDecimal.ZERO));
    }

    public Account(Money money) {
        this.money = money;
    }

    @Synchronized
    public void withdraw(Money amount) throws AccountException {
        if (money.getAmount().compareTo(amount.getAmount()) < 0) {
            throw new AccountException("Not enough credit on account");
        }

        this.money = money.subtract(amount);
    }

    @Synchronized
    public void deposit(Money amount) throws AccountException {
        this.money = money.add(amount);
    }

    public Money getMoney() {
        return money;
    }
}
