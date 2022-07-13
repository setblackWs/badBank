package ch.engenius.test.bank;

import ch.engenius.bank.exception.AccountExistsException;
import ch.engenius.bank.exception.AccountNotFoundException;
import ch.engenius.bank.exception.IllegalAccountAmountException;
import ch.engenius.bank.model.Account;
import ch.engenius.bank.model.Bank;
import ch.engenius.bank.service.AccountService;
import ch.engenius.bank.service.BankService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class BankServiceTest {

    private static final int AMOUNT_ACCOUNT_NUMBER = 1;
    private static final int NO_AMOUNT_ACCOUNT_NUMBER = 2;
    private static final int NOT_EXISTING_ACCOUNT_NUMBER = 3;
    private static final BigDecimal AMOUNT_ONE = BigDecimal.ONE;
    private static final BigDecimal AMOUNT_ZERO = BigDecimal.ZERO;
    private static final Account AMOUNT_ACCOUNT = Account.builder()
            .money(AMOUNT_ONE)
            .accountNumber(AMOUNT_ACCOUNT_NUMBER)
            .build();
    private static final Account NO_AMOUNT_ACCOUNT = Account.builder()
            .money(AMOUNT_ZERO)
            .accountNumber(NO_AMOUNT_ACCOUNT_NUMBER)
            .build();
    @Captor
    private ArgumentCaptor<Account> accountCaptor;

    @Mock
    private Bank bank;
    @Mock
    private AccountService accountService;

    @InjectMocks
    private BankService bankService = new BankService(bank, accountService);

    @Test
    public void transferMoney_happyFlow() {
        Mockito.when(bank.getAccounts()).thenReturn(Arrays.asList(AMOUNT_ACCOUNT, NO_AMOUNT_ACCOUNT));

        bankService.transferMoney(AMOUNT_ACCOUNT_NUMBER, NO_AMOUNT_ACCOUNT_NUMBER, AMOUNT_ONE);

        Mockito.verify(accountService).withdraw(AMOUNT_ACCOUNT, AMOUNT_ONE);
        Mockito.verify(accountService).deposit(NO_AMOUNT_ACCOUNT, AMOUNT_ONE);
    }

    @Test
    public void transferMoney_accountDoesNotExist_throwsException(){
        Mockito.when(bank.getAccounts()).thenReturn(Arrays.asList(AMOUNT_ACCOUNT, NO_AMOUNT_ACCOUNT));

        AccountNotFoundException actualException = assertThrows(AccountNotFoundException.class,
                ()-> bankService.transferMoney( NO_AMOUNT_ACCOUNT_NUMBER,NOT_EXISTING_ACCOUNT_NUMBER,AMOUNT_ONE));

        assertEquals(NOT_EXISTING_ACCOUNT_NUMBER, actualException.getAccountNumber());
    }

    @Test
    public void getAccount_happyFlow(){
        Mockito.when(bank.getAccounts()).thenReturn(Arrays.asList(NO_AMOUNT_ACCOUNT, AMOUNT_ACCOUNT));

        Account actualAccount = bankService.getAccount(AMOUNT_ACCOUNT_NUMBER);

        assertEquals(AMOUNT_ACCOUNT_NUMBER, actualAccount.getAccountNumber());
        assertEquals(AMOUNT_ONE, actualAccount.getMoney());
    }

    @Test
    public void getAccount_accountDoesNotExist_throwsException(){
        Mockito.when(bank.getAccounts()).thenReturn(Arrays.asList(NO_AMOUNT_ACCOUNT, AMOUNT_ACCOUNT));


        AccountNotFoundException actualException = assertThrows(AccountNotFoundException.class,
                () -> bankService.getAccount(NOT_EXISTING_ACCOUNT_NUMBER));

        assertEquals(NOT_EXISTING_ACCOUNT_NUMBER, actualException.getAccountNumber());
    }

    @Test
    public void registerAccount_happyFlow(){
        List<Account> accounts = new ArrayList(Arrays.asList(NO_AMOUNT_ACCOUNT, AMOUNT_ACCOUNT));

        Mockito.when(bank.getAccounts()).thenReturn(accounts);

        bankService.registerAccount(NOT_EXISTING_ACCOUNT_NUMBER, AMOUNT_ZERO);

        Mockito.verify(bank).addAccount(accountCaptor.capture());

        Account capturedAccount = accountCaptor.getValue();
        assertEquals(NOT_EXISTING_ACCOUNT_NUMBER, capturedAccount.getAccountNumber());
        assertEquals(AMOUNT_ZERO, capturedAccount.getMoney());
    }

    @Test
    public void registerAccount_accountAlreadyExist_throwException(){
        List<Account> accounts = new ArrayList(Arrays.asList(NO_AMOUNT_ACCOUNT, AMOUNT_ACCOUNT));

        Mockito.when(bank.getAccounts()).thenReturn(accounts);

        AccountExistsException actualException = assertThrows(AccountExistsException.class,
                () -> bankService.registerAccount(NO_AMOUNT_ACCOUNT_NUMBER, AMOUNT_ZERO));

        assertEquals(NO_AMOUNT_ACCOUNT_NUMBER, actualException.getAccountNumber());
    }

    @Test
    public void registerAccount_negativeAmountNewAccount_throwsException(){
        List<Account> accounts = new ArrayList(Arrays.asList(NO_AMOUNT_ACCOUNT, AMOUNT_ACCOUNT));
        BigDecimal negativeAmount = BigDecimal.valueOf(-1);

        Mockito.when(bank.getAccounts()).thenReturn(accounts);

        IllegalAccountAmountException actualException = assertThrows(IllegalAccountAmountException.class,
                () -> bankService.registerAccount(NOT_EXISTING_ACCOUNT_NUMBER, negativeAmount));

        assertEquals(negativeAmount, actualException.getIllegalAmount());
    }

}
