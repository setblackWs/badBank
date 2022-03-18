package ch.engenius.bank.repository.account;

import ch.engenius.bank.context.DataContext;
import ch.engenius.bank.exception.AccountNotFoundException;
import ch.engenius.bank.model.Account;

import java.util.HashMap;

public class AccountRepositoryImpl implements AccountRepository {
    private DataContext dataContext;

    public AccountRepositoryImpl(DataContext dataContext) {
        this.dataContext = dataContext;
    }

    public Account getAccount(int accountNumber) throws AccountNotFoundException {
        return dataContext.getAccount(accountNumber);
    }

    public HashMap<Integer, Account> getAllAccounts() {
        return dataContext.getAllAccounts();
    }
}
