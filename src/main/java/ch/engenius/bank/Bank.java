package ch.engenius.bank;

import java.util.HashMap;

public class Bank {
    private HashMap<Integer, Account> accounts = new HashMap<>();

    public Account registerAccount(int accountNumber, double amount) {
        Account account = new Account();
        account.setMoney(amount);
        accounts.put(accountNumber, account);
        return account;
    }

    public Account getAccount(int number) {
        return accounts.get(number);
    }
}
