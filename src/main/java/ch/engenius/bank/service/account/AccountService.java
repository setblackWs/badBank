package ch.engenius.bank.service.account;

import ch.engenius.bank.exception.AccountNotFoundException;
import ch.engenius.bank.model.Account;

import java.math.BigDecimal;
import java.util.HashMap;

public interface AccountService {
    Account getAccount(int accountNumber) throws AccountNotFoundException;
    void makeTransaction(int accountIn, int accountOut, BigDecimal amount) throws AccountNotFoundException, InterruptedException;
    HashMap<Integer, Account> getAllAccounts();
}
