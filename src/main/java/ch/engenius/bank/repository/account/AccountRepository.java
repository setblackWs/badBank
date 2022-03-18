package ch.engenius.bank.repository.account;

import ch.engenius.bank.exception.AccountNotFoundException;
import ch.engenius.bank.model.Account;

import java.util.HashMap;

public interface AccountRepository {
    Account getAccount(int accountNumber) throws AccountNotFoundException;
    HashMap<Integer, Account> getAllAccounts();
}
