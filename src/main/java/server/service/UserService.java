package server.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.json.JSONObject;

public class UserService {

    private static final String JDBC_URL = "http://localhost:8082/h2-console";  // 修改为你实际的H2数据库路径
    private static final String JDBC_USER = "sa";
    private static final String JDBC_PASSWORD = "";

    public boolean login(String username, String password) {
        boolean isAuthenticated = false;

        String query = "SELECT COUNT(*) FROM USERS WHERE USERNAME = ? AND PASSWORD = ?";

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    isAuthenticated = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isAuthenticated;
    }

    public String logout(JSONObject parameters) {
        JSONObject jsonResponse = new JSONObject();

        try {
            String username = parameters.getString("username");
            if (username != null && !username.isEmpty()) {
                jsonResponse.put("status", "success").put("message", "Logout successful");
            } else {
                jsonResponse.put("status", "fail").put("message", "Invalid username");
            }
        } catch (Exception e) {
            jsonResponse.put("status", "error").put("message", "An error occurred during logout");
        }

        return jsonResponse.toString();
    }
}