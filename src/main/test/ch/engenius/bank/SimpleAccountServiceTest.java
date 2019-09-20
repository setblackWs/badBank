package ch.engenius.bank;

import ch.engenius.bank.api.AccountService;
import ch.engenius.bank.api.Store;
import ch.engenius.bank.model.Account;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleAccountServiceTest {

    @Test
    public void withdraw_EnoughMoney_correctAmountWithdrawn() throws Exception {
        Store<Integer, Account> accountStore = new AccountStoreBuilder().withAccount(0, anAccount(100d)).build();
        AccountService testObj = new SimpleAccountService(accountStore);

        double expected = 90;
        testObj.withdraw(0, 10);
        double actual = accountStore.get(0).getMoney();

        assertEquals(expected, actual, 0f);
    }

    @Test(expected = IllegalStateException.class)
    public void withdraw_NotEnoughMoney_throwsIllegalStateException() throws Exception {
        AccountService testObj = new SimpleAccountService(new AccountStoreBuilder().withAccount(0, anAccount(90d)).build());

        testObj.withdraw(0, 100d);
    }

    @Test
    public void deposit_PositiveAmount_addsCorrectAmount() throws Exception {
        Store<Integer, Account> accountStore = new AccountStoreBuilder().withAccount(0, anAccount(100d)).build();
        AccountService testObj = new SimpleAccountService(accountStore);

        double expected = 110;
        testObj.deposit(0, 10d);
        double actual = accountStore.get(0).getMoney();

        assertEquals(expected, actual, 0f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deposit_NegativeAmount_throwsIllegalArgumentException() throws Exception {
        AccountService testObj = new SimpleAccountService(new AccountStoreBuilder().withAccount(0, anAccount(90d)).build());

        testObj.deposit(0, -10d);
    }

    // It would be nicer to have a builder here, but for the sake of simplicity we just have a simple method for now
    private Account anAccount(double initialMoney) {
        Account account = new Account();
        account.setMoney(initialMoney);
        return account;
    }
}