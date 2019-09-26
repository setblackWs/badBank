package ch.engenius.bank.api;

public class AccountException extends Exception {
    public AccountException() {
    }

    public AccountException(String message) {
        super(message);
    }

}
