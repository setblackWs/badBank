package ch.engenius.bank;

import java.math.BigDecimal;
import java.util.HashMap;

public class Bank {
    private HashMap<Integer, Account> accounts = new HashMap<>();

    public Account registerAccount(int accountNumber, BigDecimal amount) {
        if (accounts.containsKey(accountNumber)) {
            throw new IllegalStateException("account already exists");
        }

        Account account = new Account(amount);
        accounts.put(accountNumber, account);
        return account;
    }

    public Account getAccount(int accountNumber) {
        if (!accounts.containsKey(accountNumber)) {
            throw new IllegalStateException("account does not exist");
        }

        return accounts.get(accountNumber);
    }
}
