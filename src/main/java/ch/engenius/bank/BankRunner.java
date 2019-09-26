package ch.engenius.bank;

import ch.engenius.bank.api.AccountException;
import ch.engenius.bank.api.RetryTransactionException;
import ch.engenius.bank.api.Store;
import ch.engenius.bank.api.TransactionFailedException;

import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class BankRunner {

    private static final ExecutorService executor = Executors.newFixedThreadPool(8);

    private final Random random = new Random(43);
    private final Bank bank;

    public BankRunner() {
        Store<Integer, Account> store = new InMemoryStore<>();
        this.bank = new Bank(store);
    }


    public static void main(String[] args) {
        BankRunner runner = new BankRunner();
        int accounts = 100;
        int defaultDeposit = 1000;
        int iterations = 10000;
        runner.registerAccounts(accounts, defaultDeposit);
        runner.sanityCheck(accounts, accounts * defaultDeposit);
        runner.runBank(iterations, accounts);
        runner.sanityCheck(accounts, accounts * defaultDeposit);

    }

    private void runBank(int iterations, int maxAccount) {
        for (int i = 0; i < iterations; i++) {
            executor.submit(() -> runRandomOperation(maxAccount));
        }
        try {
            executor.shutdown();
            executor.awaitTermination(100, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void runRandomOperation(int maxAccount) {
        double transfer = random.nextDouble() * 100.0;
        int accountInNumber = random.nextInt(maxAccount);
        int accountOutNumber = random.nextInt(maxAccount);
        try {
            bank.doTransaction(accountInNumber, accountOutNumber, BigDecimal.valueOf(transfer));
        } catch (RetryTransactionException e) {
            // If we land here we can safely rerun the transaction. I would suggest to spawn a new thread
            // and run the same transaction again but since we're dealing with randomness here i'm not going
            // to refactor that.
            e.printStackTrace();
        } catch (TransactionFailedException e) {
            // If we land here, this means something with the transaction went wrong and it is possible
            // that we have corrupted our data (meaning we inflated or deflated our overall money in the bank)
            // So we should trigger some process to check what went wrong and correct the mistake.
            e.printStackTrace();
        } catch (Exception e) {
            // this means that the transaction did not take place and we can't recover from the error
            e.printStackTrace();
        }
    }

    private void registerAccounts(int number, int defaultMoney) {
        for (int i = 0; i < number; i++) {
            bank.registerAccount(i, BigDecimal.valueOf(defaultMoney));
        }
    }

    private void sanityCheck(int accountMaxNumber, int totalExpectedMoney) {
        BigDecimal sum = IntStream.range(0, accountMaxNumber)
                .mapToObj(bank::getAccount)
                .map(Account::getMoney)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (sum.intValue() != totalExpectedMoney) {
            throw new IllegalStateException("we got " + sum + " != " + totalExpectedMoney + " (expected)");
        }
        System.out.println("sanity check OK");
    }


}
