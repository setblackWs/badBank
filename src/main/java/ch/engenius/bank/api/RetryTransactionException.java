package ch.engenius.bank.api;

import java.math.BigDecimal;

public class RetryTransactionException extends Exception{

    private Integer srcAccountId;
    private Integer dstAccountId;
    private BigDecimal amount;

    public RetryTransactionException(Integer srcAccountId, Integer dstAccountId, BigDecimal amount) {
        this.srcAccountId = srcAccountId;
        this.dstAccountId = dstAccountId;
        this.amount = amount;
    }

    public Integer getSrcAccountId() {
        return srcAccountId;
    }

    public Integer getDstAccountId() {
        return dstAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

}
