package ch.engenius.bank.model;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.math.BigDecimal;

@Value
@RequiredArgsConstructor
public class Money {

    @NonNull
    @PositiveOrZero
    private BigDecimal amount;

    public Money subtract(Money value) {
        return new Money(amount.subtract(value.getAmount()));
    }

    public Money add(Money value) {
        return new Money(amount.add(value.getAmount()));
    }
}
