package ch.engenius.bank;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;

public class Bank {
    private final ConcurrentHashMap<Integer, Account> accounts = new ConcurrentHashMap<>();

    public void registerAccount(int accountNumber, BigDecimal amount) throws InterruptedException {
        Account account = new Account();
        account.deposit(amount);
        accounts.put(accountNumber, account);
    }

    public Account getAccount(int number) {
        return accounts.get(number);
    }

    public void transfer(Account fromAccount, Account toAccount, BigDecimal amount) throws InterruptedException {
        boolean isWithdrawn = fromAccount.withdraw(amount);
        if (isWithdrawn) {
            boolean isDeposited = false;
            try {
                isDeposited = toAccount.deposit(amount);
            } finally {
                if (!isDeposited) {
                    fromAccount.deposit(amount);
                }
            }
        }
    }
}
