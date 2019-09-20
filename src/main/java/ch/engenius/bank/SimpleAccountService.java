package ch.engenius.bank;

import ch.engenius.bank.api.AccountService;
import ch.engenius.bank.api.Store;
import ch.engenius.bank.model.Account;

public class SimpleAccountService implements AccountService {

    private Store<Integer, Account> accounts;

    public SimpleAccountService(Store<Integer, Account> accounts) {
        this.accounts = accounts;
    }

    @Override
    public void withdraw(int number, double amount) {
        Account account = accounts.get(number);

        if(account == null) {
            throw new IllegalArgumentException("No account found");
        }

        if (account.getMoney() - amount < 0) {
            throw new IllegalStateException("not enough credits on account");
        }

        account.setMoney(account.getMoney() - amount);

    }

    @Override
    public void deposit(int number, double amount) {
        if(amount < 0) {
            throw new IllegalArgumentException("negative amounts cannot be deposited");
        }

        Account account = accounts.get(number);

        if(account == null) {
            throw new IllegalArgumentException("No account found");
        }

        account.setMoney(account.getMoney() + amount);
    }

}
