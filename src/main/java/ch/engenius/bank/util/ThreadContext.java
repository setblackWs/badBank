package ch.engenius.bank.util;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

public class ThreadContext {
    ReentrantLock lock = new ReentrantLock();

    public boolean withThreadLock(Supplier<Boolean> operation) throws InterruptedException {
        if (lock.tryLock(1, TimeUnit.SECONDS)) {
            try {
                return operation.get();
            } finally {
                lock.unlock();
            }
        } else {
            return false;
        }
    }
}
