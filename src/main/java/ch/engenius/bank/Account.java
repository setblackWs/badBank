package ch.engenius.bank;

import ch.engenius.bank.util.ThreadContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;

public class Account {
    private BigDecimal money = new BigDecimal("0");
    private static final Logger log = LogManager.getLogger(Account.class);
    private final ThreadContext threadContext = new ThreadContext();

    public boolean withdraw(BigDecimal amount) throws InterruptedException {
        return threadContext.withThreadLock(() -> {
            if (money.compareTo(amount) < 0) {
                log.error("There is not enough money on this account");
                return false;
            }
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                log.error("Amount cannot be less than zero.");
                return false;
            }
            money = money.subtract(amount);
            return true;
        });
    }

    public boolean deposit(BigDecimal amount) throws InterruptedException {
        return threadContext.withThreadLock(() -> {
            try {
                money = money.add(amount);
                return true;
            } catch (Exception e) {
                log.error("Exception occurred {}", e.getMessage());
                return false;
            }
        });
    }

    public BigDecimal getMoney() {
        return money;
    }
}
