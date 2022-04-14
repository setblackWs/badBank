package ch.engenius.bank;

import java.math.BigDecimal;
import java.util.HashMap;

public class Bank {
    private HashMap<Integer, Account> accounts = new HashMap<>();

    /*
     * Creates an account with specified account number and initial amount of credits.
     *
     * @param accountNumber Number of the account
     *
     * @param amount Initial credit for the account
     *
     * @thorws IllegalStateException If account with given number has already been registered
     */
    public Account registerAccount(int accountNumber, BigDecimal amount) {
        if (accounts.containsKey(accountNumber)) {
            throw new IllegalStateException("account already exists");
        }

        Account account = new Account(amount);
        accounts.put(accountNumber, account);
        return account;
    }

    /*
     * Retrieve account by its number
     *
     * @param accountNumber Number of the account to retrieve
     *
     * @throws IllegalStateException If account with given number is not registered
     */
    public Account getAccount(int accountNumber) {
        if (!accounts.containsKey(accountNumber)) {
            throw new IllegalStateException("account does not exist");
        }

        return accounts.get(accountNumber);
    }
}
