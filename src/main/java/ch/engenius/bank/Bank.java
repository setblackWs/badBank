package ch.engenius.bank;

import ch.engenius.bank.api.*;

import java.math.BigDecimal;
import java.util.Objects;

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

        if(accounts.get(accountNumber) != null) {
            throw new AccountException(String.format("Account %d already exists", accountNumber));
        }

        Account account = new Account();
        account.setMoney(amount);
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
    public void doTransaction(Integer inAccount, Integer outAccount, BigDecimal amount) throws AccountException, TransactionFailedException, RetryTransactionException {
        Objects.requireNonNull(inAccount, "inAccount must not be null");
        Objects.requireNonNull(outAccount, "outAccount must not be null");
        Objects.requireNonNull(amount, "inAccount must not be null");

        Account src = getAccount(outAccount);
        if (src == null) {
            throw new AccountException(String.format("Account not found: %d", outAccount));
        }

        Account dst = getAccount(inAccount);
        if (dst == null) {
            throw new AccountException(String.format("Account not found: %d", inAccount));
        }

        Object key = new Object();

        try {

            src.join(key);
            dst.join(key);

            src.withdraw(key, amount);
            dst.deposit(key, amount);

            if (src.canCommit(key) && dst.canCommit(key)) {
                try {
                    src.commit(key);
                    dst.commit(key);
                } catch (TransactionException e) {
                    rollback(key, src, dst);
                    throw new TransactionFailedException(outAccount, inAccount, amount);
                }
            } else {
                rollback(key, src, dst);
                // try again later is ok
                throw new RetryTransactionException(outAccount, inAccount, amount);
            }
            System.out.println(String.format("transaction completed: src=%f, dst=%f", src.getMoney(), dst.getMoney()));


        } catch (AccountException e) {
            // this means a semantic error and we cannot try again because we have either not enough money or deposited
            // an invalid amount of money. either way, a rollback is needed
            rollback(key, src, dst);
            throw e;
        } catch (TransactionException e) {
            rollback(key, src, dst);
            // already busy; we can try again later
            throw new RetryTransactionException(outAccount, inAccount, amount);
        }
    }

    private void rollback(Object key, Account src, Account dst) {
        src.abort(key);
        dst.abort(key);
    }
}
