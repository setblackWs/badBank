import ch.engenius.bank.Account;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountTest {

    private Account testAccount;

    @Before
    public void setUp() {
        testAccount = new Account(BigDecimal.valueOf(TestConstants.INITIAL_ACCOUNT_MONEY));
    }

    @Test
    public void deposit_ShouldIncreaseAmount() {
        testAccount.deposit(TestConstants.DEPOSIT_ACCOUNT_MONEY);
        assertEquals(TestConstants.AFTER_DEPOSIT_ACCOUNT_MONEY, testAccount.getMoney().intValue());
    }

    @Test
    public void withdraw_ShouldDecreaseAmount() {
        testAccount.withdraw(TestConstants.WITHDRAW_ACCOUNT_MONEY);
        assertEquals(TestConstants.AFTER_WITHDRAW_ACCOUNT_MONEY, testAccount.getMoney().intValue());
    }

    @Test(expected = IllegalStateException.class)
    public void withdraw_ShouldThrowException_IfMoneyInsufficient() {
        testAccount.withdraw(TestConstants.WITHDRAW_INSUFFICIENT_ACCOUNT_MONEY);
    }

}
