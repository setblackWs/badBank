package ch.engenius.bank.service;

import ch.engenius.bank.domain.Account;
import ch.engenius.bank.domain.Bank;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BankServiceTest {

    @Mock
    private HashMap<Integer, Account> accounts;

    @Mock
    private Account account;

    @Mock
    private Bank bank;

    @InjectMocks
    private BankService bankService = new BankService();

    @Test
    public void shouldReturnAccountByProvidedNumber() {
        Mockito.when(bank.getAccounts()).thenReturn(accounts);
        Mockito.when(accounts.containsKey(1)).thenReturn(true);
        Mockito.when(accounts.get(1)).thenReturn(account);

        assertEquals(account, bankService.getAccount(1));
    }

    @Test
    public void shouldTestTransferMoney() {
        Account accountIn = Mockito.mock(Account.class);
        Account accountOut = Mockito.mock(Account.class);

        Mockito.when(bank.getAccounts()).thenReturn(accounts);
        Mockito.when(accounts.containsKey(1)).thenReturn(true);
        Mockito.when(accounts.containsKey(2)).thenReturn(true);
        Mockito.when(accounts.get(1)).thenReturn(accountIn);
        Mockito.when(accounts.get(2)).thenReturn(accountOut);

        Mockito.doNothing().when(accountOut).withdraw(BigDecimal.valueOf(100));
        Mockito.doNothing().when(accountIn).deposit(BigDecimal.valueOf(100));

        bankService.transferMoney(BigDecimal.valueOf(100), 1, 2);
        verify(accountOut, times(1)).withdraw(BigDecimal.valueOf(100));
        verify(accountIn, times(1)).deposit(BigDecimal.valueOf(100));
    }
}
