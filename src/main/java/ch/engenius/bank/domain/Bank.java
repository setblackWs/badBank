package ch.engenius.bank.domain;

import java.util.HashMap;

public class Bank {

    private HashMap<Integer, Account> accounts;

    public Bank() {
        this.accounts = new HashMap<>();
    }

    public HashMap<Integer, Account> getAccounts() {
        return accounts;
    }
}
