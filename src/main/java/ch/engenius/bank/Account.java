package ch.engenius.bank;

import java.math.BigDecimal;

public class Account {
    private BigDecimal money;

    /*
    * Creates the account with zero credit.
    * */
    public Account() {
        this(BigDecimal.ZERO);
    }

    /*
    * Creates the account with given amount of credit.
    *
    * @param money  Initial amount of credit for the account
    * */
    public Account(BigDecimal money) {
        this.money = money;
    }

    /*
    * Withdraws given amount of credit from the account.
    *
    * @param  amount                    Amount to withdraw
    * @throws IllegalArgumentException  If given amount is bellow or equal zero
    * @throws IllegalStateException     If given amount is larger than credits on the account
    * */
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

    /*
    * Deposits given amount of credit to the account.
    *
    * @param  amount                    Amount to deposit
    * @throws IllegalArgumentException  If given amount is bellow or equal zero
    * */
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
