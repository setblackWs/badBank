package ch.engenius.bank.data;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Bank {

  private ConcurrentMap<Integer, Account> accounts;

  public Bank() {
    this.accounts = new ConcurrentHashMap<>();
  }

  public ConcurrentMap<Integer, Account> getAccounts() {
    return accounts;
  }
}
