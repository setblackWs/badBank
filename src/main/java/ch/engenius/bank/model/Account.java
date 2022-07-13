package ch.engenius.bank.model;

import ch.engenius.bank.exception.IllegalAccountAmountException;
import lombok.*;

import java.math.BigDecimal;
import java.util.concurrent.locks.ReentrantLock;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    private BigDecimal money;
    private int accountNumber;
    private ReentrantLock lock;

    public void withdraw(BigDecimal amount) throws IllegalAccountAmountException {
        validateWithdrawAmount(amount);
        setMoney(money.subtract(amount));
    }

    public void lockAccount() {
        this.lock.lock();
    }

    public void unlockAccount() {
        this.lock.unlock();
    }

    public void deposit(BigDecimal amount) {
        setMoney(money.add(amount));
    }

    private void validateWithdrawAmount(BigDecimal amount) {
        if ((money.subtract(amount).compareTo(BigDecimal.ZERO)) < 0 || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalAccountAmountException(amount);
        }
    }
}
