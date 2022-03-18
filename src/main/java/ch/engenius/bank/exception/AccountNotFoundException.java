package ch.engenius.bank.exception;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(int accountNumber) {
        super(String.format("Account with id %d cannot be found!", accountNumber));
    }
}
