package ch.engenius.bank.repository;

import ch.engenius.bank.context.DataContext;
import ch.engenius.bank.model.Account;

public class BankRepository {
    private DataContext dataContext;

    public BankRepository(DataContext dataContext) {
        this.dataContext = dataContext;
    }

    public Account saveAccount(int accountNumber, Account account) {
        Account savedAccount = dataContext.saveAccount(accountNumber, account);
        return savedAccount;
    }

}
