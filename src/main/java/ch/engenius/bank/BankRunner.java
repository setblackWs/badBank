package ch.engenius.bank;

import ch.engenius.bank.data.Account;
import ch.engenius.bank.services.BankService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class BankRunner {

  private static final int NUMBER_OF_ITERATIONS = 10000;
  private static final int NUMBER_OF_ACCOUNTS = 100;
  private static final BigDecimal DEFAULT_DEPOSIT = BigDecimal.valueOf(1000);
  private static final SplittableRandom random = new SplittableRandom(43);
  private static final ExecutorService executor = Executors.newCachedThreadPool();
  private final BankService bankService = new BankService();

  public static void main(String[] args) {
    BankRunner runner = new BankRunner();
    try {
      runner.registerAccounts(DEFAULT_DEPOSIT);
      runner.sanityCheck(NUMBER_OF_ACCOUNTS,
          DEFAULT_DEPOSIT.multiply(BigDecimal.valueOf(NUMBER_OF_ACCOUNTS)));
      runner.runBank();
      runner.sanityCheck(NUMBER_OF_ACCOUNTS,
          DEFAULT_DEPOSIT.multiply(BigDecimal.valueOf(NUMBER_OF_ACCOUNTS)));
    } catch (IllegalStateException e) {
      System.out.println(e.getMessage());
    }
  }

  private void runBank() {
    List<Callable<Void>> callableTasks = new ArrayList<>();

    for (int i = 0; i < BankRunner.NUMBER_OF_ITERATIONS; i++) {

      Callable<Void> callableTask = () -> runRandomOperation();
      callableTasks.add(callableTask);
    }
    shutdownAndAwaitTermination();
  }


  private Void runRandomOperation() {
    BigDecimal transfer = BigDecimal.valueOf(random.nextDouble() * 100.0);

    int accountInNumber = random.nextInt(BankRunner.NUMBER_OF_ACCOUNTS);
    int accountOutNumber;

    // Preventing generation of same random numbers in sequence because transfer should go from one account to another
    while (true) {
      accountOutNumber = random.nextInt(BankRunner.NUMBER_OF_ACCOUNTS);
      if (accountOutNumber != accountInNumber) {
        break;
      }
    }
    bankService.transferMoney(transfer, accountInNumber, accountOutNumber);
    return null;
  }

  // https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ExecutorService.html
  private void shutdownAndAwaitTermination() {
    BankRunner.executor.shutdown(); // Disable new tasks from being submitted
    try {
      // Wait a while for existing tasks to terminate
      if (!BankRunner.executor.awaitTermination(60, TimeUnit.SECONDS)) {
        BankRunner.executor.shutdownNow(); // Cancel currently executing tasks
        // Wait a while for tasks to respond to being cancelled
        if (!BankRunner.executor.awaitTermination(60, TimeUnit.SECONDS)) {
          System.err.println("Pool did not terminate");
        }
      }
    } catch (InterruptedException ie) {
      // (Re-)Cancel if current thread also interrupted
      BankRunner.executor.shutdownNow();
      // Preserve interrupt status
      Thread.currentThread().interrupt();
    }
  }

  private void registerAccounts(final BigDecimal defaultMoney) {
    for (int i = 0; i < BankRunner.NUMBER_OF_ACCOUNTS; i++) {
      bankService.registerAccount(i, defaultMoney);
    }
  }

  private void sanityCheck(final int accountMaxNumber,
      final BigDecimal totalExpectedMoney) {
    BigDecimal sum = IntStream.range(0, accountMaxNumber)
        .mapToObj(bankService::getAccount)
        .map(Account::getMoney)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    if (sum.compareTo(totalExpectedMoney) != 0) {
      throw new IllegalStateException(
          "Actual sum: " + sum + " is different from expected: " + totalExpectedMoney);
    }
    System.out.println("Sanity check OK");
  }
}
