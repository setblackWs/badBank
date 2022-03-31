package ch.engenius.bank.services;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ch.engenius.bank.data.Account;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class BankEndToEndTest {

  private BankService bankService;

  @BeforeAll
  public void setUp() {
    bankService = new BankService();
  }

  @Test
  // tests happy path including account registration, deposit, withdrawal and balance enquiry
  public void successfulTransfer() {
    Account accountIn = bankService.registerAccount(1, BigDecimal.valueOf(1000));
    Account accountOut = bankService.registerAccount(2, BigDecimal.valueOf(2000));

    bankService.transferMoney(BigDecimal.valueOf(100), 1, 2);
    assertEquals(BigDecimal.valueOf(1900), accountOut.getMoney());
    assertEquals(BigDecimal.valueOf(1100), accountIn.getMoney());
  }

  @Test
  // tests handling of invalid scenario where receiver and giver are same account
  public void shouldThrowExceptionWhenInAndOutAccountsAreSame() {
    bankService.registerAccount(1, BigDecimal.valueOf(1000));
    bankService.registerAccount(2, BigDecimal.valueOf(2000));

    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class,
            () -> bankService.transferMoney(BigDecimal.valueOf(100), 1, 1));
    assertEquals("Account numbers must be different.", exception.getMessage());
  }





}