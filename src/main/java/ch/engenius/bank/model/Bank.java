package ch.engenius.bank.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Bank {
    private List<Account> accounts;

    public Bank(){
        this.accounts = new ArrayList<>();
    }

    public boolean addAccount(Account newAccount) {
        return accounts.add(newAccount);
    }

}
