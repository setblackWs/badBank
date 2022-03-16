package ch.engenius.bank.context;

import ch.engenius.bank.exception.AccountNotFoundException;
import ch.engenius.bank.model.Account;
import ch.engenius.bank.model.Bank;
import lombok.Getter;

import java.util.HashMap;

@Getter
public class DataContext {
    private final Bank bank;


    public DataContext() {
        bank = new Bank();
    }

    public Account getAccount(int number) throws AccountNotFoundException {
        return bank.getAccount(number);
    }

    public HashMap<Integer, Account> getAllAccounts() {
        return bank.getAllAccounts();
    }

    public Account saveAccount(int number, Account account) {
        bank.getAccounts().put(number, account);
        return account;
    }
}
