package ch.engenius.bank;

import ch.engenius.bank.api.AccountException;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class BankTest {

    @Test
    public void registerAccount_validNumberAndAmount_newAccountCreated() throws Exception {
        Bank testObj = new BankBuilder().build();

        testObj.registerAccount(0, BigDecimal.TEN);

        assertNotNull(testObj.getAccount(0));
    }

    @Test
    public void registerAccount_validNumberAndAmount_newAccountHasCorrectAmount() throws Exception {
        Bank testObj = new BankBuilder().withAccount(0, 100).build();

        BigDecimal expected = BigDecimal.valueOf(100.0);
        BigDecimal actual = testObj.getAccount(0).getMoney();

        assertEquals(expected, actual);
    }

    @Test
    public void doTransaction_validAccountsAndAmount_OK() throws Exception {
        Bank testObj = new BankBuilder().withAccount(0, 100).withAccount(1, 100).build();

        BigDecimal expectedIn = BigDecimal.valueOf(110.0);
        BigDecimal expectedOut = BigDecimal.valueOf(90.0);

        testObj.doTransaction(1, 0, BigDecimal.valueOf(10));

        BigDecimal actualIn = testObj.getAccount(0).getMoney();
        BigDecimal actualOut = testObj.getAccount(1).getMoney();

        assertEquals(expectedIn, actualIn);
        assertEquals(expectedOut, actualOut);
    }

    @Test(expected = AccountException.class)
    public void doTransaction_transactionFailsDueToNegativeAmount_throwsAccountException() throws Exception {
        Bank testObj = new BankBuilder().withAccount(0, 100).withAccount(1, 100).build();

        testObj.doTransaction(1, 0, BigDecimal.valueOf(-10));
    }

    @Test
    public void doTransaction_transactionFailsDueToUnsufficientBalance_performsRollback() throws Exception {
        BigDecimal expectedIn = BigDecimal.valueOf(100.0);
        BigDecimal expectedOut = BigDecimal.valueOf(10.0);

        Bank testObj = new BankBuilder().withAccount(0, expectedIn.doubleValue()).withAccount(1, expectedOut.doubleValue()).build();

        try {
            testObj.doTransaction(1, 0, BigDecimal.valueOf(100));
        } catch (AccountException e) {
            // skip
        }

        BigDecimal actualIn = testObj.getAccount(0).getMoney();
        BigDecimal actualOut = testObj.getAccount(1).getMoney();

        assertEquals(expectedIn, actualIn);
        assertEquals(expectedOut, actualOut);
    }

}