package ch.engenius.bank.service.bank;

import ch.engenius.bank.context.DataContext;
import ch.engenius.bank.model.Account;
import ch.engenius.bank.repository.bank.BankRepository;
import ch.engenius.bank.repository.bank.BankRepositoryImpl;

import java.math.BigDecimal;

public class BankServiceImpl implements BankService {
    private final BankRepository bankRepository;


    public BankServiceImpl(DataContext dataContext) {
        bankRepository = new BankRepositoryImpl(dataContext);
    }

    public void registerAccounts(int number, BigDecimal defaultMoney) {
        for ( int i = 0; i < number; i++) {
            registerAccount(i, defaultMoney);
        }
    }

    private Account registerAccount(int accountNumber, BigDecimal amount) {
        Account account = new Account(amount);
        saveAccount(accountNumber, account);
        return account;
    }

    private void saveAccount(int accountNumber, Account account) {
        bankRepository.saveAccount(accountNumber, account);
    }

}
