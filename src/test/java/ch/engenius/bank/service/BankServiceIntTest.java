package ch.engenius.bank.service;

import ch.engenius.bank.domain.Account;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BankServiceIntTest {

    private BankService bankService;

    @BeforeAll
    public void setUp() {
        bankService = new BankService();
    }

    @Test
    public void shouldTransferMoney() {
        Account accountIn = bankService.registerAccount(1, BigDecimal.valueOf(1000));
        Account accountOut = bankService.registerAccount(2, BigDecimal.valueOf(2000));

        bankService.transferMoney(BigDecimal.valueOf(100), 1, 2);
        assertEquals(BigDecimal.valueOf(1900), accountOut.getMoney());
        assertEquals(BigDecimal.valueOf(1100), accountIn.getMoney());
    }

    @Test
    public void shouldCreateAccountWithZeroAmount_whenAmountIsNegative() {
        Account account = bankService.registerAccount(1, BigDecimal.valueOf(-1000));
        assertEquals(BigDecimal.ZERO, account.getMoney());
    }

    @Test
    public void shouldThrowException_whenThereIsNotEnoughMoneyOnAccount() {
        Account accountIn = bankService.registerAccount(1, BigDecimal.valueOf(1000));
        Account accountOut = bankService.registerAccount(2, BigDecimal.valueOf(2000));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->  {
            bankService.transferMoney(BigDecimal.valueOf(2500), 1, 2);
        });
        assertEquals("Not enough credits on account", exception.getMessage());
        assertEquals(BigDecimal.valueOf(1000), accountIn.getMoney());
        assertEquals(BigDecimal.valueOf(2000), accountOut.getMoney());
    }

    @Test
    public void shouldNotTransferMoney_whenTransferAmountIsNegative() {
        Account accountIn = bankService.registerAccount(1, BigDecimal.valueOf(1000));
        Account accountOut = bankService.registerAccount(2, BigDecimal.valueOf(2000));

        bankService.transferMoney(BigDecimal.valueOf(-100), 1, 2);
        assertEquals(BigDecimal.valueOf(1000), accountIn.getMoney());
        assertEquals(BigDecimal.valueOf(2000), accountOut.getMoney());
    }
}
