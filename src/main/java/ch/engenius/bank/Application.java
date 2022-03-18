package ch.engenius.bank;

import ch.engenius.bank.exception.AccountNotFoundException;
import ch.engenius.bank.runner.BankRunner;

public class Application {

    public static void main(String[] args) throws AccountNotFoundException, InterruptedException {
        BankRunner runner = new BankRunner();
        runner.run();
    }
}
