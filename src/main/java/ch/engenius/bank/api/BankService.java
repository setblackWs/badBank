package ch.engenius.bank.api;

import ch.engenius.bank.model.Account;

public interface BankService {

    Account registerAccount(int accountNumber, double amount);

    Account getAccount(int number);
}
