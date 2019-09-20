package ch.engenius.bank;

import ch.engenius.bank.api.Store;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryStore<K, V> implements Store<K, V> {

    private Map<K, V> accounts;

    public InMemoryStore() {
        this.accounts = new ConcurrentHashMap<>();
    }

    @Override
    public void create(K key, V obj) {
        this.accounts.put(key, obj);
    }

    @Override
    public V get(K id) {
        return this.accounts.get(id);
    }
}
