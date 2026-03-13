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
- IntelliJ IDEA

## Project Structure

- `src/Login.java` - application entry point and login screen
- `src/SignUpPageOne.java` - personal details and DOB validation
- `src/SignUpPageTwo.java` - additional applicant details
- `src/SignUpPageThree.java` - account creation rules and final setup
- `src/ATMMenu.java` - transaction menu after login
- `src/Deposit.java` - deposit screen
- `src/Withdraw.java` - withdraw screen
- `src/FastCash.java` - preset withdrawal amounts
- `src/BalanceEnquiry.java` - balance screen
- `src/DBConnection.java` - JDBC connection and schema checks
- `src/database.sql` - base schema and sample data
- `lib/mysql-connector-j-9.5.0.jar` - MySQL JDBC driver

## Business Rules

- Users aged 18 or above can open a normal account and receive an ATM card and PIN.
- Users below 18 are restricted to a joint account with a nominee.
- Minors do not receive ATM card credentials during signup.

## Database Setup

The application connects to MySQL database `atmdb`.

1. Create the database in MySQL.
2. Run the SQL script from `src/database.sql`.
3. Confirm the MySQL server is running before starting the app.

`DBConnection.java` also creates the runtime tables `accounts` and `transactions` if they are missing, and it adds required columns to `signup_page2` when needed.

## Run In IntelliJ IDEA

1. Open the project folder in IntelliJ IDEA.
2. Set the project SDK to a working Java version.
3. Add `lib/mysql-connector-j-9.5.0.jar` as a library if IntelliJ does not detect it automatically.
4. Ensure MySQL is running and the `atmdb` schema is available.
5. Open `src/Login.java` and run the `main` method.

## Optional Environment Variables

The app supports these environment variables:

- `ATM_DB_URL`
- `ATM_DB_USER`
- `ATM_DB_PASS`

If they are not set, the code falls back to the defaults in `src/DBConnection.java`.

## Terminal Run

If you want to run the project outside IntelliJ:

```powershell
javac -cp "lib/mysql-connector-j-9.5.0.jar" -d out src\*.java
java -cp "out;lib/mysql-connector-j-9.5.0.jar" Login
```

## Notes

- This is a Swing desktop application, so final verification is manual.
- Fast cash transactions are stored with transaction type `FAST_CASH`.
- IDE-specific folders such as `.vscode` and `.idea` are not required for running the project.
