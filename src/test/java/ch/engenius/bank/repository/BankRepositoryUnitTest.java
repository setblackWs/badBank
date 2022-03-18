package ch.engenius.bank.repository;

import ch.engenius.bank.TestData;
import ch.engenius.bank.model.Account;
import ch.engenius.bank.repository.bank.BankRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

public class BankRepositoryUnitTest {
    private BankRepositoryImpl bankRepository =
            Mockito.mock(BankRepositoryImpl.class);


    @Test
    public void givenSaveAccountThenWillReturnSavedAccount() {
        Account account = new Account(BigDecimal.valueOf(TestData.DEFAULT_DEPOSIT));
        given(bankRepository.saveAccount(TestData.VALID_ACCOUNT_NUMBER, account)).willReturn(account);

        Account result = bankRepository.saveAccount(TestData.VALID_ACCOUNT_NUMBER, account);

        then(bankRepository)
                .should()
                .saveAccount(TestData.VALID_ACCOUNT_NUMBER, account);
        assertNotNull(result);
    }
}
