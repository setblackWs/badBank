package ch.engenius.bank.context;

import ch.engenius.bank.TestData;
import ch.engenius.bank.exception.AccountNotFoundException;
import ch.engenius.bank.model.Account;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

public class DataContextUnitTest {
    private final DataContext dataContext =
            Mockito.mock(DataContext.class);


    private HashMap<Integer, Account> getBankAccounts() {
        HashMap<Integer, Account> accounts = new HashMap<>();
        for (int i = 0; i < TestData.ACCOUNTS_NUM; i++) {
            accounts.put(i, new Account(BigDecimal.valueOf(TestData.DEFAULT_DEPOSIT)));
        }
        return accounts;
    }

    @Test
    public void givenEmptyBankAccountsThenGetAccountsWillReturnEmptyMap() {
        given(dataContext.getAllAccounts()).willReturn(new HashMap<>());

        HashMap<Integer, Account> accounts = dataContext.getAllAccounts();

        assertEquals(0, accounts.size());
    }

    @Test
    public void givenBankAccountsThenGetAccountsWillReturnMap() {
        given(dataContext.getAllAccounts()).willReturn(getBankAccounts());

        HashMap<Integer, Account> accounts = dataContext.getAllAccounts();

        assertEquals(TestData.ACCOUNTS_NUM, accounts.size());
    }

    @Test
    public void givenInvalidAccountNumberThenGetAccountWillThrowException() throws AccountNotFoundException {
        given(dataContext.getAccount(TestData.NON_EXISTING_ACCOUNT_NUMBER)).willThrow(AccountNotFoundException.class);

        assertThrows(AccountNotFoundException.class, () -> dataContext.getAccount(TestData.NON_EXISTING_ACCOUNT_NUMBER));
    }

    @Test
    public void givenValidAccountNumberThenGetAccountWillReturnAccount() throws AccountNotFoundException {
        Account account = new Account(BigDecimal.valueOf(100));
        given(dataContext.getAccount(TestData.EXISTING_ACCOUNT_NUMBER)).willReturn(account);

        Account result = dataContext.getAccount(TestData.EXISTING_ACCOUNT_NUMBER);

        assertNotNull(account);
        assertEquals(result.getMoney(), account.getMoney());
    }

    @Test
    public void givenGetAllAccountsThenWillReturnAllAccounts() {
        HashMap<Integer, Account> accounts = getBankAccounts();
        given(dataContext.getAllAccounts()).willReturn(accounts);

        HashMap<Integer, Account> result = dataContext.getAllAccounts();

        assertEquals(accounts.size(), result.size());
    }

    @Test
    public void givenSaveAccountThenWillReturnSavedAccount() {
        Account account = new Account(BigDecimal.valueOf(TestData.DEFAULT_DEPOSIT));
        given(dataContext.saveAccount(TestData.VALID_ACCOUNT_NUMBER, account)).willReturn(account);

        Account result = dataContext.saveAccount(TestData.VALID_ACCOUNT_NUMBER, account);

        then(dataContext)
                .should()
                .saveAccount(TestData.VALID_ACCOUNT_NUMBER, account);
        assertNotNull(result);
    }

}
