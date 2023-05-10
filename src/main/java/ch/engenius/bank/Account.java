package ch.engenius.bank;

import java.math.BigDecimal;
import java.util.concurrent.locks.ReentrantLock;

public class Account {
    private BigDecimal balance = BigDecimal.ZERO;
    private final ReentrantLock lock = new ReentrantLock();

    /**
     * Withdraws the specified amount from this account.
     * @param amount the amount to withdraw
     * @throws IllegalArgumentException if the amount is null or non-positive
     * @throws IllegalStateException if there is not enough balance to cover the withdrawal
     */
    public void withdraw(BigDecimal amount) {
        validateAmount(amount);
        lock.lock();
        try {
            if (!hasEnoughBalance(amount)) {
                throw new IllegalStateException("Not enough credits on account!");
            }
            balance = balance.subtract(amount);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Deposits the specified amount into this account.
     * @param amount the amount to deposit
     * @throws IllegalArgumentException if the amount is null or non-positive
     */
    public void deposit(BigDecimal amount) {
        validateAmount(amount);
        lock.lock();
        try {
            balance = balance.add(amount);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Returns the current balance of this account.
     * @return the current balance
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * Validates the specified amount.
     * @param amount the amount to validate
     * @throws IllegalArgumentException if the amount is null or non-positive
     */
    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive and not null.");
        }
    }

    /**
     * Checks if this account has enough balance to cover the specified amount.
     * @param amount the amount to check
     * @return true if there is enough balance, false otherwise
     */
    private boolean hasEnoughBalance(BigDecimal amount) {
        return balance.compareTo(amount) >= 0;
    }
}
