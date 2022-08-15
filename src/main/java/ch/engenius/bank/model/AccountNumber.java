package ch.engenius.bank.model;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class AccountNumber {

    @NonNull
    @PositiveOrZero
    private int number;
}
