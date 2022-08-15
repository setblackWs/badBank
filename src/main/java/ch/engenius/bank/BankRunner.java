package ch.engenius.bank;

import ch.engenius.bank.domain.Account;
import ch.engenius.bank.domain.Bank;
import ch.engenius.bank.model.AccountNumber;
import ch.engenius.bank.model.Money;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.IntStream;

@Slf4j
public class BankRunner {

    private static final ExecutorService executor = Executors.newFixedThreadPool(8);
    private final Random random = new Random(43);
    private static final int DEFAULT_DEPOSIT = 1000;
    private static final int ITERATIONS = 10000;
    private static final int ACCOUNTS = 100;
    private final Bank bank;

    public BankRunner() {
        ConcurrentMap<Integer, Account> accounts = new ConcurrentHashMap<>();
        this.bank = new Bank(accounts);
    }

    public static void main(String[] args) {
        BankRunner runner = new BankRunner();
        runner.registerAccounts(ACCOUNTS, DEFAULT_DEPOSIT);
        runner.sanityCheck(ACCOUNTS, ACCOUNTS * DEFAULT_DEPOSIT);
        runner.runBank(ITERATIONS, ACCOUNTS);
        runner.sanityCheck(ACCOUNTS, ACCOUNTS * DEFAULT_DEPOSIT);
    }

    private void runBank(int iterations, int maxAccount) {
        for (int i = 0; i < iterations; i++) {
            executor.submit(() -> initiateMoneyTransfer(maxAccount));
        }
        try {
            executor.shutdown();
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            log.error("InterruptedException occurred: ", e);
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private void initiateMoneyTransfer(int maxAccount) {
        Money moneyAmount = new Money(BigDecimal.valueOf(random.nextDouble() * 100.0));
        AccountNumber accountInNumber = new AccountNumber(random.nextInt(maxAccount));
        AccountNumber accountOutNumber = new AccountNumber(random.nextInt(maxAccount));

        bank.transferMoney(accountOutNumber, accountInNumber, moneyAmount);
    }

    private void registerAccounts(int numberOfAccounts, int defaultMoney) {
        for (int accountNumber = 0; accountNumber < numberOfAccounts; accountNumber++) {
            bank.registerAccount(new AccountNumber(accountNumber), new Money(BigDecimal.valueOf(defaultMoney)));
        }
    }

    private void sanityCheck(int accountMaxNumber, int totalExpectedMoney) {
        BigDecimal sum = IntStream.range(0, accountMaxNumber)
                .mapToObj(a -> bank.getAccount(new AccountNumber(a)))
                .map(Account::getMoney)
                .reduce(new Money(BigDecimal.ZERO), Money::add).getAmount();

        if (sum.intValue() != totalExpectedMoney) {
            throw new IllegalStateException("we got " + sum + " != " + totalExpectedMoney + " (expected)");
        }

        log.info("Sanity check: OK");
    }

}
