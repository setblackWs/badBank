package ch.engenius.bank.api;

public interface AccountService {

    void withdraw(int number, double amount);

    void deposit(int number, double amount);
}
