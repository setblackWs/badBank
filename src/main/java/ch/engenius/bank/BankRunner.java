package ch.engenius.bank;

import ch.engenius.bank.domain.Account;
import ch.engenius.bank.service.BankService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class BankRunner {

    private static final ExecutorService executor = Executors.newFixedThreadPool(8);

    private final Random random = new Random(43);
    private final BankService bankService = new BankService();


    public static void main(String[] args) {
        BankRunner runner = new BankRunner();
        int accounts = 100;
        BigDecimal defaultDeposit = BigDecimal.valueOf(1000);
        int iterations  = 10000;
        try {
            runner.registerAccounts(accounts, defaultDeposit);
            runner.sanityCheck(accounts, defaultDeposit.multiply(BigDecimal.valueOf(accounts)));
            runner.runBank(iterations, accounts);
            runner.sanityCheck(accounts, defaultDeposit.multiply(BigDecimal.valueOf(accounts)));
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    private void runBank(int iterations, int maxAccount) {
        List<Callable<String>> callableTasks = new ArrayList<>();

        for (int i = 0; i < iterations; i++) {
            Callable<String> callableTask = () -> {
                runRandomOperation(maxAccount);
                return "";
            };
            callableTasks.add(callableTask);
        }

        try {
            executor.invokeAll(callableTasks);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executor.shutdown();
    }

    private void runRandomOperation(int maxAccount) {
        BigDecimal transfer = BigDecimal.valueOf(random.nextDouble() * 100.0);
        int accountInNumber = random.nextInt(maxAccount);
        int accountOutNumber = random.nextInt(maxAccount);
        bankService.transferMoney(transfer, accountInNumber, accountOutNumber);
    }

    private void  registerAccounts(int number, BigDecimal defaultMoney) {
        for (int i = 0; i < number; i++) {
            bankService.registerAccount(i, defaultMoney);
        }
    }

    private void sanityCheck(int accountMaxNumber, BigDecimal totalExpectedMoney) {
        BigDecimal sum = IntStream.range(0, accountMaxNumber)
                .mapToObj(bankService::getAccount)
                .map (Account::getMoney)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (sum.compareTo(totalExpectedMoney) != 0) {
            throw new IllegalStateException("We got "+ sum + " != " + totalExpectedMoney +" (expected)");
        }
        System.out.println("Sanity check OK");
    }
}
