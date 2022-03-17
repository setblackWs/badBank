package ch.engenius.bank;

import java.math.BigDecimal;

public class Account {
    private BigDecimal money;

    public Account(BigDecimal money) {
        this.money = money;
    }

    public void withdraw(double amount) {
        if (transactionAllowed(BigDecimal.valueOf(amount))) {
            throw new IllegalStateException("not enough credits on account");
        }
        this.money = money.subtract(BigDecimal.valueOf(amount));
    }

    public void deposit(double amount) {
        this.money = money.add(BigDecimal.valueOf(amount));
    }

    public BigDecimal getMoney() {
        return new BigDecimal(String.valueOf(this.money));
    }

    private boolean transactionAllowed(BigDecimal amount) {
        return this.money
                .subtract(amount)
                .compareTo(BigDecimal.ZERO) < 0;
    }

}
