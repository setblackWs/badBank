package ch.engenius.bank.domain;

import ch.engenius.bank.exceptions.NotEnoughCreditsException;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BankAccount {

    private UUID accountNumber;
    private BigDecimal moneyAmount;

    @Synchronized
    public void withdraw(BigDecimal amount) {
        if ((moneyAmount.compareTo(amount)) < 0) {
            throw new NotEnoughCreditsException("Not enough credits on bank account.");
        }
        setMoneyAmount(moneyAmount.subtract(amount));
    }

    @Synchronized
    public void deposit(BigDecimal amount) {
        setMoneyAmount(moneyAmount.add(amount));
    }
}
