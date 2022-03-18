package ch.engenius.bank.service;

import ch.engenius.bank.domain.Account;
import ch.engenius.bank.domain.Bank;
import java.math.BigDecimal;
import java.util.NoSuchElementException;

public class BankService {

    private Bank bank;

    public BankService() {
        this.bank = new Bank();
    }

    public Account registerAccount(int accountNumber, BigDecimal amount) {
        Account account = new Account(amount);
        bank.getAccounts().put(accountNumber, account);
        return account;
    }

    public Account getAccount(int number) {
        if (!bank.getAccounts().containsKey(number))
            throw new NoSuchElementException("Account with number: " + number + " does not exist");

        return bank.getAccounts().get(number);
    }

    public void transferMoney(BigDecimal money, int accountInNumber, int accountOutNumber) {
        try {
            Account accountIn = getAccount(accountInNumber);
            Account accountOut = getAccount(accountOutNumber);

            accountOut.withdraw(money);
            accountIn.deposit(money);
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
    }
}
