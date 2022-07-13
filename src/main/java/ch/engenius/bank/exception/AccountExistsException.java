package ch.engenius.bank.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountExistsException extends RuntimeException{
    private int accountNumber;
}
