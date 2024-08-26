package server.service;

import java.sql.*;

public class DatabaseConnection {

    private static final String JDBC_URL = "jdbc:h2:~/test;DB_CLOSE_DELAY=-1";
    private static final String JDBC_USER = "sa";
    private static final String JDBC_PASSWORD = "";

    static {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }

    public static void initializeDatabase() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            String createTableSQL = "CREATE TABLE IF NOT EXISTS USERS (" +
                    "ID INT AUTO_INCREMENT PRIMARY KEY, " +
                    "USERNAME VARCHAR(255) NOT NULL, " +
                    "PASSWORD VARCHAR(255) NOT NULL)";
            statement.execute(createTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean validateUser(String username, String password) {
        try (Connection connection = getConnection()) {
            String queryUserSQL = "SELECT * FROM USERS WHERE USERNAME = ? AND PASSWORD = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(queryUserSQL)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean registerUser(String username, String password) {
        try (Connection connection = getConnection()) {
            String insertUserSQL = "INSERT INTO USERS (USERNAME, PASSWORD) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertUserSQL)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}