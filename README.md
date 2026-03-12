# ATM Management System

Java Swing ATM simulation backed by MySQL and JDBC.

## Features

- Secure login with card number and PIN
- New user registration across three signup pages
- Adult/minor account handling
- Adults can receive ATM card credentials
- Applicants below 18 are restricted to joint accounts with a nominee
- Deposit, withdraw, balance enquiry, and fast cash
- Transaction logging in MySQL
- ATM-style navigation with back and home actions

## Tech Stack

- Java
- Java Swing
- MySQL
- JDBC with MySQL Connector/J

## Project Structure

- `src/Login.java`: main login screen
- `src/SignUpPageOne.java`: personal details and DOB validation
- `src/SignUpPageTwo.java`: additional details
- `src/SignUpPageThree.java`: account issuance rules and final account setup
- `src/ATMMenu.java`: transaction menu
- `src/Deposit.java`: deposit screen
- `src/Withdraw.java`: manual withdraw screen
- `src/FastCash.java`: preset withdrawals like `100`, `500`, `1000`
- `src/BalanceEnquiry.java`: balance screen
- `src/DBConnection.java`: JDBC connection and schema self-healing
- `src/database.sql`: schema and sample data

## Business Rules

- Users aged 18 or above can open a normal account and receive an ATM card and PIN.
- Users below 18 are restricted to a joint account with a nominee.
- Minors do not receive ATM card credentials during signup.

## Database

The application expects database `atmdb`.

Main tables:

- `signup_page1`
- `signup_page2`
- `users`
- `accounts`
- `transactions`

`DBConnection.java` also ensures missing runtime tables and required signup columns are created automatically when the app connects.

## Run

1. Create the schema:

```sql
source src/database.sql;
```

2. Compile:

```powershell
javac -d out src\*.java
```

3. Run:

```powershell
java -cp "out;lib/mysql-connector-j-9.5.0.jar" Login
```

You can also use the VS Code launch configuration already included in the repo.

## Environment Variables

Optional variables supported by the app:

- `ATM_DB_URL`
- `ATM_DB_USER`
- `ATM_DB_PASS`

If not set, the project falls back to the defaults in `DBConnection.java`.

## Notes

- The app uses Swing screens, so runtime verification is partly manual.
- Fast cash transactions are stored with type `FAST_CASH`.
