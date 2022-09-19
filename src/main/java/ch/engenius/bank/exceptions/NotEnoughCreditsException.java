package ch.engenius.bank.exceptions;

public class NotEnoughCreditsException extends RuntimeException {

    public NotEnoughCreditsException(String message) {
        super(message);
    }
}
