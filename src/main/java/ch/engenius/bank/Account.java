package ch.engenius.bank;

import java.math.BigDecimal;

public class Account {
    private BigDecimal money;

    public Account() {
        this(BigDecimal.ZERO);
    }

    public Account(BigDecimal money) {
        this.money = money;
    }

    public void withdraw(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 1) {
            throw new IllegalArgumentException("cannot withdraw zero or negative amount");
        }

        synchronized (this) {
            if (money.subtract(amount).compareTo(BigDecimal.ZERO) == -1) {
                throw new IllegalStateException("not enough credit");
            }

            setMoney(money.subtract(amount));
        }
    }

    public void deposit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 1) {
            throw new IllegalArgumentException("cannot deposit zero or negative amount");
        }

        synchronized (this) {
            setMoney(money.add(amount));
        }
    }

    public BigDecimal getMoney() {
        return money;
    }

    private void setMoney(BigDecimal money) {
        this.money = money;
    }
}
