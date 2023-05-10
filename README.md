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

Corrections:
1) The setMoney() was removed from the Account class to promote encapsulation and ensure proper handling of account balance modifications. 
By removing the setMoney method, we prevent direct access to the money variable from outside the class, ensuring that all modifications
to account balances must go through the deposit and withdraw methods. This approach helps maintain data integrity and prevents misuse
of the Account class. Instead of using the setMoney method to set the initial balance during account registration, we use the deposit 
method, which is more appropriate in this context. By making these changes, we ensure that the Account class follows the principle of 
encapsulation and provides a cleaner and more robust implementation.

2) Using double for currency calculations can lead to precision issues. It's better to use BigDecimal for currency-related calculations.
This change eliminates any potential issues with floating-point arithmetic inaccuracies.

3) It's better to use Loggers instead of System.out.println(). Loggers can be configured to output log messages to various destinations, 
such as console, files, or remote logging services. This makes it easy to collect and manage logs from different parts of application or 
even multiple applications. Logging frameworks usually handle log messages more efficiently than System.out.println() particularly when 
writing to file or other output streams.

4) When the InterruptedException is caught, it's generally a good practise to either re-interrupt the current thread or propagate the 
exception further. The Thread.currentThread().interrupt() method call re-interrupts the current thread, allowing other parts of code to
handle interruption if necessary. Also, we log the exception using LOGGER instead of calling e.printStackTrace().

5)  Exception in thread "main" java.lang.IllegalStateException: we got 106524.836545496162378 != 100000 (expected)
	    at ch.engenius.bank.BankRunner.sanityCheck(BankRunner.java:65)
	    at ch.engenius.bank.BankRunner.main(BankRunner.java:26)
This issue is solved with introducing lock method using Reentrant lock in deposit and withdraw methods. This ensures that only one thread 
can modify the balance of an account at a time, and that the threads can access the transfer method in parallel. This will prevent the balance 
from being corrupted and ensure thread safety.

6) In original code for runRandomOperation() the deposit method was first called, and then withdraw, but the correct way is to first withdraw money
and then do the deposit. The transfer logic was also moved to a separate method and put in the Bank class.

7) For ExecutorService part in runBank() method it is better to add the tasks in a list through iteration and then use invokeAll() method that 
ensure us that all the tasks will be finished before we shutdown the service.
 
8) Added JUnit tests, class diagram, .gitignore, javadoc. Names of some variables and methods are changed to be more descriptive.  

