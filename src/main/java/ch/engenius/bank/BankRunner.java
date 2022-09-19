package ch.engenius.bank;


import ch.engenius.bank.domain.Bank;
import ch.engenius.bank.domain.BankAccount;
import ch.engenius.bank.exceptions.BankMoneyAmountException;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class BankRunner {

    private static final int NUMBER_OF_BANK_ACCOUNTS = 1000;
    private static final int NUMBER_OF_ITERATIONS = 1000;
    private static final BigDecimal DEFAULT_DEPOSIT = BigDecimal.valueOf(100);
    public static final BigDecimal TOTAL_BANK_MONEY_AMOUNT = DEFAULT_DEPOSIT.multiply(BigDecimal.valueOf(NUMBER_OF_BANK_ACCOUNTS));
    private static final ExecutorService executorService = Executors.newFixedThreadPool(8);
    private final Bank bank;

    public BankRunner() {
        HashMap<UUID, BankAccount> accounts = new HashMap<>();
        this.bank = new Bank(accounts);
    }

    public static void main(String[] args) {
        BankRunner runner = new BankRunner();
        runner.registerAccounts();
        runner.bankMoneyAmountCheck();
        runner.runTransactions();
        runner.bankMoneyAmountCheck();
    }

    private void registerAccounts() {
        IntStream.range(0, NUMBER_OF_BANK_ACCOUNTS).forEach(k ->
                bank.registerBankAccount(UUID.randomUUID(), BankRunner.DEFAULT_DEPOSIT)
        );
    }

    private void bankMoneyAmountCheck() {
        BigDecimal currentTotalAmount = bank.getAccounts().values()
                .stream()
                .map(BankAccount::getMoneyAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (TOTAL_BANK_MONEY_AMOUNT.compareTo(currentTotalAmount) != 0) {
            throw new BankMoneyAmountException("Bank money amount check failed.");
        }
        System.out.println("Bank money amount check successful.");
    }

    private void runTransactions() {
        for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
            executorService.submit(this::transferMoneyFromAndToRandomBankAccount);
        }
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            System.out.println("InterruptedException occurred: " + e);
            executorService.shutdownNow();
        }
    }

    private void transferMoneyFromAndToRandomBankAccount() {
        BigDecimal transferMoneyAmount = BigDecimal.valueOf(Math.random() * DEFAULT_DEPOSIT.doubleValue());

        UUID accountInId = bank.getRandomBankAccountId();
        UUID accountOutId = bank.getRandomBankAccountId();
        while (accountInId.equals(accountOutId)) {
            accountOutId = bank.getRandomBankAccountId();
        }

        bank.transferMoney(accountOutId, accountInId, transferMoneyAmount);
    }
}
