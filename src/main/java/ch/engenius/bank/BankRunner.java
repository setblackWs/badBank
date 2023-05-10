package ch.engenius.bank;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.stream.IntStream;
import java.util.logging.Logger;

public class BankRunner {

    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(8);
    private static final Logger LOGGER = Logger.getLogger(BankRunner.class.getName());
    private static final int NUM_ACCOUNTS = 100;
    private static final BigDecimal INITIAL_DEPOSIT = BigDecimal.valueOf(1000);
    private static final int NUM_ITERATIONS = 10000;

    private final Random random = new Random(43);
    private final Bank bank = new Bank();


    public static void main(String[] args) {
        BankRunner runner = new BankRunner();

        runner.registerAccounts(NUM_ACCOUNTS, INITIAL_DEPOSIT);
        runner.verifyTotalBalance(NUM_ACCOUNTS, INITIAL_DEPOSIT.multiply(BigDecimal.valueOf(NUM_ACCOUNTS)));
        runner.runBank(NUM_ITERATIONS, NUM_ACCOUNTS);
        runner.verifyTotalBalance(NUM_ACCOUNTS, INITIAL_DEPOSIT.multiply(BigDecimal.valueOf(NUM_ACCOUNTS)));
    }

    /**
     * Executes a number of iterations of random transfers between accounts in the bank.
     * @param iterations The number of iterations to run
     * @param maxAccount The maximum number of accounts in the bank
     */
    private void runBank(int iterations, int maxAccount) {
        List<Callable<Void>> transferTasks = new ArrayList<>();
        for (int i = 0; i < iterations; i++) {
            transferTasks.add(() -> {
                performRandomTransfer(maxAccount);
                return null;
            });
        }
        try {
            EXECUTOR.invokeAll(transferTasks);
            EXECUTOR.shutdown();
        } catch (InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Execution was interrupted", e);
            Thread.currentThread().interrupt();
        } finally {
            EXECUTOR.shutdownNow();
        }
    }

    /**
     * Performs a random transfer between two random accounts in the bank.
     * @param maxAccount The maximum number of accounts in the bank
     */
    private void performRandomTransfer(int maxAccount) {
        BigDecimal transferAmount = BigDecimal.valueOf(random.nextDouble()).multiply(BigDecimal.valueOf(100));
        int accountInNumber = random.nextInt(maxAccount);
        int accountOutNumber = random.nextInt(maxAccount);
        bank.transfer(accountOutNumber, accountInNumber, transferAmount);
    }

    /**
     * Registers a specified number of accounts in the bank, each with a default amount of money.
     * @param number The number of accounts to register
     * @param defaultMoney The default amount of money for each account
     */
    private void  registerAccounts(int number, BigDecimal defaultMoney) {
        for (int i = 0; i < number; i++) {
            bank.registerAccount(i, defaultMoney);
        }
    }

    /**
     * Verifies that the total balance of all accounts in the bank matches an expected value.
     * @param accountMaxNumber The maximum number of accounts in the bank
     * @param totalExpectedMoney The expected total balance of all accounts
     */
    private void verifyTotalBalance(int accountMaxNumber, BigDecimal totalExpectedMoney) {
        BigDecimal sum = IntStream.range(0, accountMaxNumber)
                .mapToObj(bank::getAccount)
                .map(Account::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (sum.compareTo(totalExpectedMoney) != 0) {
            throw new IllegalStateException("we got "+ sum + " != " + totalExpectedMoney +" (expected)");
        }
        LOGGER.info("Total balance is OK.");
    }
}
