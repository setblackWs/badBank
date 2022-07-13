package ch.engenius.bank;

import ch.engenius.bank.exception.AccountExistsException;
import ch.engenius.bank.exception.AccountNotFoundException;
import ch.engenius.bank.exception.IllegalAccountAmountException;
import ch.engenius.bank.model.Account;
import ch.engenius.bank.model.Bank;
import ch.engenius.bank.service.AccountService;
import ch.engenius.bank.service.BankService;

import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class BankRunner {

    private static final ExecutorService executor = Executors.newFixedThreadPool(8);

    private final Random random = new Random(43);
    private Bank bank = new Bank();
    private AccountService accountService = new AccountService();
    private final BankService bankService = new BankService(bank, accountService);


    public static void main(String[] args) {
        BankRunner runner = new BankRunner();
        int accountsNumber = 100;
        BigDecimal defaultDeposit = BigDecimal.valueOf(1000);
        int iterations = 100000;
        runner.registerAccounts(accountsNumber, defaultDeposit);
        runner.sanityCheck(accountsNumber, BigDecimal.valueOf(accountsNumber).multiply(defaultDeposit));
        runner.runBank(iterations, accountsNumber);
        runner.sanityCheck(accountsNumber, BigDecimal.valueOf(accountsNumber).multiply(defaultDeposit));
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
        BigDecimal transferAmount = BigDecimal.valueOf(random.nextDouble() * 100.0);
        int accountInNumber = random.nextInt(maxAccount);
        int accountOutNumber = random.nextInt(maxAccount);
        try {
            bankService.transferMoney(accountInNumber, accountOutNumber, transferAmount);
        } catch (AccountNotFoundException accountNotFoundException) {
            System.out.println("Account with account number:" + accountNotFoundException.getAccountNumber());
        } catch (IllegalAccountAmountException illegalAccountAmountException) {
            if (illegalAccountAmountException.getIllegalAmount().intValue() < 0) {
                System.out.println("Amount for transfer can't be negative value");
            } else {
                System.out.println("insufficient funds on account with account number:" + accountOutNumber);
            }
        }
    }

    private void registerAccounts(int number, BigDecimal defaultMoney) {
        for (int i = 0; i < number; i++) {
            try {
                bankService.registerAccount(i, defaultMoney);
            } catch (AccountExistsException accountExistsException) {
                System.out.println("Account with account number:" + accountExistsException.getAccountNumber() + " already exist");
            }
        }
    }

    private void sanityCheck(int accountMaxNumber, BigDecimal totalExpectedMoney) {
        BigDecimal sum = IntStream.range(0, accountMaxNumber)
                .mapToObj(bankService::getAccount)
                .map(Account::getMoney)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (sum.compareTo(totalExpectedMoney) != 0) {
            throw new IllegalStateException("we got " + sum + " != " + totalExpectedMoney + " (expected)");
        }
        System.out.println("sanity check OK");
    }
}
