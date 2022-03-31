package ch.engenius.bank.data;

import java.math.BigDecimal;


public class Account {

  private BigDecimal money;

  public Account(BigDecimal money) {
    this.money = money;
  }

  public void withdraw(BigDecimal amount) {

    if (amount.signum() <= 0) {
      throw new IllegalArgumentException("Withdrawal amount must be positive");
    }
    if (money.compareTo(amount) < 0) {
      throw new IllegalStateException("Not enough credits on account");
    }
    money = money.subtract(amount);
  }

  public void deposit(BigDecimal amount) {

    if (amount.signum() < 0) {
      throw new IllegalArgumentException("Deposit must be positive number");
    }
    money = money.add(amount);
  }

  public BigDecimal getMoney() {
    return money;
  }
}
