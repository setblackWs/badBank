package ch.engenius.bank;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

public class BankTest {

    private static Bank bank;
    private final int accountNumber = 3;
    private final BigDecimal amount = BigDecimal.valueOf(123.456);

    @BeforeAll
    public static void setUp() {
        bank = new Bank();
    }

    @Test
    @Order(1)
    public void shouldRegisterAccount() {
        bank.registerAccount(accountNumber, amount);

        Account account = bank.getAccount(accountNumber);
        assertNotNull(account);
    }

    @Test
    public void shouldFailToRegisterAccountIfAlreadyExists() {
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> bank.registerAccount(accountNumber, BigDecimal.ZERO));
        assertEquals("account already exists", exception.getMessage());
    }

    @Test
    public void shouldRegisterAccountWithEnoughMoney() {
        Account account = bank.getAccount(accountNumber);

        assertEquals(amount, account.getMoney());
    }

    @Test
    public void shouldFailToGetNonExistingAccount() {
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> bank.getAccount(550));
        assertEquals("account does not exist", exception.getMessage());
    }
}
