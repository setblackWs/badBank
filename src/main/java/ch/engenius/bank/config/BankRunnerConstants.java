package ch.engenius.bank.config;

import java.math.BigDecimal;

public class BankRunnerConstants {
    public static final BigDecimal DEPOSIT = new BigDecimal("1000");
    public static final int ITERATIONS = 10000;
    public static final int ACCOUNTS_NUMBER = 100;
    public static final BigDecimal TOTAL_EXPECTED_AMOUNT = DEPOSIT.multiply(BigDecimal.valueOf(ACCOUNTS_NUMBER));
    public static final int THREAD_POOL_NUMBER = 8;
}
