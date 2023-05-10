package ch.engenius.bank;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class Bank {
    private final ConcurrentHashMap<Integer, Account> accounts = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, ReentrantLock> locks = new ConcurrentHashMap<>();

    /**
     * Registers a new account with the bank.
     *
     * @param accountNumber the account number to register.
     * @param amount the initial deposit amount.
     * @return the newly created account.
     * @throws IllegalArgumentException if the account number is already registered or if the initial deposit amount is negative.
     */
    public Account registerAccount(int accountNumber, BigDecimal amount) {
        if (accounts.containsKey(accountNumber)) {
            throw new IllegalArgumentException("Account number " + accountNumber + " is already registered.");
        }

        Account account = new Account();
        account.deposit(amount);
        accounts.put(accountNumber, account);
        return account;
    }

    /**
     * Retrieves an account by its number.
     *
     * @param number the account number to retrieve.
     * @return the account with the given number.
     * @throws IllegalArgumentException if no account with the given number exists.
     */
    public Account getAccount(int number) {
        Account account = accounts.get(number);
        if (account == null) {
            throw new IllegalArgumentException("Account number " + number + " does not exist.");
        }

        return account;
    }

    /**
     * Transfers an amount of money from one account to another.
     *
     * @param fromAccount the number of the account to withdraw money from.
     * @param toAccount the number of the account to deposit money into.
     * @param amount the amount of money to transfer.
     * @throws IllegalArgumentException if the source and destination accounts are the same, or if either account does not exist,
     *         or if the source account has insufficient funds.
     */
    public void transfer(int fromAccount, int toAccount, BigDecimal amount) {
        if (fromAccount == toAccount) {
            throw new IllegalArgumentException("The source and destination accounts cannot be the same.");
        }

        Account accOut = getAccount(fromAccount);
        Account accIn = getAccount(toAccount);

        accOut.withdraw(amount);
        accIn.deposit(amount);
    }

}
