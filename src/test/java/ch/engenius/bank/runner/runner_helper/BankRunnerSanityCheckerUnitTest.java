package ch.engenius.bank.runner.runner_helper;

import ch.engenius.bank.service.account.AccountService;
import ch.engenius.bank.service.account.AccountServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class BankRunnerSanityCheckerUnitTest {
    private final AccountService accountService =
            Mockito.mock(AccountServiceImpl.class);

    @Test
    public void givenSanityCheckThenShouldSucceed() {

    }
}
