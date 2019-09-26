package ch.engenius.bank.api;

import ch.engenius.bank.Account;

import java.math.BigDecimal;

public interface BankService {

    Account registerAccount(int accountNumber, BigDecimal amount);

    Account getAccount(int number);

    void doTransaction(int inAccount, int outAccount, BigDecimal amount) throws AccountException, TransactionFailedException, RetryTransactionException;
}
