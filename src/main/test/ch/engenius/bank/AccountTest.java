package ch.engenius.bank;

import org.junit.Test;

import static org.junit.Assert.*;

public class AccountTest {

    @Test
    public void withdraw_EnoughMoney_correctAmountWithdrawn() throws Exception {
        Account testObj = anAccount(100d);

        double expected = 90;
        testObj.withdraw(10);
        double actual = testObj.getMoney();

        assertEquals(expected, actual, 0f);
    }

    @Test(expected = IllegalStateException.class)
    public void withdraw_NotEnoughMoney_throwsIllegalStateException() throws Exception {
        Account testObj = anAccount(90d);

        testObj.withdraw(100d);
    }

    @Test
    public void deposit_PositiveAmount_addsCorrectAmount() throws Exception {
        Account testObj = anAccount(100d);

        double expected = 110;
        testObj.deposit(10d);
        double actual = testObj.getMoney();

        assertEquals(expected, actual, 0f);

    }

    @Test(expected = IllegalArgumentException.class)
    public void deposit_NegativeAmount_throwsIllegalArgumentException() throws Exception {
        Account testObj = anAccount(100d);

        testObj.deposit(-10d);
    }

    // It would be nicer to have a builder here, but for the sake of simplicity we just have a simple method for now
    private Account anAccount(double initialMoney) {
        Account account = new Account();
        account.setMoney(initialMoney);
        return account;
    }
}