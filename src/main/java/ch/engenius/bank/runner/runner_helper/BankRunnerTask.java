package ch.engenius.bank.runner.runner_helper;

import ch.engenius.bank.exception.AccountNotFoundException;
import ch.engenius.bank.service.account.AccountService;

import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.Callable;


public class BankRunnerTask implements Callable<Void> {
    private final AccountService accountService;

    private final Random random;
    private int maxAccount;


    public BankRunnerTask(
            AccountService accountService,
            int maxAccount) {
        this.random = new Random(43);
        this.accountService = accountService;
        this.maxAccount = maxAccount;
    }

    @Override
    public Void call() throws Exception {
        tryRunningRandomOperation(this.maxAccount);
        return null;
    }

    private void tryRunningRandomOperation(int maxAccount) {
        try {
            runRandomOperation(maxAccount);
        } catch (InterruptedException | AccountNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void runRandomOperation(int maxAccount) throws InterruptedException, AccountNotFoundException {
        BigDecimal transferAmount = BigDecimal.valueOf(random.nextDouble()*100.0);
        int accountInNumber = random.nextInt(maxAccount);
        int accountOutNumber = random.nextInt(maxAccount);
        makeTransaction(accountInNumber, accountOutNumber, transferAmount);
    }

    private void makeTransaction(int accountInNumber, int accountOutNumber, BigDecimal transferAmount) throws InterruptedException, AccountNotFoundException {
        accountService.makeTransaction(accountInNumber, accountOutNumber, transferAmount);
    }
}
