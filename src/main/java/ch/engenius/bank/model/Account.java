package ch.engenius.bank.model;

import java.math.BigDecimal;

public class Account {
    private double money;

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
