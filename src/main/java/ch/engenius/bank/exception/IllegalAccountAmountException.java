package ch.engenius.bank.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class IllegalAccountAmountException extends RuntimeException {
    private BigDecimal illegalAmount;
}
