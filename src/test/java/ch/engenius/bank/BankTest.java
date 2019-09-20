package ch.engenius.bank;

import org.junit.Test;

import static org.junit.Assert.*;

public class BankTest {

    @Test
    public void registerAccount_validNumberAndAmount_newAccountCreated() throws Exception {
        Bank testObj = new BankBuilder().build();

        testObj.registerAccount(0, 100);

        assertNotNull(testObj.getAccount(0));
    }

    @Test
    public void registerAccount_validNumberAndAmount_newAccountHasCorrectAmount() throws Exception {
        Bank testObj = new BankBuilder().withAccount(0, 100).build();

        double expected = 100;
        double actual = testObj.getAccount(0).getMoney();

        assertEquals(expected, actual, 0f);
    }

    @Test
    public void doTransaction_validAccountsAndAmount_OK() throws Exception {
        Bank testObj = new BankBuilder().withAccount(0, 100).withAccount(1, 100).build();

        double expectedIn = 110;
        double expectedOut = 90;

        testObj.doTransaction(0, 1, 10);

        double actualIn = testObj.getAccount(0).getMoney();
        double actualOut = testObj.getAccount(1).getMoney();

        assertEquals(expectedIn, actualIn, 0f);
        assertEquals(expectedOut, actualOut, 0f);
    }

    @Test
    public void doTransaction_transactionFails_rollbackPerformed() throws Exception {
        Bank testObj = new BankBuilder().withAccount(0, 100).withAccount(1, 100).build();

        double expectedIn = 100;
        double expectedOut = 100;

        testObj.doTransaction(0, 2, 10);

        double actualIn = testObj.getAccount(0).getMoney();
        double actualOut = testObj.getAccount(1).getMoney();

        assertEquals(expectedIn, actualIn, 0f);
        assertEquals(expectedOut, actualOut, 0f);
    }

}