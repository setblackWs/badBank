package ch.engenius.bank;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class BankBuilder {

    private Bank bank;
    private List<Account> accounts;

    public BankBuilder() {
        InMemoryStore<Integer, Account> accountStore = new InMemoryStore<>();
        this.bank = new Bank(accountStore);
        this.accounts = new ArrayList<>();
    }

    public BankBuilder withAccount(int number, double initialBalance) {
        this.bank.registerAccount(number, BigDecimal.valueOf(initialBalance));
        return this;
    }

    public Bank build() {
        IntStream.range(0, this.accounts.size()).forEach(i -> this.bank.registerAccount(i, this.accounts.get(i).getMoney()));
        return this.bank;
    }

}
