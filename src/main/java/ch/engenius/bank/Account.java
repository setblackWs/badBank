package ch.engenius.bank;

import java.math.BigDecimal;

public class Account {
    private double money;

    public void withdraw(double amount) {
        if (money - amount < 0) {
            throw new IllegalStateException("not enough credits on account");
        }
        setMoney(money - amount);

    }

    public void deposit(double amount) {
        if(amount < 0) {
            throw new IllegalArgumentException("negative amounts cannot be deposited");
        }
        setMoney(money + amount);
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public BigDecimal getMoneyAsBigDecimal() {
        return BigDecimal.valueOf(money);
    }
}
