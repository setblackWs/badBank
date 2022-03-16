package ch.engenius.bank.service;

import ch.engenius.bank.TestData;
import ch.engenius.bank.model.Account;
import ch.engenius.bank.service.account.AccountService;
import ch.engenius.bank.service.account.AccountServiceImpl;
import ch.engenius.bank.service.bank.BankService;
import ch.engenius.bank.service.bank.BankServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

public class BankServiceUnitTest {
    private final BankService bankService =
            Mockito.mock(BankServiceImpl.class);

    private final AccountService accountService =
            Mockito.mock(AccountServiceImpl.class);

    private HashMap<Integer, Account> getBankAccounts() {
        HashMap<Integer, Account> accounts = new HashMap<>();
        for (int i = 0; i < TestData.ACCOUNTS_NUM; i++) {
            accounts.put(i, new Account(BigDecimal.valueOf(TestData.DEFAULT_DEPOSIT)));
        }
        return accounts;
    }

    @Test
    public void givenRegisterAccountsThenAccountsWillBeFull() {
        BigDecimal defaultDeposit = BigDecimal.valueOf(TestData.DEFAULT_DEPOSIT);
        given(accountService.getAllAccounts()).willReturn(getBankAccounts());

        bankService.registerAccounts(TestData.ACCOUNTS_NUM, defaultDeposit);
        HashMap<Integer, Account> accounts = accountService.getAllAccounts();

        assertEquals(accounts.size(), TestData.ACCOUNTS_NUM);
    }
}
