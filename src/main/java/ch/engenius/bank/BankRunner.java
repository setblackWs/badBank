package ch.engenius.bank;

import ch.engenius.bank.config.BankRunnerConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
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
        for (int i = 0; i < BankRunnerConstants.ITERATIONS; i++) {
            executor.submit(this::runRandomOperation);
        }
        try {
            executor.shutdown();
            boolean terminationStatus = executor.awaitTermination(100, TimeUnit.SECONDS);

            if (!terminationStatus) {
                logger.error("Executor did not terminate within the specified timeout.");
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.error("InterruptedException occurred: {}", e.getMessage());
        }
    }

    private void runRandomOperation()  {
        BigDecimal transferAmount = BigDecimal.valueOf(random.nextDouble() * 100.0);
        int toAccountNumber = random.nextInt(BankRunnerConstants.ACCOUNTS_NUMBER);
        int fromAccountNumber = random.nextInt(BankRunnerConstants.ACCOUNTS_NUMBER);
        Account toAccount = bank.getAccount(toAccountNumber);
        Account fromAccount = bank.getAccount(fromAccountNumber);

        logger.trace("Before transaction - From Account {}: {}", fromAccountNumber, fromAccount.getMoney());
        logger.trace("Before transaction - To Account {}: {}", toAccountNumber, toAccount.getMoney());

        try {
            bank.transfer(fromAccount, toAccount, transferAmount);
        } catch  (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Thread interrupted while running random operation", e);
        }

        logger.trace("After transaction - From Account {}: {}", fromAccountNumber, fromAccount.getMoney());
        logger.trace("After transaction - To Account {}: {}", toAccountNumber, toAccount.getMoney());
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
                .map(Account::getMoney)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (sum.compareTo(BankRunnerConstants.TOTAL_EXPECTED_AMOUNT) != 0) {
            throw new IllegalStateException("we got " + sum + " != " + BankRunnerConstants.TOTAL_EXPECTED_AMOUNT + " (expected)");
        }
        logger.info("sanity check OK");
    }

}
