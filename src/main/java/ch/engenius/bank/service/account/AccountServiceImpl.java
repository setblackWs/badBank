package ch.engenius.bank.service.account;

import ch.engenius.bank.context.DataContext;
import ch.engenius.bank.exception.AccountNotFoundException;
import ch.engenius.bank.model.Account;
import ch.engenius.bank.repository.AccountRepository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    private ReentrantLock lock;

    public AccountServiceImpl(DataContext dataContext) {
        accountRepository = new AccountRepository(dataContext);
        lock = new ReentrantLock();
    }

    public void makeTransaction(int accountIn, int accountOut, BigDecimal amount) throws AccountNotFoundException {
        lock.lock();
        try {
            withdraw(accountOut, amount);
            deposit(accountIn, amount);
        } finally {
          lock.unlock();
        }
    }

    private void withdraw(int accountNumber, BigDecimal withdrawAmount) throws AccountNotFoundException {
        Account account = getAccount(accountNumber);
        checkIfHasEnoughMoney(accountNumber, withdrawAmount);

        BigDecimal newAmount = account.getMoney().subtract(withdrawAmount);
        setMoney(accountNumber, newAmount);
    }

    private void deposit(int accountNumber, BigDecimal depositAmount) throws AccountNotFoundException {
        Account account = getAccount(accountNumber);
        BigDecimal newAmount = account.getMoney().add(depositAmount);
        setMoney(accountNumber, newAmount);
    }

    public Account getAccount(int number) throws AccountNotFoundException {
        return accountRepository.getAccount(number);
    }

    public HashMap<Integer, Account> getAllAccounts() {
        return accountRepository.getAllAccounts();
    }

    public void setMoney(int accountNumber, BigDecimal amount) throws AccountNotFoundException {
        Account account = getAccount(accountNumber);
        account.setMoney(amount);
    }

    private boolean hasEnoughMoney(int accountNumber, BigDecimal withdrawAmount) throws AccountNotFoundException {
        Account account = getAccount(accountNumber);
        BigDecimal difference = account.getMoney().subtract(withdrawAmount);
        return difference.compareTo(BigDecimal.ZERO) > 0;
    }

    private void checkIfHasEnoughMoney(int accountNumber, BigDecimal withdrawAmount) throws AccountNotFoundException {
        if (!hasEnoughMoney(accountNumber, withdrawAmount))
            throw new IllegalStateException("not enough credits on account");
    }
}
