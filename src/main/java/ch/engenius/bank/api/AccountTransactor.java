package ch.engenius.bank.api;

import java.math.BigDecimal;

public interface AccountTransactor extends Transactor {

    void deposit(Object key, BigDecimal amount) throws TransactionException, AccountException;

    void withdraw(Object key, BigDecimal amount) throws TransactionException, AccountException;

    BigDecimal getMoney(Object key) throws TransactionException;

}
