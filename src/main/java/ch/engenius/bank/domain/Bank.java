package ch.engenius.bank.domain;

import ch.engenius.bank.exception.BankException;
import ch.engenius.bank.model.AccountNumber;
import ch.engenius.bank.model.Money;
import ch.engenius.bank.service.BankService;
import lombok.Synchronized;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Bank implements BankService {
    private final ConcurrentMap<AccountNumber, Account> accounts;

    public Bank() {
        this(new ConcurrentHashMap<>());
    }

    public Bank(ConcurrentMap<AccountNumber, Account> accounts) {
        this.accounts = accounts;
    }

    @Override
    public Account registerAccount(AccountNumber accountNumber, Money amount) {
        if (accounts.containsKey(accountNumber)) {
            throw new BankException("Account already exists");
        }

        Account account = new Account(accountNumber, amount);
        accounts.put(accountNumber, account);
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
        if (accounts.get(accountNumber) == null) {
            throw new BankException("Account not found");
        }
        return accounts.get(accountNumber);
    }
}
