package ch.engenius.bank;

import ch.engenius.bank.config.BankRunnerConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class BankRunner {
    final Bank bank = new Bank();
    private static final ExecutorService executor = Executors.newFixedThreadPool(BankRunnerConstants.THREAD_POOL_NUMBER);
    private static final Logger logger = LogManager.getLogger(BankRunner.class);
    private final ThreadLocalRandom random = ThreadLocalRandom.current();

    public static void main(String[] args) throws InterruptedException {
        BankRunner runner = new BankRunner();
        runner.registerAccounts();
        runner.sanityCheck();
        runner.runBank();
        runner.sanityCheck();
    }

    private void runBank() {
        List<Callable<Void>> randomOperationTasks = new ArrayList<>();
        for (int i = 0; i < BankRunnerConstants.ITERATIONS; i++) {
            randomOperationTasks.add(() -> {
                runRandomOperation();
                return null;
            });
        }
        try {
            executor.invokeAll(randomOperationTasks);
            executor.shutdown();
            if (!executor.awaitTermination(100, TimeUnit.SECONDS)) {
                logger.error("Executor did not terminate within the specified timeout.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Execution was interrupted", e);
        } finally {
            if (!executor.isShutdown()) {
                executor.shutdownNow();
            }
        }
    }

    private void runRandomOperation() {
        BigDecimal transferAmount = BigDecimal.valueOf(random.nextDouble() * 100.0);
        int toAccountNumber = random.nextInt(BankRunnerConstants.ACCOUNTS_NUMBER);
        int fromAccountNumber = random.nextInt(BankRunnerConstants.ACCOUNTS_NUMBER);
        Account toAccount = bank.getAccount(toAccountNumber);
        Account fromAccount = bank.getAccount(fromAccountNumber);

        logger.trace("Before transaction - From Account {}: {}", fromAccountNumber, fromAccount.getBalance());
        logger.trace("Before transaction - To Account {}: {}", toAccountNumber, toAccount.getBalance());

        try {
            bank.transfer(fromAccount, toAccount, transferAmount);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Thread interrupted while running random operation", e);
        }

        logger.trace("After transaction - From Account {}: {}", fromAccountNumber, fromAccount.getBalance());
        logger.trace("After transaction - To Account {}: {}", toAccountNumber, toAccount.getBalance());
    }

    private void registerAccounts() throws InterruptedException {
        for (int i = 0; i < BankRunnerConstants.ACCOUNTS_NUMBER; i++) {
            bank.registerAccount(i, BankRunnerConstants.DEPOSIT);
            logger.trace("Account {} registered with initial DEPOSIT: {}", i, BankRunnerConstants.DEPOSIT);
        }
    }

    private void sanityCheck() {
        BigDecimal sum = IntStream.range(0, BankRunnerConstants.ACCOUNTS_NUMBER)
                .mapToObj(bank::getAccount)
                .map(Account::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (sum.compareTo(BankRunnerConstants.TOTAL_EXPECTED_AMOUNT) != 0) {
            throw new IllegalStateException("we got " + sum + " != " + BankRunnerConstants.TOTAL_EXPECTED_AMOUNT + " (expected)");
        }
        logger.info("sanity check OK");
    }

}
