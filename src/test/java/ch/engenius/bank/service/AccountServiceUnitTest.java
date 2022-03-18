package ch.engenius.bank.service;

import ch.engenius.bank.TestData;
import ch.engenius.bank.exception.AccountNotFoundException;
import ch.engenius.bank.model.Account;
import ch.engenius.bank.repository.account.AccountRepository;
import ch.engenius.bank.repository.account.AccountRepositoryImpl;
import ch.engenius.bank.service.account.AccountService;
import ch.engenius.bank.service.account.AccountServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class AccountServiceUnitTest {
    private AccountService accountService =
            Mockito.mock(AccountServiceImpl.class);

    private AccountRepository accountRepository =
            Mockito.mock(AccountRepositoryImpl.class);

    private HashMap<Integer, Account> getBankAccounts() {
        HashMap<Integer, Account> accounts = new HashMap<>();
        for (int i = 0; i < TestData.ACCOUNTS_NUM; i++) {
            accounts.put(i, new Account(BigDecimal.valueOf(TestData.DEFAULT_DEPOSIT)));
        }
        return accounts;
    }

    @Test
    public void givenInvalidAccountNumberThenGetAccountWillThrowException() throws AccountNotFoundException {
        given(accountService.getAccount(TestData.NON_EXISTING_ACCOUNT_NUMBER)).willThrow(AccountNotFoundException.class);

        assertThrows(AccountNotFoundException.class, () -> accountService.getAccount(TestData.NON_EXISTING_ACCOUNT_NUMBER));
    }

    @Test
    public void givenValidAccountNumberThenGetAccountWillReturnAccount() throws AccountNotFoundException {
        Account account = new Account(BigDecimal.valueOf(100));
        given(accountService.getAccount(TestData.EXISTING_ACCOUNT_NUMBER_1)).willReturn(account);

        Account result = accountService.getAccount(TestData.EXISTING_ACCOUNT_NUMBER_1);

        assertNotNull(account);
        assertEquals(result.getMoney(), account.getMoney());
    }

    @Test
    public void givenMakeTransactionWithInvalidAccountNumberWillThrowException() throws AccountNotFoundException, InterruptedException {
        BigDecimal amount = BigDecimal.valueOf(100);
        given(accountService.getAccount(TestData.NON_EXISTING_ACCOUNT_NUMBER)).willThrow(AccountNotFoundException.class);

        accountService.makeTransaction(TestData.NON_EXISTING_ACCOUNT_NUMBER, TestData.EXISTING_ACCOUNT_NUMBER_1, amount);

        assertThrows(AccountNotFoundException.class, () -> accountService.getAccount(TestData.NON_EXISTING_ACCOUNT_NUMBER));
    }

    @Test
    public void givenMakeTransactionWithValidAccountNumberWillSucceed() throws AccountNotFoundException, InterruptedException {
        BigDecimal amount = BigDecimal.valueOf(TestData.DEFAULT_DEPOSIT);
        BigDecimal transactionAmount = BigDecimal.valueOf(100);
        Account account1 = new Account(amount);
        Account account2 = new Account(amount);
        given(accountService.getAccount(TestData.EXISTING_ACCOUNT_NUMBER_1)).willReturn(account1);
        given(accountService.getAccount(TestData.EXISTING_ACCOUNT_NUMBER_2)).willReturn(account2);

        accountService.makeTransaction(TestData.EXISTING_ACCOUNT_NUMBER_1, TestData.EXISTING_ACCOUNT_NUMBER_2, transactionAmount);

        verify(accountService, times(1))
                .makeTransaction(TestData.EXISTING_ACCOUNT_NUMBER_1, TestData.EXISTING_ACCOUNT_NUMBER_2, transactionAmount);
    }

    @Test
    public void givenGetAllAccountsThenWillReturnAllAccounts() {
        HashMap<Integer, Account> accounts = getBankAccounts();
        given(accountService.getAllAccounts()).willReturn(accounts);

        HashMap<Integer, Account> result = accountService.getAllAccounts();

        assertEquals(accounts.size(), result.size());
    }

}
