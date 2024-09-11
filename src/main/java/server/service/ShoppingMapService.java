package server.service;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ShoppingMapService {

    private final Lock mapLock = new ReentrantLock();

    // 增加一条记录
    public JSONObject addMapRecord(String productID, String mapStart, String mapEnd) {
        mapLock.lock();
        JSONObject response = new JSONObject();
        try {
            String query = "INSERT INTO tblMap (orderID, mapStart, mapEnd) VALUES (?, ?, ?)";

            DatabaseConnection dbConnection = new DatabaseConnection();
            Connection conn = dbConnection.connect();

            if (conn == null) {
                response.put("status", "fail").put("message", "数据库连接失败");
                return response;
            }

            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, productID);
                preparedStatement.setString(2, mapStart);
                preparedStatement.setString(3, mapEnd);

                int rowsInserted = preparedStatement.executeUpdate();
                if (rowsInserted > 0) {
                    response.put("status", "success").put("message", "记录添加成功");
                } else {
                    response.put("status", "fail").put("message", "记录添加失败");
                }
            } finally {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("status", "fail").put("message", "SQL错误: " + e.getMessage());
        } finally {
            mapLock.unlock();
        }
        return response;
    }

    // 删除一条记录
    public JSONObject deleteMapRecord(String productID) {
        mapLock.lock();
        JSONObject response = new JSONObject();
        try {
            String query = "DELETE FROM tblMap WHERE orderID = ?";

            DatabaseConnection dbConnection = new DatabaseConnection();
            Connection conn = dbConnection.connect();

            if (conn == null) {
                response.put("status", "fail").put("message", "数据库连接失败");
                return response;
            }

            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, productID);

                int rowsDeleted = preparedStatement.executeUpdate();
                if (rowsDeleted > 0) {
                    response.put("status", "success").put("message", "记录删除成功");
                } else {
                    response.put("status", "fail").put("message", "记录删除失败");
                }
            } finally {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("status", "fail").put("message", "SQL错误: " + e.getMessage());
        } finally {
            mapLock.unlock();
        }
        return response;
    }

    // 更新一条记录
    public JSONObject updateMapRecord(String productID, String mapStart, String mapEnd) {
        mapLock.lock();
        JSONObject response = new JSONObject();
        try {
            String query = "UPDATE tblMap SET mapStart = ?, mapEnd = ? WHERE orderID = ?";

            DatabaseConnection dbConnection = new DatabaseConnection();
            Connection conn = dbConnection.connect();

            if (conn == null) {
                response.put("status", "fail").put("message", "数据库连接失败");
                return response;
            }

            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, mapStart);
                preparedStatement.setString(2, mapEnd);
                preparedStatement.setString(3, productID);

                int rowsUpdated = preparedStatement.executeUpdate();
                if (rowsUpdated > 0) {
                    response.put("status", "success").put("message", "记录更新成功");
                } else {
                    response.put("status", "fail").put("message", "记录更新失败");
                }
            } finally {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("status", "fail").put("message", "SQL错误: " + e.getMessage());
        } finally {
            mapLock.unlock();
        }
        return response;
    }

    // 查询所有记录
    public JSONObject getAllMapRecords() {
        mapLock.lock();
        JSONObject response = new JSONObject();
        JSONArray recordsArray = new JSONArray();
        try {
            String query = "SELECT * FROM tblMap";

            DatabaseConnection dbConnection = new DatabaseConnection();
            Connection conn = dbConnection.connect();

            if (conn == null) {
                response.put("status", "fail").put("message", "数据库连接失败");
                return response;
            }

            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    JSONObject record = new JSONObject();
                    record.put("productID", resultSet.getString("productID"));
                    record.put("mapStart", resultSet.getString("mapStart"));
                    record.put("mapEnd", resultSet.getString("mapEnd"));

                    recordsArray.put(record);  // 将每条记录添加到记录数组中
                }

                // 添加 status 和 records 到响应中
                response.put("status", "success");
                response.put("records", recordsArray);  // 确保 records 字段存在
            } finally {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("status", "fail").put("message", "SQL错误: " + e.getMessage());
        } finally {
            mapLock.unlock();
        }
        return response;
    }

    // 根据productID获取起始位置和终止位置
    public JSONObject getMapRecordByProductID(String productID) {
        mapLock.lock();
        JSONObject response = new JSONObject();
        try {
            String query = "SELECT mapStart, mapEnd FROM tblMap WHERE orderID = ?";

            DatabaseConnection dbConnection = new DatabaseConnection();
            Connection conn = dbConnection.connect();

            if (conn == null) {
                response.put("status", "fail").put("message", "数据库连接失败");
                return response;
            }

            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, productID);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    response.put("status", "success");
                    response.put("mapStart", resultSet.getString("mapStart"));
                    response.put("mapEnd", resultSet.getString("mapEnd"));
                } else {
                    response.put("status", "fail").put("message", "记录未找到");
                }
            } finally {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("status", "fail").put("message", "SQL错误: " + e.getMessage());
        } finally {
            mapLock.unlock();
        }
        return response;
    }


}
