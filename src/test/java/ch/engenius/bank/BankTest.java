package ch.engenius.bank;

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

        testObj.doTransaction(0, 1, BigDecimal.valueOf(10));

        BigDecimal actualIn = testObj.getAccount(0).getMoney();
        BigDecimal actualOut = testObj.getAccount(1).getMoney();

        assertEquals(expectedIn, actualIn);
        assertEquals(expectedOut, actualOut);
    }

    @Test
    public void doTransaction_transactionFailsDueToNegativeAmount_rollbackPerformed() throws Exception {
        Bank testObj = new BankBuilder().withAccount(0, 100).withAccount(1, 100).build();

        BigDecimal expectedIn = BigDecimal.valueOf(100.0);
        BigDecimal expectedOut = BigDecimal.valueOf(100.0);

        testObj.doTransaction(0, 1, BigDecimal.valueOf(-10));

        BigDecimal actualIn = testObj.getAccount(0).getMoney();
        BigDecimal actualOut = testObj.getAccount(1).getMoney();

        assertEquals(expectedIn, actualIn);
        assertEquals(expectedOut, actualOut);
    }

}