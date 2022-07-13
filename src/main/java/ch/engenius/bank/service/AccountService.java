package ch.engenius.bank.service;

import ch.engenius.bank.exception.IllegalAccountAmountException;
import ch.engenius.bank.model.Account;

import java.math.BigDecimal;

public class AccountService {
    //Idea of adding AccountService was not to expose directly Account model to BankService. This approach is also good for unit testing, because
    //AccountService will just be mocked and verified that is called. it's not a good idea to put synchronization in model, so AccountService is also
    //Proxy to Account with responsibility of synchronization

    public void withdraw(Account account, BigDecimal amount) throws IllegalAccountAmountException {
        try {
            account.lockAccount();
            account.withdraw(amount);
        }
        finally {
            account.unlockAccount();
        }
    }

    public void deposit(Account account, BigDecimal amount) {
        try {
            account.lockAccount();
            account.deposit(amount);
        }
        finally {
            account.unlockAccount();
        }
    }
}
