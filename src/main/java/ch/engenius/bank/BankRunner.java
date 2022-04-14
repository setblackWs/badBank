package ch.engenius.bank;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class BankRunner {

    private static final ExecutorService executor = Executors.newFixedThreadPool(8);

    private final Random random = new Random(43);
    private final Bank bank = new Bank();


    public static void main(String[] args) {
        BankRunner runner = new BankRunner();
        int accounts = 100;
        int defaultDeposit  = 1000;
        int iterations  = 10000;
        runner.registerAccounts(accounts, defaultDeposit);
        runner.sanityCheck(accounts, accounts*defaultDeposit);
        runner.runBank(iterations, accounts);
        runner.sanityCheck(accounts, accounts*defaultDeposit);

    }

    private void runBank(int iterations, int maxAccount) {
        for (int i =0; i< iterations; i++ ) {
            executor.submit( ()-> runRandomOperation(maxAccount));
        }
        try {
            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (!executor.isTerminated()) {
                List<Runnable> operations = executor.shutdownNow();
                System.err.println("# of operations that failed to complete: " + operations.size());
            }
        }
    }

    private void runRandomOperation(int maxAccount) {
        BigDecimal transfer = BigDecimal.valueOf(random.nextDouble())
                                .multiply(BigDecimal.valueOf(100));
        int accountInNumber = random.nextInt(maxAccount);
        int accountOutNumber = random.nextInt(maxAccount);
        Account accIn  =bank.getAccount(accountInNumber);
        Account accOut  =bank.getAccount(accountOutNumber);
        accOut.withdraw(transfer);
        accIn.deposit(transfer);
    }

    private void  registerAccounts(int number, int defaultMoney) {
        for ( int i = 0; i < number; i++) {
            bank.registerAccount(i, BigDecimal.valueOf(defaultMoney));
        }
    }

    private void sanityCheck( int accountMaxNumber, int totalExpectedMoney) {
        BigDecimal sum = IntStream.range(0, accountMaxNumber)
                .mapToObj( bank::getAccount)
                .map ( Account::getMoney)
                .reduce( BigDecimal.ZERO, BigDecimal::add);

        if ( sum.intValue() != totalExpectedMoney) {
            throw new IllegalStateException("we got "+ sum + " != " + totalExpectedMoney +" (expected)");
        }
        System.out.println("sanity check OK");
    }


}
