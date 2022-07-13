package ch.engenius.test.account;

import ch.engenius.bank.model.Account;
import ch.engenius.bank.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    private static final BigDecimal ACCOUNT_AMOUNT = BigDecimal.ONE;

    @Mock
    private Account ACCOUNT;

    @InjectMocks
    private AccountService accountService;

    @Test
    public void withdraw_happyFlow() {
        accountService.withdraw(ACCOUNT, ACCOUNT_AMOUNT);

        Mockito.verify(ACCOUNT).lockAccount();
        Mockito.verify(ACCOUNT).withdraw(ACCOUNT_AMOUNT);
        Mockito.verify(ACCOUNT).unlockAccount();
    }

    @Test
    public void deposit() {
        accountService.deposit(ACCOUNT, ACCOUNT_AMOUNT);

        Mockito.verify(ACCOUNT).lockAccount();
        Mockito.verify(ACCOUNT).deposit(ACCOUNT_AMOUNT);
        Mockito.verify(ACCOUNT).unlockAccount();
    }
}
