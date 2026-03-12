import java.sql.*;

public class DBConnection {
    private static volatile boolean schemaInitialized = false;

    public static Connection getConnection() {
        String url = readEnv(
            "ATM_DB_URL",
            "jdbc:mysql://localhost:3306/atmdb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
        );
        String user = readEnv("ATM_DB_USER", "YOUR_USERNAME");
        String pass = readEnv("ATM_DB_PASS", "YOUR_PASSWORD");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, user, pass);
            ensureSchema(connection);
            return connection;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(
                "MySQL JDBC driver not found. Ensure mysql-connector-j is in classpath/lib.",
                e
            );
        } catch (SQLException e) {
            throw new RuntimeException(
                "Unable to connect to MySQL. Check ATM_DB_URL/ATM_DB_USER/ATM_DB_PASS. Details: " + e.getMessage(),
                e
            );
        }
    }

    private static String readEnv(String key, String fallback) {
        String value = System.getenv(key);
        return value == null || value.isBlank() ? fallback : value;
    }

    private static void ensureSchema(Connection connection) throws SQLException {
        if (schemaInitialized) {
            return;
        }

        synchronized (DBConnection.class) {
            if (schemaInitialized) {
                return;
            }

            ensureColumn(connection, "signup_page2", "account_type", "VARCHAR(20) NULL");
            ensureColumn(connection, "signup_page2", "nominee_name", "VARCHAR(50) NULL");
            ensureColumn(connection, "signup_page2", "card_issued", "VARCHAR(5) NULL");

            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS accounts (" +
                    "card_no VARCHAR(20) PRIMARY KEY, " +
                    "balance DOUBLE DEFAULT 0, " +
                    "CONSTRAINT fk_card_no FOREIGN KEY (card_no) REFERENCES users(card_no) ON DELETE CASCADE" +
                    ")"
                );

                statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS transactions (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "card_no VARCHAR(20) NOT NULL, " +
                    "txn_type VARCHAR(20) NOT NULL, " +
                    "amount DOUBLE NOT NULL, " +
                    "txn_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "CONSTRAINT fk_txn_card_no FOREIGN KEY (card_no) REFERENCES users(card_no) ON DELETE CASCADE" +
                    ")"
                );

                statement.executeUpdate(
                    "INSERT INTO accounts (card_no, balance) " +
                    "SELECT u.card_no, 0 FROM users u " +
                    "LEFT JOIN accounts a ON a.card_no = u.card_no " +
                    "WHERE a.card_no IS NULL"
                );
            }

            schemaInitialized = true;
        }
    }

    private static void ensureColumn(
        Connection connection,
        String tableName,
        String columnName,
        String definition
    ) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        try (ResultSet columns = metaData.getColumns(connection.getCatalog(), null, tableName, columnName)) {
            if (columns.next()) {
                return;
            }
        }

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(
                "ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " " + definition
            );
        }
    }
}
