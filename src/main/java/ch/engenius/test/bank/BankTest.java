package ch.engenius.test.bank;

import ch.engenius.bank.model.Account;
import ch.engenius.bank.model.Bank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class BankTest {

    private Bank bank;

    @BeforeEach
    public void setUp() {
        this.bank = new Bank();
    }

    @Test
    public void addAccount_happyFlow() {
        Account account = new Account();

        bank.addAccount(account);

        assertEquals(1, bank.getAccounts().size());
    }
}
