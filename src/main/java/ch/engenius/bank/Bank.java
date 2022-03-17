package ch.engenius.bank;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class Bank {

    private final Map<Integer, Account> accounts = new HashMap<>();

    public void registerAccount(int accountNumber, int amount) {
        Account account = new Account(BigDecimal.valueOf(amount));
        accounts.put(accountNumber, account);
    }

    public Account getAccount(int number) {
        Account account = accounts.get(number);
        if (account == null) {
            throw new NoSuchElementException();
        }
        return account;
    }

}
