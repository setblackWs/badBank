package ch.engenius.bank.repository;

import ch.engenius.bank.context.DataContext;
import ch.engenius.bank.exception.AccountNotFoundException;
import ch.engenius.bank.model.Account;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class AccountRepository {
    private DataContext dataContext;

    public AccountRepository(DataContext dataContext) {
        this.dataContext = dataContext;
    }

    public Account getAccount(int accountNumber) throws AccountNotFoundException {
        return dataContext.getAccount(accountNumber);
    }

    public HashMap<Integer, Account> getAllAccounts() {
        return dataContext.getAllAccounts();
    }
}
