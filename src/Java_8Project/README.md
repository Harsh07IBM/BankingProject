# Java 8 Banking Demo

A mini banking application demonstrating key **Java 8** features.

## Java 8 Features Covered

| # | Feature | Where Used |
|---|---------|------------|
| 1 | **Streams & Lambda Expressions** | Filtering high-value transactions (`BankingApp.java`) |
| 2 | **Optional** | Handling nullable PAN card in `Customer.java` |
| 3 | **Date-Time API (`java.time`)** | EMI scheduling with `LocalDate` (`BankingApp.java`, `Transaction.java`) |
| 4 | **Default & Static Methods in Interfaces** | `Payment.java` interface with `validate()` and `log()` |
| 5 | **Method References** | Logging with `System.out::println` (`BankingApp.java`) |

## Project Structure

```
Java_8Project/
 ├── src/
 │    └── com/bank/demo/
 │         ├── BankingApp.java          # Main application entry point
 │         ├── Customer.java            # Customer model with Optional PAN
 │         ├── Transaction.java         # Transaction model with LocalDate
 │         ├── Payment.java             # Interface with default & static methods
 │         └── CreditCardPayment.java   # Implements Payment interface
 ├── Reference_code.txt
 └── README.md
```

## How to Compile & Run

```bash
# From the Java_8Project directory:

# Compile
javac src/com/bank/demo/*.java

# Run
java -cp src com.bank.demo.BankingApp
```

## Expected Output

```
High Value Txn: 2500
High Value Txn: 1200
PAN not available
Next EMI due on: <date + 1 month>
Basic validation done.
LOG: Payment processed successfully
Debit
Credit
Transfer
```
