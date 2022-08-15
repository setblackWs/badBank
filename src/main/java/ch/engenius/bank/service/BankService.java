package ch.engenius.bank.service;

import ch.engenius.bank.domain.Account;
import ch.engenius.bank.exception.BankException;
import ch.engenius.bank.model.AccountNumber;
import ch.engenius.bank.model.Money;

public interface BankService {

    Account registerAccount(AccountNumber accountNumber, Money amount) throws BankException;

    Account getAccount(AccountNumber accountNumber) throws BankException;
}
