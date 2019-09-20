package ch.engenius.bank;

import ch.engenius.bank.api.AccountService;
import ch.engenius.bank.api.BankService;
import ch.engenius.bank.api.Store;
import ch.engenius.bank.api.TransactionException;
import ch.engenius.bank.model.Account;

public class Bank implements BankService {
    private Store<Integer, Account> accounts;
    private AccountService accountService;

    // If we would have a DI container we would mark this as @Inject
    public Bank(Store<Integer, Account> accounts, AccountService accountService) {
        this.accounts = accounts;
        this.accountService = accountService;
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

    @Override
    public void doTransaction(int inAccount, int outAccount, double amount) {

        try {
            accountService.withdraw(outAccount, amount);
        } catch (TransactionException e) {
            // we cannot complete the transaction
            return;
        }
        try {
            accountService.deposit(inAccount, amount);
        } catch (TransactionException e) {
            // rollback
            try {
                accountService.deposit(outAccount, amount);
            } catch (TransactionException e1) {
                System.out.println("rollback failed, there is no specified behavior for this scenario");
            }
        }
    }
}
