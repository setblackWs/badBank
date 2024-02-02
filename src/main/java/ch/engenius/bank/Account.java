package ch.engenius.bank;

import ch.engenius.bank.util.ThreadContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;

public class Account {
    private BigDecimal balance = new BigDecimal("0");
    private static final Logger log = LogManager.getLogger(Account.class);
    private final ThreadContext threadContext = new ThreadContext();

    public boolean withdraw(BigDecimal amount) throws InterruptedException {
        return threadContext.withThreadLock(() -> {
            if (balance.compareTo(amount) < 0) {
                log.info("There is not enough balance on this account");
                return false;
            }
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                log.info("Amount cannot be less than or equal to zero.");
                return false;
            }
            balance = balance.subtract(amount);
            return true;
        });
    }

    public boolean deposit(BigDecimal amount) throws InterruptedException {
        return threadContext.withThreadLock(() -> {
            try {
                balance = balance.add(amount);
                return true;
            } catch (Exception e) {
                log.error("Exception occurred {}", e.getMessage());
                return false;
            }
        });
    }

    public BigDecimal getBalance() {
        return balance;
    }
}
