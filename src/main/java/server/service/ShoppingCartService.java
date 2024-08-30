package server.service;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ShoppingCartService {

    //获取一个用户的购物车
    public JSONObject getShoppingCart(String username) {
        JSONObject response = new JSONObject();
        JSONArray cartItems = new JSONArray();

        String query = "SELECT * FROM tblShoppingCart WHERE username = ?";

        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn == null) {
            response.put("status", "fail").put("message", "Database connection failed");
            return response;
        }

        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                JSONObject cartItem = new JSONObject();
                cartItem.put("productID", resultSet.getString("productID"));
                cartItem.put("productNumber", resultSet.getInt("productNumber"));
                cartItems.put(cartItem);
            }

            response.put("status", "success").put("cartItems", cartItems);
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

    //添加一个商品到购物车
    public boolean addToCart(String username, String productID, int quantity) {
        boolean isSuccess = false;
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn == null) {
            return false;
        }

        String query = "INSERT INTO tblShoppingCart (username, productID, productNumber) VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE productNumber = productNumber + ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, productID);
            preparedStatement.setInt(3, quantity);
            preparedStatement.setInt(4, quantity);
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

    //添加购物车某个商品的数量
    public boolean updateCart(String username, String productID, int quantity) {
        boolean isSuccess = false;
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn == null) {
            return false;
        }

        String query = "UPDATE tblShoppingCart SET productNumber = ? WHERE username = ? AND productID = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, quantity);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, productID);
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

    //从购物车上面删除某个商品
    public boolean removeFromCart(String username, String productID) {
        boolean isSuccess = false;
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn == null) {
            return false;
        }

        String query = "DELETE FROM tblShoppingCart WHERE username = ? AND productID = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, productID);
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
