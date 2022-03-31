package ch.engenius.bank.services;

import ch.engenius.bank.data.Account;
import ch.engenius.bank.data.Bank;

import java.math.BigDecimal;

public class BankService {

  private Bank bank;

  public BankService() {
    this.bank = new Bank();
  }

  public Account registerAccount(final int accountNumber, final BigDecimal amount) {
    Account account = new Account(amount);
    bank.getAccounts().put(accountNumber, account);
    return account;
  }

  public Account getAccount(final int number) {
    if (bank.getAccounts().containsKey(number)) {
      return bank.getAccounts().get(number);
    }
    throw new IllegalArgumentException("Account: " + number + "doesn't exist.");
  }

  public void transferMoney(final BigDecimal transfer, final int accountInNumber,
      final int accountOutNumber) {

    if (accountInNumber == accountOutNumber) {
      throw new IllegalArgumentException("Account numbers must be different.");
    }

    Account accIn = getAccount(accountInNumber);
    Account accOut = getAccount(accountOutNumber);
    accOut.withdraw(transfer);
    accIn.deposit(transfer);
  }

}
