import ch.engenius.bank.Account;
import ch.engenius.bank.Bank;
import org.junit.Before;
import org.junit.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BankTest {

    private Bank testBank;

    @Before
    public void setUp() {
        testBank = new Bank();
        testBank.registerAccount(TestConstants.ACCOUNT_KEY_1, TestConstants.ACCOUNT_MONEY_1);
        testBank.registerAccount(TestConstants.ACCOUNT_KEY_2, TestConstants.ACCOUNT_MONEY_2);
        testBank.registerAccount(TestConstants.ACCOUNT_KEY_3, TestConstants.ACCOUNT_MONEY_3);
    }

    @Test
    public void getAccount_ShouldReturnExistingOne() {
        Account firstQueriedAccount = testBank.getAccount(TestConstants.ACCOUNT_KEY_1);
        assertEquals(TestConstants.ACCOUNT_MONEY_1, firstQueriedAccount.getMoney().intValue());
        Account secondQueriedAccount = testBank.getAccount(TestConstants.ACCOUNT_KEY_2);
        assertEquals(TestConstants.ACCOUNT_MONEY_2, secondQueriedAccount.getMoney().intValue());
    }

    @Test(expected = NoSuchElementException.class)
    public void getAccount_ShouldThrowException_IfAccountKeyDoesNotExist() {
        testBank.getAccount(TestConstants.NOT_EXISTING_ACCOUNT_KEY);
    }

}
