package ch.engenius.bank;

import ch.engenius.bank.api.*;

import java.math.BigDecimal;
import java.util.Optional;

public class Bank implements BankService {
    private Store<Integer, Account> accounts;

    // If we would have a DI container we would mark this as @Inject
    public Bank(Store<Integer, Account> accounts) {
        this.accounts = accounts;
    }

    @Override
    public Account registerAccount(Integer accountNumber, BigDecimal amount) throws AccountException {
        if (accountNumber == null) {
            throw new AccountException("Cannot create account with number null");
        }

        // Here I assume that the Store implementation is thread safe
        if (accounts.get(accountNumber) != null) {
            throw new AccountException(String.format("Account %d already exists", accountNumber));
        }

        // I think we can safely assume that the initial balance is zero of amount is null
        Account account = new Account(Optional.ofNullable(amount).orElse(BigDecimal.ZERO));
        accounts.create(accountNumber, account);
        return account;
    }

    @Override
    public Account getAccount(Integer number) {
        if (number == null) {
            return null;
        }

        return accounts.get(number);
    }

    @Override
    public synchronized void doTransaction(Integer srcKey, Integer dstKey, BigDecimal amount) throws AccountException, TransactionFailedException, RetryTransactionException {
        // We could use Objects.requireNonNull() here but because of the semantics of the
        // transaction I chose to throw an AccountException instead of a NullpointerException.
        //
        if (srcKey == null) throw new AccountException("srcKey must not be null");
        if (dstKey == null) throw new AccountException("dstKey must not be null");
        // I throw AccountException here because it's a semantic problem and thus the transaction should not be retried
        if (amount == null) throw new AccountException("amount must not be null");

        Account src = getAccount(srcKey);
        if (src == null) {
            throw new AccountException(String.format("Account not found: %d", srcKey));
        }

        Account dst = getAccount(dstKey);
        if (dst == null) {
            throw new AccountException(String.format("Account not found: %d", dstKey));
        }

        Object txKey = new Object();

        try {

            src.join(txKey);
            dst.join(txKey);

            src.withdraw(txKey, amount);
            dst.deposit(txKey, amount);

            if (src.canCommit(txKey) && dst.canCommit(txKey)) {
                try {
                    src.commit(txKey);
                    dst.commit(txKey);
                } catch (TransactionException e) {
                    rollback(txKey, src, dst);
                    throw new TransactionFailedException(srcKey, dstKey, amount);
                }
            } else {
                rollback(txKey, src, dst);
                // try again later is ok
                throw new RetryTransactionException(srcKey, dstKey, amount);
            }


        } catch (AccountException e) {
            // this means a semantic error and we cannot try again because we have either not enough money or deposited
            // an invalid amount of money. either way, a rollback is needed
            rollback(txKey, src, dst);
            throw e;
        } catch (TransactionException e) {
            rollback(txKey, src, dst);
            // already busy; we can try again later
            throw new RetryTransactionException(srcKey, dstKey, amount);
        }
    }

    private void rollback(Object key, Account src, Account dst) {
        src.abort(key);
        dst.abort(key);
    }
}
