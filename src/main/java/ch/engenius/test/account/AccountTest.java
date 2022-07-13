package ch.engenius.test.account;

import ch.engenius.bank.exception.IllegalAccountAmountException;
import ch.engenius.bank.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AccountTest {

    private static final BigDecimal ACCOUNT_AMOUNT = BigDecimal.ONE;
    private static final int ACCOUNT_NUMBER = 1;

    private Account account;

    @BeforeEach
    public void setUp() {
        this.account = Account.builder()
                .money(ACCOUNT_AMOUNT)
                .accountNumber(ACCOUNT_NUMBER)
                .lock(new ReentrantLock())
                .build();
    }

    @Test
    public void deposit_happyFlow() {
        account.deposit(ACCOUNT_AMOUNT);

        assertEquals(BigDecimal.valueOf(2), account.getMoney());
    }

    @Test
    public void withdraw_happyFlow() {
        account.withdraw(ACCOUNT_AMOUNT);

        assertEquals(BigDecimal.ZERO, account.getMoney());
    }

    @Test
    public void withdraw_notEnoughFunds_throwException() {
        BigDecimal amountToWithdraw = BigDecimal.valueOf(2);

        IllegalAccountAmountException actualException = assertThrows(IllegalAccountAmountException.class,
                () -> account.withdraw(amountToWithdraw));

        assertEquals(amountToWithdraw, actualException.getIllegalAmount());
    }

    @Test
    public void lockAccount_happyFlow() {
        account.lockAccount();

        assertTrue(account.getLock().isLocked());
    }

    @Test
    public void unlockAccount_happyFlow() {
        account.lockAccount();
        account.unlockAccount();

        assertFalse(account.getLock().isLocked());
    }
}
