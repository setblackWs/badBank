package ch.engenius.bank.runner;

import ch.engenius.bank.context.DataContext;
import ch.engenius.bank.exception.AccountNotFoundException;
import ch.engenius.bank.runner.runner_helper.BankRunnerAccountHelper;
import ch.engenius.bank.runner.runner_helper.BankRunnerSanityChecker;

import java.math.BigDecimal;

public class BankRunner {
    private final BankRunnerAccountHelper bankRunnerAccountHelper;
    private final BankRunnerSanityChecker sanityChecker;

    private final DataContext dataContext;

    private final int ACCOUNTS_NUM = 100;
    private final int ITERATIONS = 10000;
    private final int DEFAULT_DEPOSIT = 1000;

    public BankRunner() {
        dataContext = new DataContext();
        bankRunnerAccountHelper = new BankRunnerAccountHelper(dataContext);
        sanityChecker = new BankRunnerSanityChecker(dataContext);
    }

    public void run() throws AccountNotFoundException, InterruptedException {
        BigDecimal defaultDeposit  = BigDecimal.valueOf(DEFAULT_DEPOSIT);
        BigDecimal totalDeposit = defaultDeposit.multiply(BigDecimal.valueOf(ACCOUNTS_NUM));

        registerAccounts(defaultDeposit);
        sanityCheck(totalDeposit);
        runBank();
        sanityCheck(totalDeposit);
    }

    private void runBank() throws InterruptedException {
        bankRunnerAccountHelper.runBank(ITERATIONS, ACCOUNTS_NUM);
    }

    private void registerAccounts(BigDecimal defaultAccountMoney) {
        bankRunnerAccountHelper.registerAccounts(ACCOUNTS_NUM, defaultAccountMoney);
    }

    private void sanityCheck(BigDecimal totalExpectedSum) throws AccountNotFoundException {
        sanityChecker.sanityCheck(ACCOUNTS_NUM, totalExpectedSum);
    }

}
