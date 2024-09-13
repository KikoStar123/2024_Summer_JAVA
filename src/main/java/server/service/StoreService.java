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
 * 商店服务类，提供与商店相关的操作
 */
public class StoreService {

    private final Lock addStoreLock = new ReentrantLock();
    private final Lock updateStoreLock = new ReentrantLock();
    private final Lock deleteStoreLock = new ReentrantLock();
    private final Lock getStoreLock = new ReentrantLock();
    private final Lock getStoreByUsernameLock = new ReentrantLock();

    /**
     * 添加商店信息。
     * @param storeID 商店ID。
     * @param storeName 商店名称。
     * @param storePhone 商店电话。
     * @param storeRate 商店评分。
     * @param storeStatus 商店状态（开启/关闭）。
     * @return 添加是否成功。
     */
    public boolean addStore(String storeID, String storeName, String storePhone, float storeRate, boolean storeStatus) {
        addStoreLock.lock();
        try {
            boolean isSuccess = false;
            DatabaseConnection dbConnection = new DatabaseConnection();
            Connection conn = dbConnection.connect();

            if (conn == null) {
                return false;
            }

            String query = "INSERT INTO tblStore (storeID, storeName, storePhone, storeRate, storeStatus) VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, storeID);
                preparedStatement.setString(2, storeName);
                preparedStatement.setString(3, storePhone);
                preparedStatement.setFloat(4, storeRate);
                preparedStatement.setBoolean(5, storeStatus);

                int rowsAffected = preparedStatement.executeUpdate();
                isSuccess = rowsAffected > 0;
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
            addStoreLock.unlock();
        }
    }

    /**
     * 更新商店信息。
     * @param storeID 商店ID。
     * @param storeName 商店名称。
     * @param storePhone 商店电话。
     * @param storeRate 商店评分。
     * @param storeStatus 商店状态（开启/关闭）。
     * @return 更新是否成功。
     */
    public boolean updateStore(String storeID, String storeName, String storePhone, float storeRate, boolean storeStatus) {
        updateStoreLock.lock();
        try {
            boolean isSuccess = false;
            DatabaseConnection dbConnection = new DatabaseConnection();
            Connection conn = dbConnection.connect();

            if (conn == null) {
                return false;
            }

            String query = "UPDATE tblStore SET storeName = ?, storePhone = ?, storeRate = ?, storeStatus = ? WHERE storeID = ?";

            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, storeName);
                preparedStatement.setString(2, storePhone);
                preparedStatement.setFloat(3, storeRate);
                preparedStatement.setBoolean(4, storeStatus);
                preparedStatement.setString(5, storeID);

                int rowsAffected = preparedStatement.executeUpdate();
                isSuccess = rowsAffected > 0;
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
            updateStoreLock.unlock();
        }
    }

    /**
     * 删除商店。
     * @param storeID 商店ID。
     * @return 删除是否成功。
     */
    public boolean deleteStore(String storeID) {
        deleteStoreLock.lock();
        try {
            boolean isSuccess = false;
            DatabaseConnection dbConnection = new DatabaseConnection();
            Connection conn = dbConnection.connect();

            if (conn == null) {
                return false;
            }

            String query = "DELETE FROM tblStore WHERE storeID = ?";

            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, storeID);
                int rowsAffected = preparedStatement.executeUpdate();
                isSuccess = rowsAffected > 0;
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
            deleteStoreLock.unlock();
        }
    }

    /**
     * 获取商店详情。
     * @param storeID 商店ID。
     * @return 包含商店详情的 JSON 对象。
     */
    public JSONObject getStore(String storeID) {
        getStoreLock.lock();
        try {
            JSONObject response = new JSONObject();
            DatabaseConnection dbConnection = new DatabaseConnection();
            Connection conn = dbConnection.connect();

            if (conn == null) {
                response.put("status", "fail").put("message", "Database connection failed");
                return response;
            }

            String query = "SELECT * FROM tblStore WHERE storeID = ?";

            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, storeID);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    response.put("storeID", resultSet.getString("storeID"));
                    response.put("storeName", resultSet.getString("storeName"));
                    response.put("storePhone", resultSet.getString("storePhone"));
                    response.put("storeRate", resultSet.getFloat("storeRate"));
                    response.put("storeStatus", resultSet.getBoolean("storeStatus"));
                    response.put("status", "success");
                } else {
                    response.put("status", "fail").put("message", "Store not found");
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
        } finally {
            getStoreLock.unlock();
        }
    }

    /**
     * 获取所有商店信息。
     * @return 包含所有商店信息的 JSON 对象。
     */
    public JSONObject getAllStores() {
        getStoreLock.lock();
        try {
            JSONObject response = new JSONObject();
            JSONArray storesArray = new JSONArray();

            String query = "SELECT * FROM tblStore";

            DatabaseConnection dbConnection = new DatabaseConnection();
            Connection conn = dbConnection.connect();

            if (conn == null) {
                response.put("status", "fail").put("message", "Database connection failed");
                return response;
            }

            try (PreparedStatement preparedStatement = conn.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    JSONObject store = new JSONObject();
                    store.put("storeID", resultSet.getString("storeID"));
                    store.put("storeName", resultSet.getString("storeName"));
                    store.put("storePhone", resultSet.getString("storePhone"));
                    store.put("storeRate", resultSet.getFloat("storeRate"));
                    store.put("storeStatus", resultSet.getBoolean("storeStatus"));

                    storesArray.put(store);
                }

                response.put("status", "success").put("stores", storesArray);
            } catch (SQLException e) {
                e.printStackTrace();
                response.put("status", "fail").put("message", "SQL Error: " + e.getMessage());
            } finally {
                try {
                    if (conn != null) {
                        conn.close
                                ();
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }

            return response;
        } finally {
            getStoreLock.unlock();
        }
    }


    /**
     * 根据用户名获取商店ID。
     * @param username 用户名。
     * @return 商店ID，若未找到则返回 null。
     */
    public String getStoreIDByUsername(String username) {
        getStoreByUsernameLock.lock();
        try {
            String storeID = null;
            DatabaseConnection dbConnection = new DatabaseConnection();
            Connection conn = dbConnection.connect();

            if (conn == null) {
                return null;
            }

            String query = "SELECT storeID FROM tblStore WHERE username = ?";

            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    storeID = resultSet.getString("storeID");
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
            return storeID;
        } finally {
            getStoreByUsernameLock.unlock();
        }
    }

}
