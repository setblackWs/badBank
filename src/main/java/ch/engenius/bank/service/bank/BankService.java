package ch.engenius.bank.service.bank;

import java.math.BigDecimal;


public interface BankService {
    void registerAccounts(int number, BigDecimal defaultMoney);

}
