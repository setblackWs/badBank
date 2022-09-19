package ch.engenius.bank.domain;

import ch.engenius.bank.exceptions.NotFoundException;
import lombok.*;

import java.math.BigDecimal;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Bank {

    private HashMap<UUID, BankAccount> accounts = new HashMap<>();

    public void registerBankAccount(UUID accountNumber, BigDecimal initialMoneyAmount) {
        BankAccount account = new BankAccount();
        account.setMoneyAmount(initialMoneyAmount);
        account.setAccountNumber(accountNumber);
        this.accounts.put(accountNumber, account);
    }

    @Synchronized
    public void transferMoney(UUID bankAccountOutId, UUID bankAccountInId, BigDecimal moneyAmount) {
        BankAccount bankAccountOut = getBankAccount(bankAccountOutId);
        BankAccount bankAccountIn = getBankAccount(bankAccountInId);

        bankAccountOut.withdraw(moneyAmount);
        bankAccountIn.deposit(moneyAmount);
    }

    public BankAccount getBankAccount(UUID accountNumber) {
        if (this.accounts.get(accountNumber) == null) {
            throw new NotFoundException("Bank account not found.");
        }
        return this.accounts.get(accountNumber);
    }

    public UUID getRandomBankAccountId() {
        List<UUID> accountIds = new ArrayList<>(getAccounts().keySet());
        return accountIds.get(new Random().nextInt(accountIds.size()));
    }
}
