package ch.engenius.bank.exceptions;

public class BankMoneyAmountException extends RuntimeException {

    public BankMoneyAmountException(String message) {
        super(message);
    }
}
