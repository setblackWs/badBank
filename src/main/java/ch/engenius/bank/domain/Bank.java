package ch.engenius.bank.domain;

import ch.engenius.bank.exception.BankException;
import ch.engenius.bank.model.AccountNumber;
import ch.engenius.bank.model.Money;
import ch.engenius.bank.service.BankService;
import lombok.Synchronized;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Bank implements BankService {
    private final ConcurrentMap<Integer, Account> accounts;

    public Bank() {
        this(new ConcurrentHashMap<>());
    }

    public Bank(ConcurrentMap<Integer, Account> accounts) {
        this.accounts = accounts;
    }

    @Override
    public Account registerAccount(AccountNumber accountNumber, Money amount) {

        if (accounts.containsKey(accountNumber.getNumber())) {
            throw new BankException("Account already exists");
        }

        Account account = new Account(amount);
        accounts.put(accountNumber.getNumber(), account);
        return account;
    }

    @Synchronized
    public void transferMoney(AccountNumber accountOutNumber, AccountNumber accountInNumber, Money moneyAmount) {

        Account payerAccount = getAccount(accountOutNumber);
        Account payeeAccount = getAccount(accountInNumber);

        payerAccount.withdraw(moneyAmount);
        payeeAccount.deposit(moneyAmount);
    }

    @Override
    public Account getAccount(AccountNumber accountNumber) {
        return accounts.get(accountNumber.getNumber());
    }
}
