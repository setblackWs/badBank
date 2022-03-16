package ch.engenius.bank.repository;

import ch.engenius.bank.TestData;
import ch.engenius.bank.context.DataContextUnitTest;
import ch.engenius.bank.exception.AccountNotFoundException;
import ch.engenius.bank.model.Account;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

public class AccountRepositoryUnitTest {
    private AccountRepository accountRepository =
            Mockito.mock(AccountRepository.class);

    private HashMap<Integer, Account> getBankAccounts() {
        HashMap<Integer, Account> accounts = new HashMap<>();
        for (int i = 0; i < TestData.ACCOUNTS_NUM; i++) {
            accounts.put(i, new Account(BigDecimal.valueOf(TestData.DEFAULT_DEPOSIT)));
        }
        return accounts;
    }

    @Test
    public void givenInvalidAccountNumberThenGetAccountWillThrowException() throws AccountNotFoundException {
        given(accountRepository.getAccount(TestData.NON_EXISTING_ACCOUNT_NUMBER)).willThrow(AccountNotFoundException.class);

        assertThrows(AccountNotFoundException.class, () -> accountRepository.getAccount(TestData.NON_EXISTING_ACCOUNT_NUMBER));
    }

    @Test
    public void givenValidAccountNumberThenGetAccountWillReturnAccount() throws AccountNotFoundException {
        Account account = new Account(BigDecimal.valueOf(100));
        given(accountRepository.getAccount(TestData.EXISTING_ACCOUNT_NUMBER)).willReturn(account);

        Account result = accountRepository.getAccount(TestData.EXISTING_ACCOUNT_NUMBER);

        assertNotNull(account);
        assertEquals(result.getMoney(), account.getMoney());
    }

    @Test
    public void givenGetAllAccountsThenWillReturnAllAccounts() {
        HashMap<Integer, Account> accounts = getBankAccounts();
        given(accountRepository.getAllAccounts()).willReturn(accounts);

        HashMap<Integer, Account> result = accountRepository.getAllAccounts();

        assertEquals(accounts.size(), result.size());
    }
}
