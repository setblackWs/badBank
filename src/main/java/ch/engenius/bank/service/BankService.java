package ch.engenius.bank.service;

import ch.engenius.bank.exception.AccountExistsException;
import ch.engenius.bank.exception.AccountNotFoundException;
import ch.engenius.bank.exception.IllegalAccountAmountException;
import ch.engenius.bank.model.Account;
import ch.engenius.bank.model.Bank;

import java.math.BigDecimal;
import java.util.concurrent.locks.ReentrantLock;

public class BankService {

    private Bank bank;
    private AccountService accountService;

    public BankService(Bank bank, AccountService accountService) {
        this.bank = bank;
        this.accountService = accountService;
    }

    public void transferMoney(int accOutNumber, int accInNumber, BigDecimal amount) throws AccountNotFoundException, IllegalAccountAmountException {
        Account accIn = getAccount(accInNumber);   //I have decided to not go for a synchronization here in this case, instead I choose to put it in Account service.
        Account accOut = getAccount(accOutNumber); //If somebody implements another service that need to use our account withdraw and deposit methods, developer
        accountService.withdraw(accOut, amount);   //of that service must also deal with synchronization. In this way we are avoiding boilerplate code
        accountService.deposit(accIn, amount);     //and also preventing potential new bugs
    }

    public Account getAccount(int accountNumber) throws AccountNotFoundException {
        return bank.getAccounts().stream()
                .filter(acc -> acc.getAccountNumber() == accountNumber)
                .findFirst()
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));
    }

    public void registerAccount(int accountNumber, BigDecimal amount) throws AccountExistsException {
        validateAccountExistence(accountNumber);
        validateAmount(amount);
        Account account = Account.builder()
                .accountNumber(accountNumber)
                .money(amount)
                .lock(new ReentrantLock())
                .build();
        bank.addAccount(account);
    }

    private void validateAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalAccountAmountException(amount);
        }
    }

    private void validateAccountExistence(int accountNumber) {
        if (accountExist(accountNumber)) {
            throw new AccountExistsException(accountNumber);
        }
    }

    private boolean accountExist(int accountNumber) {
        return bank.getAccounts().stream()
                .anyMatch(acc -> acc.getAccountNumber() == accountNumber);
    }
}
