package ch.engenius.bank.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MoneyTest {

    private Money initialMoney;
    private Money amount;

    @BeforeEach
    public void setUp() {
        initialMoney = new Money(BigDecimal.valueOf(100));
        amount = new Money(BigDecimal.valueOf(30));
    }

    @Test
    void shouldSubtract_whenAmountProvided() {
        assertEquals(initialMoney.subtract(amount), new Money(BigDecimal.valueOf(70)));
    }

    @Test
    void shouldAdd_whenAmountProvided() {
        assertEquals(initialMoney.add(amount), new Money(BigDecimal.valueOf(130)));
    }
}