package ch.engenius.bank.runner.runner_helper;

import ch.engenius.bank.context.DataContext;
import ch.engenius.bank.service.account.AccountService;
import ch.engenius.bank.service.account.AccountServiceImpl;
import ch.engenius.bank.service.bank.BankService;
import ch.engenius.bank.service.bank.BankServiceImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class BankRunnerAccountHelper {
    private final BankService bankService;
    private final AccountService accountService;

    private static final ExecutorService executor = Executors.newFixedThreadPool(8);

    public BankRunnerAccountHelper(DataContext dataContext) {
        this.bankService = new BankServiceImpl(dataContext);
        this.accountService = new AccountServiceImpl(dataContext);
    }

    public void runBank(int iterations, int maxAccount) throws InterruptedException {
        List<Callable<Void>> tasks = new ArrayList<>();
        for (int i = 0; i < iterations; i++) {
            tasks.add(new BankRunnerTask(accountService, maxAccount));
        }
        executor.invokeAll(tasks);
        executor.shutdown();
    }


    public void registerAccounts(int number, BigDecimal defaultMoney) {
        bankService.registerAccounts(number, defaultMoney);
    }

}
