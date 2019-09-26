package ch.engenius.bank.api;

public interface Transactor {

    // enter into a new transaction
    void join(Object key) throws TransactionException;

    // return true if commit is possible
    boolean canCommit(Object key) throws TransactionException;

    // make state change permanent
    void commit(Object key) throws TransactionException;

    // roll back state change
    void abort(Object key);

}
