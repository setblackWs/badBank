package ch.engenius.bank.repository.bank;

import ch.engenius.bank.model.Account;

public interface BankRepository {
    Account saveAccount(int accountNumber, Account account);
}
