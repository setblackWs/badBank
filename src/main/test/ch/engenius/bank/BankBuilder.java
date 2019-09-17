package ch.engenius.bank;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class BankBuilder {

    private Bank bank;
    private List<Account> accounts;

    public BankBuilder() {
        this.bank = new Bank();
        this.accounts = new ArrayList<>();
    }

    public BankBuilder withAccount(int number, double initialBalance) {
        this.bank.registerAccount(number, initialBalance);
        return this;
    }

    public Bank build() {
        IntStream.range(0, this.accounts.size()).forEach(i -> this.bank.registerAccount(i, (int) this.accounts.get(i).getMoney()));
        return this.bank;
    }

}
