package ch.engenius.bank;

import ch.engenius.bank.api.AccountException;
import ch.engenius.bank.api.AccountTransactor;
import ch.engenius.bank.api.TransactionException;

import java.math.BigDecimal;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Standard implementation of a transactor using a pessimistic locking strategy.
 * For simplicity, I used the ReentrantLock for locking. However, this can and will
 * lead to deadlocks. So we would need to implement our own lock to prevent this and handle
 * this scenario, which needs to be specified by the business. One way could be to implement
 * a TimeoutLock, so the lock can be released after some time and the transaction can be rolled back and
 * be put in a list of transactions to retry or notify the "owner" of the transaction
 * that it failed.
 */
public class Account implements AccountTransactor {
    private BigDecimal money = BigDecimal.ZERO;
    /**
     * This is the shadow copy of the actual money this account has deposited.
     * It is used to withdraw or deposit money while in a transaction.
     */
    private BigDecimal shadowMoney = BigDecimal.ZERO;
    private Object currentTransactionKey = null;
    private Lock lock = new ReentrantLock();

    /**
     * THIS METHOD IS FOR CONVENIENCE ONLY TO CREATE ACCOUNTS EN MASSE FOR TESTING.
     *
     * @param money
     */
    public synchronized void setMoney(BigDecimal money) {
        this.money = money;
    }

    public synchronized BigDecimal getMoney() {
        return money;
    }

    @Override
    public void deposit(Object key, BigDecimal amount) throws TransactionException, AccountException {
        if (key == null) {
            throw new TransactionException("Transaction key cannot be null");
        }

        if (currentTransactionKey != key) {
            throw new TransactionException("Wrong transaction key");
        }

        if (amount == null) {
            throw new TransactionException("Cannot deposit null");
        }

        synchronized (this) {
            if (amount.compareTo(BigDecimal.ZERO) < 0) {
                throw new AccountException("Cannot deposit a negative amount");
            }

            shadowMoney = shadowMoney.add(amount);
        }

    }

    @Override
    public void withdraw(Object key, BigDecimal amount) throws TransactionException, AccountException {
        if (key == null) {
            throw new TransactionException("Transaction key cannot be null");
        }

        if (currentTransactionKey != key) {
            throw new TransactionException("Wrong transaction key");
        }

        if (amount == null) {
            throw new TransactionException("Cannot withdraw null");
        }

        synchronized (this) {
            if (shadowMoney.compareTo(amount) < 0) {
                throw new AccountException("Account does not have enough money");
            }

            shadowMoney = shadowMoney.subtract(amount);
        }
    }

    @Override
    public BigDecimal getMoney(Object key) throws TransactionException {
        if (key == null) {
            throw new TransactionException("Transaction key cannot be null");
        }

        if (currentTransactionKey != key) {
            throw new TransactionException("Wrong transaction key");
        }

        synchronized (this) {
            return shadowMoney;
        }
    }

    @Override
    public void join(Object key) throws TransactionException {
        lock.lock();
        if (key == null) {
            throw new TransactionException("Transaction key cannot be null");
        }

        if (currentTransactionKey != null) {
            throw new TransactionException("A transaction is already open");
        }

        synchronized (this) {
            currentTransactionKey = key;
            // Copy the actual value to a shadow copy to work on during the transaction
            shadowMoney = money.add(BigDecimal.ZERO); // creates an actual copy
        }
    }

    @Override
    public boolean canCommit(Object key) throws TransactionException {
        if (key == null) {
            throw new TransactionException("Transaction key cannot be null");
        }

        synchronized (this) {
            return key == currentTransactionKey;
        }
    }

    @Override
    public void commit(Object key) throws TransactionException {
        if (key == null) {
            throw new TransactionException("Transaction key cannot be null");
        }

        if (key != currentTransactionKey) {
            throw new TransactionException("Wrong transaction key");
        }

        synchronized (this) {
            // Do the actual commit by copying the shadow copy back to the actual variable
            money = shadowMoney.add(BigDecimal.ZERO); // creates an actual copy
            // reset the transaction key so we can join a new transaction
            currentTransactionKey = null;
        }

        lock.unlock();
    }

    @Override
    public void abort(Object key) {
        synchronized (this) {
            currentTransactionKey = null;
            shadowMoney = money.add(BigDecimal.ZERO); // creates an actual copy
        }

        lock.unlock();
    }
}
