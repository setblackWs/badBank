# Fix and refactor

##  Simple bank 

The application is built using 2 classes. 

Account class models how much money a person has. Two main methods are
deposit and withdraw. Account may never have negative deposit. (There is an if statement preventing that).
Bank class models registering account and getting existing account (by id). 

Both classes have a lot of bugs.

## Simulation

There is a 3rd class BankRunner which has a main method.  
This class runs a simulation, where some number of accounts is created.
Then some random transfers are done. This is performed using multiple threads.  
After that the overall amount of money on accounts should not change.

This seems not to be the case.

##  Task

Your job is to find bugs and other problems in the implementation.
1. Make corrections to the Account and Bank classes. (if you do not have time, please simply list the problems that you see).
2. You may also have to change BankRunner class! In such case, please try to preserve its intended logic.
3. Additionally try to fix or list other potential code quality issues that you see.
4. You are free to create additional classes or files as needed. 


## Solution

Two main issues in the assignment are having non-thread-safe operations and the usage of `double` for currency calculations. 
Deposit and withdraw methods now use `ReentrantLock` class and `tryLock()` to ensure that only one thread can change the account balance at 
a time and prevent data corruption. Using `double` for currency calculations introduces precision issues, which are resolved 
with the usage of `BigDecimal`.

Some of the other improvements include:

- Extracting constants into the configuration file.
- Using a `logger` instead of `System.out.println()`. Trace logging has been added; to enable it, go to the resource/log4j2.xml 
configuration file and set the level to trace.
- To ensure the atomicity of the transfer method, both withdraw and deposit are set to return a `boolean`. Both operations,
 must be successful in order to transfer money between accounts.
- Removing the `setMoney` method and using deposit instead.
- Amount validation for negative values.
- Using `ThreadLocalRandom` which is generally recommended in a multithreaded environment.
- `JUnit` tests added.