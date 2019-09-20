package ch.engenius.bank;

import ch.engenius.bank.api.Store;
import ch.engenius.bank.model.Account;

public class AccountStoreBuilder {

    private InMemoryStore<Integer, Account> store;

    public AccountStoreBuilder() {
        this.store = new InMemoryStore<>();
    }

    public AccountStoreBuilder withAccount(int number, Account account) {
        this.store.create(number, account);
        return this;
    }

    public InMemoryStore<Integer, Account> build() {
        return this.store;
    }

}
