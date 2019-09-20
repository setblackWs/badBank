package ch.engenius.bank.api;

public interface Store<K, V> {

    void create(K key,  V obj);

    V get(K key);
}
