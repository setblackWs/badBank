package ch.engenius.bank.bank;

import ch.engenius.bank.TestData;
import ch.engenius.bank.exception.AccountNotFoundException;
import ch.engenius.bank.model.Account;
import ch.engenius.bank.model.Bank;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

public class BankUnitTest {
    private final Bank bank =
            Mockito.mock(Bank.class);


    private HashMap<Integer, Account> getBankAccounts() {
        HashMap<Integer, Account> accounts = new HashMap<>();
        for (int i = 0; i < TestData.ACCOUNTS_NUM; i++) {
            accounts.put(i, new Account(BigDecimal.valueOf(TestData.DEFAULT_DEPOSIT)));
        }
        return accounts;
    }

    @Test
    public void givenEmptyBankAccountsThenGetAccountsWillReturnEmptyMap() {
        given(bank.getAccounts()).willReturn(new HashMap<>());

        HashMap<Integer, Account> accounts = bank.getAccounts();

        assertEquals(0, accounts.size());
    }

    @Test
    public void givenBankAccountsThenGetAccountsWillReturnMap() {
        given(bank.getAccounts()).willReturn(getBankAccounts());

        HashMap<Integer, Account> accounts = bank.getAccounts();

        assertEquals(TestData.ACCOUNTS_NUM, accounts.size());
    }

    @Test
    public void givenInvalidAccountNumberThenGetAccountWillThrowException() throws AccountNotFoundException {
        given(bank.getAccount(TestData.NON_EXISTING_ACCOUNT_NUMBER)).willThrow(AccountNotFoundException.class);

        assertThrows(AccountNotFoundException.class, () -> bank.getAccount(TestData.NON_EXISTING_ACCOUNT_NUMBER));
    }

    @Test
    public void givenValidAccountNumberThenGetAccountWillReturnAccount() throws AccountNotFoundException {
        Account account = new Account(BigDecimal.valueOf(100));
        given(bank.getAccount(TestData.EXISTING_ACCOUNT_NUMBER)).willReturn(account);

        Account result = bank.getAccount(TestData.EXISTING_ACCOUNT_NUMBER);

        assertNotNull(account);
        assertEquals(result.getMoney(), account.getMoney());
    }

    @Test
    public void givenGetAllAccountsThenWillReturnAllAccounts() {
        HashMap<Integer, Account> accounts = getBankAccounts();
        given(bank.getAllAccounts()).willReturn(accounts);

        HashMap<Integer, Account> result = bank.getAllAccounts();

        assertEquals(accounts.size(), result.size());
    }
}
