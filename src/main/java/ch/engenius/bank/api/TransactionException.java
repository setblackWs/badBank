package ch.engenius.bank.api;

public class TransactionException extends Exception {

    public TransactionException() {
    }

    public TransactionException(String message) {
        super(message);
    }

}
