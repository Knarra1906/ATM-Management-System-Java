-- ==============================
-- ATM MANAGEMENT SYSTEM DATABASE
-- ==============================

DROP DATABASE IF EXISTS atmdb;
CREATE DATABASE atmdb;
USE atmdb;

-- ==============================
-- PAGE 1 : PERSONAL DETAILS
-- ==============================

DROP TABLE IF EXISTS signup_page1;

CREATE TABLE signup_page1 (
    app_no INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    father_name VARCHAR(50) NOT NULL,
    dob VARCHAR(15) NOT NULL,
    gender VARCHAR(10) NOT NULL,
    email VARCHAR(50) NOT NULL,
    marital_status VARCHAR(10) NOT NULL,
    address VARCHAR(150) NOT NULL,
    city VARCHAR(30) NOT NULL,
    pincode VARCHAR(6) NOT NULL,
    state VARCHAR(30) NOT NULL
);

-- ==============================
-- PAGE 2 : ADDITIONAL DETAILS
-- ==============================

DROP TABLE IF EXISTS signup_page2;

CREATE TABLE signup_page2 (
    app_no INT PRIMARY KEY,
    religion VARCHAR(20) NOT NULL,
    category VARCHAR(10) NOT NULL,
    income VARCHAR(20) NOT NULL,
    education VARCHAR(30) NOT NULL,
    occupation VARCHAR(30) NOT NULL,
    pan VARCHAR(10),
    aadhar VARCHAR(12) NOT NULL,
    senior_citizen VARCHAR(5) NOT NULL,
    account_type VARCHAR(20),
    nominee_name VARCHAR(50),
    card_issued VARCHAR(5),

    CONSTRAINT fk_app_no
        FOREIGN KEY (app_no)
        REFERENCES signup_page1(app_no)
        ON DELETE CASCADE
);

-- ==============================
-- USERS (LOGIN TABLE)
-- ==============================

DROP TABLE IF EXISTS users;

CREATE TABLE users (
    card_no VARCHAR(20) PRIMARY KEY,
    pin VARCHAR(4) NOT NULL,
    role VARCHAR(10) NOT NULL
);

-- ==============================
-- ACCOUNTS (BALANCE TABLE)
-- ==============================

DROP TABLE IF EXISTS accounts;

CREATE TABLE accounts (
    card_no VARCHAR(20) PRIMARY KEY,
    balance DOUBLE DEFAULT 0,

    CONSTRAINT fk_card_no
        FOREIGN KEY (card_no)
        REFERENCES users(card_no)
        ON DELETE CASCADE
);

-- ==============================
-- TRANSACTIONS (MINI STATEMENT)
-- ==============================

DROP TABLE IF EXISTS transactions;

CREATE TABLE transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    card_no VARCHAR(20) NOT NULL,
    txn_type VARCHAR(20) NOT NULL,
    amount DOUBLE NOT NULL,
    txn_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_txn_card_no
        FOREIGN KEY (card_no)
        REFERENCES users(card_no)
        ON DELETE CASCADE
);

-- ==============================
-- TEST DATA (OPTIONAL)
-- ==============================

-- Dummy user for login testing
INSERT INTO users VALUES ('1234567890123456', '1234', 'User');
INSERT INTO accounts VALUES ('1234567890123456', 5000);
INSERT INTO transactions (card_no, txn_type, amount) VALUES ('1234567890123456', 'DEPOSIT', 3000);
INSERT INTO transactions (card_no, txn_type, amount) VALUES ('1234567890123456', 'WITHDRAW', 1000);

-- ==============================
-- VERIFY TABLES
-- ==============================

SELECT * FROM signup_page1;
SELECT * FROM signup_page2;
SELECT * FROM users;
SELECT * FROM accounts;
SELECT * FROM transactions;
