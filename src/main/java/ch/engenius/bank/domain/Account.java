package ch.engenius.bank.domain;

import java.math.BigDecimal;
import java.util.concurrent.locks.ReentrantLock;

public class Account {

    private BigDecimal money;
    private ReentrantLock lock;

    public Account() {
        this.money = BigDecimal.ZERO;
        this.lock = new ReentrantLock();
    }

    public Account(BigDecimal money) {
        this.lock = new ReentrantLock();
        try {
            validAmount(money);
            this.money = money;
        } catch (IllegalArgumentException e) {
            this.money = BigDecimal.ZERO;
            System.out.println(e.getMessage());
        }
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void withdraw(BigDecimal amount) {
        lock.lock();
        try {
            validAmount(amount);
            if (money.subtract(amount).compareTo(BigDecimal.ZERO) < 0)
                throw new IllegalStateException("Not enough credits on account");

            money = money.subtract(amount);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } finally {
            lock.unlock();
        }
    }

    public void deposit(BigDecimal amount) {
        lock.lock();
        try {
            validAmount(amount);
            money = money.add(amount);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } finally {
            lock.unlock();
        }
    }

    private void validAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Amount should have positive value");
    }
}
