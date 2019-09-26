package ch.engenius.bank.api;

import ch.engenius.bank.Account;

import java.math.BigDecimal;

public interface BankService {

    Account registerAccount(Integer accountNumber, BigDecimal amount) throws AccountException;

    Account getAccount(Integer number);

    void doTransaction(Integer inAccount, Integer outAccount, BigDecimal amount) throws AccountException, TransactionFailedException, RetryTransactionException;
}
