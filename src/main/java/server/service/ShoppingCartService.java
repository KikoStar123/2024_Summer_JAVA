package server.service;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
/**
 * 购物车服务类，提供与购物车相关的操作
 */
public class ShoppingCartService {

    private final Lock getShoppingCartLock = new ReentrantLock();
    private final Lock addToCartLock = new ReentrantLock();
    private final Lock updateCartLock = new ReentrantLock();
    private final Lock removeFromCartLock = new ReentrantLock();

    /**
     * 获取指定用户的购物车信息。
     * @param username 用户名。
     * @return 包含购物车信息的 JSON 对象。
     */
    public JSONObject getShoppingCart(String username) {
        getShoppingCartLock.lock();
        try {
            JSONObject response = new JSONObject();
            JSONArray cartItems = new JSONArray();

            // 修改查询，联接 tblShoppingCart 和 tblShoppingProduct 获取 storeID
            String query = "SELECT c.productID, c.productNumber, p.storeID " +
                    "FROM tblShoppingCart c " +
                    "JOIN tblShoppingProduct p ON c.productID = p.productID " +
                    "WHERE c.username = ?";

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
                    cartItem.put("storeID", resultSet.getString("storeID"));  // 新增 storeID 字段

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


    /**
     * 向购物车添加商品。
     * @param username 用户名。
     * @param productID 商品ID。
     * @param quantity 添加的商品数量。
     * @return 添加是否成功。
     */
    public boolean addToCart(String username, String productID, int quantity) {
        addToCartLock.lock();
        try {
            boolean isSuccess = false;
            DatabaseConnection dbConnection = new DatabaseConnection();
            Connection conn = dbConnection.connect();

            if (conn == null) {
                return false;
            }

            // 首先检查商品库存是否足够
            String checkInventoryQuery = "SELECT productInventory FROM tblShoppingProduct WHERE productID = ?";
            try (PreparedStatement checkStatement = conn.prepareStatement(checkInventoryQuery)) {
                checkStatement.setString(1, productID);
                ResultSet resultSet = checkStatement.executeQuery();
                if (resultSet.next()) {
                    int availableInventory = resultSet.getInt("productInventory");
                    if (quantity > availableInventory) {
                        // 商品数量超过库存，返回false
                        return false;
                    }
                } else {
                    // 商品未找到，返回false
                    return false;
                }
            } catch (SQLException e) {
                e.printStackTrace();
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


    /**
     * 更新购物车中商品的数量。
     * @param username 用户名。
     * @param productID 商品ID。
     * @param quantity 更新后的商品数量。
     * @return 更新是否成功。
     */
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

    /**
     * 从购物车中删除商品。
     * @param username 用户名。
     * @param productID 商品ID。
     * @return 删除是否成功。
     */
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
