package ch.engenius.bank.repository.bank;

import ch.engenius.bank.context.DataContext;
import ch.engenius.bank.model.Account;

public class BankRepositoryImpl implements BankRepository {
    private DataContext dataContext;

    public BankRepositoryImpl(DataContext dataContext) {
        this.dataContext = dataContext;
    }

    public Account saveAccount(int accountNumber, Account account) {
        Account savedAccount = dataContext.saveAccount(accountNumber, account);
        return savedAccount;
    }

}
