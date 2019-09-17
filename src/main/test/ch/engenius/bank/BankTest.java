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

}