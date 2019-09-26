package ch.engenius.bank.api;


/**
 * Implementors of this should keep in mind that operations need to be thread safe
 *
 * @param <K> Type of the key
 * @param <V> Type of the value
 */
public interface Store<K, V> {

    void create(K key,  V obj);

    V get(K key);

}
