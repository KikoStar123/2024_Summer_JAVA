package server.service;

import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ShoppingOrderService {

    public boolean createOrder(String username, String productID, int productNumber, float paidMoney) {
        boolean isSuccess = false;
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn == null) {
            return false;
        }

        String orderID = generateOrderID();

        String query = "INSERT INTO tblShoppingOrder (orderID, username, productID, productNumber, whetherComment, paidMoney) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, orderID);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, productID);
            preparedStatement.setInt(4, productNumber);
            preparedStatement.setBoolean(5, false); // whetherComment 默认为 false (0)
            preparedStatement.setFloat(6, paidMoney);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                isSuccess = true;
            }
        } catch (SQLException e) {
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

        return isSuccess;
    }

    private String generateOrderID() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        String datePart = sdf.format(new Date());
        String uniquePart = String.format("%04d", (int) (Math.random() * 10000)); // 生成四位随机数

        return datePart + uniquePart;
    }

    public JSONObject getOrderDetails(String orderID) {
        JSONObject response = new JSONObject();
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn == null) {
            response.put("status", "fail").put("message", "Database connection failed");
            return response;
        }

        String query = "SELECT * FROM tblShoppingOrder WHERE orderID = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, orderID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                response.put("orderID", resultSet.getString("orderID"));
                response.put("username", resultSet.getString("username"));
                response.put("productID", resultSet.getString("productID"));
                response.put("productNumber", resultSet.getInt("productNumber"));
                response.put("whetherComment", resultSet.getBoolean("whetherComment"));
                response.put("paidMoney", resultSet.getFloat("paidMoney"));
                response.put("status", "success");
            } else {
                response.put("status", "fail").put("message", "Order not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("status", "fail").put("message", "SQL Error: " + e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }

        return response;
    }

    public boolean updateOrderCommentStatus(String orderID, boolean whetherComment) {
        boolean isSuccess = false;
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn == null) {
            return false;
        }

        String query = "UPDATE tblShoppingOrder SET whetherComment = ? WHERE orderID = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setBoolean(1, whetherComment);
            preparedStatement.setString(2, orderID);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                isSuccess = true;
            }
        } catch (SQLException e) {
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

        return isSuccess;
    }
}
