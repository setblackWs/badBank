package ch.engenius.bank;

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
