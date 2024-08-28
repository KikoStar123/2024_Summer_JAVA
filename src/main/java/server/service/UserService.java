package server.service;

import org.json.JSONObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserService {

    public boolean login(String username, String password) {
        boolean isAuthenticated = false;
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn == null) {
            System.out.println("Failed to connect to the database.");
            return isAuthenticated;
        }

        String query = "SELECT COUNT(*) FROM USERS WHERE USERNAME = ? AND PASSWORD = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            // 输出接收到的用户名和密码
            System.out.println("Checking login for username: " + username + " with password: " + password);

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    isAuthenticated = true;
                    System.out.println("User authenticated successfully.");
                } else {
                    System.out.println("Authentication failed.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }

        return isAuthenticated;
    }
    public JSONObject loginReturn(String username, String password) {
        JSONObject userJson = null;
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn == null) {
            System.out.println("Failed to connect to the database.");
            return null;
        }

        String query = "SELECT * FROM USERS WHERE USERNAME = ? AND PASSWORD = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            // 输出接收到的用户名和密码
            System.out.println("Checking login for username: " + username + " with password: " + password);

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("User authenticated successfully.");

                    // 构建用户信息的JSON对象
                    userJson = new JSONObject();
                    userJson.put("id", resultSet.getInt("id"));
                    userJson.put("username", resultSet.getString("username"));
                    userJson.put("password", resultSet.getString("password"));
                    userJson.put("age", resultSet.getInt("age"));
                    userJson.put("role", resultSet.getString("role"));
                    userJson.put("gender", resultSet.getString("gender"));
                } else {
                    System.out.println("Authentication failed.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }

        return userJson;
    }

    public String logout(JSONObject parameters) {
        JSONObject jsonResponse = new JSONObject();

        try {
            String username = parameters.getString("username");
            if (username != null && !username.isEmpty()) {
                jsonResponse.put("status", "success").put("message", "Logout successful");
                System.out.println("Logout successful for username: " + username);
            } else {
                jsonResponse.put("status", "fail").put("message", "Invalid username");
                System.out.println("Logout failed: Invalid username");
            }
        } catch (Exception e) {
            jsonResponse.put("status", "error").put("message", "An error occurred during logout");
            System.out.println("An error occurred during logout: " + e.getMessage());
        }

        return jsonResponse.toString();
    }
}