ATM Management System using Java Swing and MySQL

Introduction-
The ATM Management System is a desktop-based application developed using Java Swing for the graphical user interface GUI and MySQL for backend data storage. The primary goal of this project is to simulate the functioning of a real Automated Teller Machine (ATM).
The system allows users to register for a new account, log in securely using a card number and PIN, and perform essential banking operations such as cash withdrawal, deposit, and balance enquiry. All transactions are processed in real time with immediate updates reflected in the database.
This project provides practical exposure to GUI-based application development, database connectivity using JDBC, and real-time transaction handling, which are key concepts in modern software and banking systems.

Features of the System
* User registration with proper input validation
* Secure login using Card Number and PIN
* ATM-style numeric keypad for PIN entry
* Cash withdrawal with balance validation
* Deposit functionality
* Balance enquiry
* Real-time database updates
* MySQL database integration using JDBC

Technologies Used
* Programming Language: Java
* GUI Framework: Java Swing
* Database: MySQL 8.0 or higher
* Connectivity: JDBC (MySQL Connector/J)
* Development Tools: VS Code / Command Line

Project Description and File Structure
Login.java-
This class serves as the main entry point of the application. 
It displays the ATM login interface along with a numeric keypad.
The user enters their card number and PIN, which are verified against the database.
If the credentials are valid, the user is redirected to the ATM menu. If invalid details are entered, an error message is shown.
The CLEAR button allows users to reset the entered credentials. New users can register by clicking the “NEW USER? REGISTER HERE” button, which redirects them to the signup process.

SignUpPageOne.java-
This class handles the first stage of user registration. 
It collects basic personal details such as name and mobile number.

Validations applied include:
* Fields cannot be left empty
* Mobile number must be exactly 10 digits
After successful validation, an application number is automatically generated and stored in the database.

SignUpPageTwo.java-
This page collects additional user details required to complete the registration process.
Once the information is submitted successfully, the user proceeds to the final stage of account creation.

SignUpPageThree.java
This class finalizes the account creation process.
The user selects the account type and confirms their details.
A unique card number and PIN are automatically generated and stored in the database. The account is then ready for use.

ATMMenu.java-
This class represents the main dashboard displayed after successful login.
From this menu, the user can choose the following options:
* Deposit
* Withdraw
* Balance Enquiry
* Logout

Withdraw.java-
This class handles the cash withdrawal process.
It checks whether the entered amount is valid and ensures that sufficient balance is available. If the transaction is successful, the balance is updated immediately in the database.

Deposit.java-
This class allows users to deposit money into their account.
The deposited amount is instantly added to the account balance and saved in the database.

DBConnection.java-
This is a centralized database connection class.
It manages the connection between the Java application and the MySQL database using JDBC. All database operations in the project use this class to ensure consistency and reusability.

Database Design-

CREATE DATABASE atmdb;
USE atmdb;
CREATE TABLE signup\_page1 (
&app\_no INT AUTO\_INCREMENT PRIMARY KEY,
name VARCHAR(50),
mobile VARCHAR(10)
);

CREATE TABLE users (
card\_no VARCHAR(20) PRIMARY KEY,
pin VARCHAR(4)
);

CREATE TABLE accounts (
card\_no VARCHAR(20) PRIMARY KEY,
balance DOUBLE DEFAULT 0.0
);

How to Run the Project
Step 1: Database Setup
* Ensure the MySQL server is running.
* Create the database and tables using the SQL queries provided above.
Step 2: Compile the Project
Open the terminal in the project directory and run:
javac \*.java
This will compile all Java source files.
Step 3: Run the Application
Run the application by adding the MySQL Connector JAR file to the classpath:
java -cp ".;mysql-connector.jar" Login
(Replace mysql-connector.jar with the actual JAR file name, such as mysql-connector-j-9.0.0.jar.)

Troubleshooting Common Errors-
Unresolved Compilation Problem in VS Code
If the project folder is moved and compilation errors occur:
* Clean the Java workspace:
Ctrl + Shift + P → Java: Clean Java Language Server Workspace
* Compile manually using:
javac \*.java
* Ensure the MySQL Connector JAR file is added to Referenced Libraries in VS Code.

Conclusion-
The ATM Management System successfully demonstrates how a Java Swing-based GUI application can be integrated with a MySQL database using JDBC. The project provides hands-on experience with user authentication, transaction processing, and real-time database updates, closely resembling the behavior of a real ATM system.

The project can be further enhanced by implementing features such as transaction history, mini statements, OTP-based authentication, and advanced security mechanisms.
