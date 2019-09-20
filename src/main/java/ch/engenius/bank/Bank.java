package ch.engenius.bank;

import ch.engenius.bank.api.BankService;
import ch.engenius.bank.api.Store;
import ch.engenius.bank.model.Account;

public class Bank implements BankService {
    private Store<Integer, Account> accounts;

    // If we would have a DI container we would mark this as @Inject
    public Bank(Store<Integer, Account> accounts) {
        this.accounts = accounts;
    }

    @Override
    public Account registerAccount(int accountNumber, double amount) {
        Account account = new Account();
        account.setMoney(amount);
        accounts.create(accountNumber, account);
        return account;
    }

    @Override
    public Account getAccount(int number) {
        return accounts.get(number);
    }
}
