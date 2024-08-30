package server.service;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ShoppingCartService {

    private final Lock getShoppingCartLock = new ReentrantLock();
    private final Lock addToCartLock = new ReentrantLock();
    private final Lock updateCartLock = new ReentrantLock();
    private final Lock removeFromCartLock = new ReentrantLock();

    // 获取一个用户的购物车
    public JSONObject getShoppingCart(String username) {
        getShoppingCartLock.lock();
        try {
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
        } finally {
            getShoppingCartLock.unlock();
        }
    }

    // 添加一个商品到购物车
    public boolean addToCart(String username, String productID, int quantity) {
        addToCartLock.lock();
        try {
            boolean isSuccess = false;
            DatabaseConnection dbConnection = new DatabaseConnection();
            Connection conn = dbConnection.connect();

            if (conn == null) {
                return false;
            }

            String query = "MERGE INTO tblShoppingCart AS target " +
                    "USING (VALUES (?, ?, ?)) AS source (username, productID, productNumber) " +
                    "ON target.username = source.username AND target.productID = source.productID " +
                    "WHEN MATCHED THEN " +
                    "  UPDATE SET target.productNumber = target.productNumber + source.productNumber " +
                    "WHEN NOT MATCHED THEN " +
                    "  INSERT (username, productID, productNumber) VALUES (source.username, source.productID, source.productNumber)";

            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, productID);
                preparedStatement.setInt(3, quantity);
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
        } finally {
            addToCartLock.unlock();
        }
    }

    // 更改购物车某个商品的数量
    public boolean updateCart(String username, String productID, int quantity) {
        updateCartLock.lock();
        try {
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
        } finally {
            updateCartLock.unlock();
        }
    }

    // 从购物车上面删除某个商品
    public boolean removeFromCart(String username, String productID) {
        removeFromCartLock.lock();
        try {
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
        } finally {
            removeFromCartLock.unlock();
        }
    }
}
