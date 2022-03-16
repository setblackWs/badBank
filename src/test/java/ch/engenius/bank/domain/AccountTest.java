package ch.engenius.bank.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AccountTest {

    private Account account;

    @BeforeEach
    public void setUp() {
        account = new Account(BigDecimal.valueOf(1000));
    }

    @Test
    public void shouldCreateAccount() {
        assertEquals(BigDecimal.valueOf(1000), account.getMoney());
    }

    @Test
    public void shouldAddMoney() {
        account.deposit(BigDecimal.valueOf(100));
        assertEquals(BigDecimal.valueOf(1100), account.getMoney());
    }

    @Test
    public void shouldSubtractMoney() {
        account.withdraw(BigDecimal.valueOf(100));
        assertEquals(BigDecimal.valueOf(900), account.getMoney());
    }

    @Test
    public void shouldThrowException_whenThereIsNotEnoughMoney() {
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> account.withdraw(BigDecimal.valueOf(2000)));
        assertEquals("Not enough credits on account", exception.getMessage());
        assertEquals(BigDecimal.valueOf(1000), account.getMoney());
    }
}
