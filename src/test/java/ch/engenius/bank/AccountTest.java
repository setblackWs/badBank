package ch.engenius.bank;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AccountTest {

    private Account account;
    private final BigDecimal initialMoney = BigDecimal.valueOf(123.456);

    @BeforeEach
    public void setUp() {
        account = new Account(initialMoney);
    }

    @Test
    public void shouldDepositMoney() {
        final BigDecimal depositAmount = BigDecimal.valueOf(11);

        account.deposit(depositAmount);

        assertEquals(initialMoney.add(depositAmount), account.getMoney());
    }

    @Test
    public void shouldWithdrawMoney() {
        final BigDecimal withdrawalAmount = BigDecimal.valueOf(22);

        account.withdraw(withdrawalAmount);

        assertEquals(initialMoney.subtract(withdrawalAmount), account.getMoney());
    }

    @Test
    public void shouldFailToDepositNothing() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> account.deposit(BigDecimal.ZERO));
        assertEquals("cannot deposit zero or negative amount", exception.getMessage());
    }

    @Test
    public void shouldFailToDepositNegativeAmount() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> account.deposit(BigDecimal.valueOf(-100)));
        assertEquals("cannot deposit zero or negative amount", exception.getMessage());
    }

    @Test
    public void shouldWithdrawAllMoney() {
        account.withdraw(initialMoney);

        assertEquals(BigDecimal.ZERO, account.getMoney().setScale(0));
    }

    @Test
    public void shouldFailToWithdrawNothing() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> account.withdraw(BigDecimal.ZERO));
        assertEquals("cannot withdraw zero or negative amount", exception.getMessage());
    }

    @Test
    public void shouldFailToWithdrawNegativeAmount() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> account.withdraw(BigDecimal.valueOf(-100)));
        assertEquals("cannot withdraw zero or negative amount", exception.getMessage());
    }

    @Test
    public void shouldFailToWithdrawWhenMissingMoney() {
        final BigDecimal withdrawalAmount = BigDecimal.valueOf(321);

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> account.withdraw(BigDecimal.valueOf(321)));
        assertEquals("not enough credit", exception.getMessage());
    }
}
