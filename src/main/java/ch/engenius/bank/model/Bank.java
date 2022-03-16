package ch.engenius.bank.model;

import ch.engenius.bank.exception.AccountNotFoundException;
import lombok.Getter;

import java.util.HashMap;

@Getter
public class Bank {
    private HashMap<Integer, Account> accounts;

    public Bank() {
        accounts = new HashMap<>();
    }

    public Account getAccount( int number) throws AccountNotFoundException {
        Account account = accounts.get(number);
        if(account == null)
            throw new AccountNotFoundException(number);
        return account;
    }

    public HashMap<Integer, Account> getAllAccounts() {
        return accounts;
    }
}
