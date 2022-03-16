package ch.engenius.bank;


import ch.engenius.bank.bank.BankUnitTest;
import ch.engenius.bank.repository.AccountRepositoryUnitTest;
import ch.engenius.bank.repository.BankRepositoryUnitTest;
import ch.engenius.bank.service.AccountServiceUnitTest;
import ch.engenius.bank.service.BankServiceUnitTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        BankUnitTest.class,
        AccountRepositoryUnitTest.class,
        BankRepositoryUnitTest.class,
        AccountServiceUnitTest.class,
        BankServiceUnitTest.class,
})
public class BadBankUnitTests {
}
