package ch.engenius.bank.runner.runner_helper;

import ch.engenius.bank.context.DataContext;
import ch.engenius.bank.exception.AccountNotFoundException;
import ch.engenius.bank.model.Account;
import ch.engenius.bank.service.account.AccountService;
import ch.engenius.bank.service.account.AccountServiceImpl;

import java.math.BigDecimal;
import java.util.stream.IntStream;

public class BankRunnerSanityChecker {
    private final AccountService accountService;

    public BankRunnerSanityChecker(
            DataContext dataContext) {
        this.accountService = new AccountServiceImpl(dataContext);
    }

    public void sanityCheck(int accountMaxNumber, BigDecimal totalExpectedSum) throws AccountNotFoundException {
        BigDecimal totalSum = calculateAccountsMoneySum(accountMaxNumber);
        checkIfSumAsExpected(totalSum, totalExpectedSum);
        System.out.println("sanity check OK");
    }

    private void checkIfSumAsExpected(BigDecimal sum, BigDecimal totalExpectedMoney) {
        if (sum.compareTo(totalExpectedMoney) != 0) {
            throw new IllegalStateException("we got "+ sum + " != " + totalExpectedMoney +" (expected)");
        }
    }

    private BigDecimal calculateAccountsMoneySum(int accountMaxNumber) throws AccountNotFoundException {
        return IntStream.range(0, accountMaxNumber)
                .mapToObj(this::getAccount)
                .map ( Account::getMoney)
                .reduce( BigDecimal.ZERO, BigDecimal::add);
    }

    private Account getAccount(int accountNumber) {
        try {
            return accountService.getAccount(accountNumber);
        } catch (AccountNotFoundException e) {
            e.printStackTrace();
        }
        return new Account(null);
    }
}
