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
 * 商店用户服务类，提供与商店用户相关的操作
 */
public class ShoppingUserService {

    private final Lock userLock = new ReentrantLock();

    /**
     * 获取用户的所有地址和电话信息。
     * @param username 用户名。
     * @return 包含用户地址和电话信息的 JSON 对象。
     */
    public JSONObject getUserDetails(String username) {
        userLock.lock();
        JSONObject response = new JSONObject();
        try {
            String query = "SELECT address, telephone FROM tblShoppingUser WHERE username = ?";

            DatabaseConnection dbConnection = new DatabaseConnection();
            Connection conn = dbConnection.connect();

            if (conn == null) {
                response.put("status", "fail").put("message", "数据库连接失败");
                return response;
            }

            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    // 获取地址和电话的字符串，用分隔符 ';' 进行分割
                    String[] addresses = resultSet.getString("address").split(";");
                    String[] telephones = resultSet.getString("telephone").split(";");

                    JSONArray addressArray = new JSONArray();
                    JSONArray telephoneArray = new JSONArray();

                    for (String address : addresses) {
                        addressArray.put(address.trim());
                    }
                    for (String telephone : telephones) {
                        telephoneArray.put(telephone.trim());
                    }

                    response.put("status", "success");
                    response.put("addresses", addressArray);
                    response.put("telephones", telephoneArray);
                } else {
                    response.put("status", "fail").put("message", "未找到该用户");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                response.put("status", "fail").put("message", "SQL错误: " + e.getMessage());
            } finally {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            userLock.unlock();
        }
        return response;
    }

    /**
     * 更新用户的地址和电话。
     * @param username 用户名。
     * @param addresses 地址列表。
     * @param telephones 电话列表。
     * @return 更新是否成功的 JSON 对象。
     */
    public JSONObject updateUserContacts(String username, String[] addresses, String[] telephones) {
        userLock.lock();
        JSONObject response = new JSONObject();
        try {
            // 将地址和电话数组转换为分隔符分隔的字符串
            String addressString = String.join(";", addresses);
            String telephoneString = String.join(";", telephones);

            // 更新数据库中的地址和电话
            String query = "UPDATE tblShoppingUser SET address = ?, telephone = ? WHERE username = ?";

            DatabaseConnection dbConnection = new DatabaseConnection();
            Connection conn = dbConnection.connect();

            if (conn == null) {
                response.put("status", "fail").put("message", "数据库连接失败");
                return response;
            }

            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, addressString);
                preparedStatement.setString(2, telephoneString);
                preparedStatement.setString(3, username);

                int rowsUpdated = preparedStatement.executeUpdate();
                if (rowsUpdated > 0) {
                    response.put("status", "success").put("message", "用户地址和电话更新成功");
                } else {
                    response.put("status", "fail").put("message", "未找到用户");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                response.put("status", "fail").put("message", "SQL错误: " + e.getMessage());
            } finally {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            userLock.unlock();
        }
        return response;
    }

    /**
     * 删除用户的某一行地址和电话。
     * @param username 用户名。
     * @param index 要删除的地址和电话的索引。
     * @return 删除是否成功的 JSON 对象。
     */
    public JSONObject deleteUserContact(String username, int index) {
        userLock.lock();
        JSONObject response = new JSONObject();
        try {
            String query = "SELECT address, telephone FROM tblShoppingUser WHERE username = ?";

            DatabaseConnection dbConnection = new DatabaseConnection();
            Connection conn = dbConnection.connect();

            if (conn == null) {
                response.put("status", "fail").put("message", "数据库连接失败");
                return response;
            }

            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    // 获取当前的地址和电话
                    String[] addresses = resultSet.getString("address").split(";");
                    String[] telephones = resultSet.getString("telephone").split(";");

                    if (index < 0 || index >= addresses.length || index >= telephones.length) {
                        response.put("status", "fail").put("message", "索引无效");
                        return response;
                    }

                    // 删除指定索引的地址和电话
                    addresses = removeElement(addresses, index);
                    telephones = removeElement(telephones, index);

                    // 更新数据库
                    response = updateUserContacts(username, addresses, telephones);
                } else {
                    response.put("status", "fail").put("message", "未找到用户");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                response.put("status", "fail").put("message", "SQL错误: " + e.getMessage());
            } finally {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            userLock.unlock();
        }
        return response;
    }

    private String[] removeElement(String[] array, int index) {
        if (index < 0 || index >= array.length) {
            return array; // 返回原数组，若索引无效
        }
        String[] newArray = new String[array.length - 1];
        for (int i = 0, j = 0; i < array.length; i++) {
            if (i == index) {
                continue; // 跳过要删除的元素
            }
            newArray[j++] = array[i];
        }
        return newArray;
    }

    /**
     * 添加用户新的地址和电话。
     * @param username 用户名。
     * @param newAddress 新的地址。
     * @param newTelephone 新的电话。
     * @return 添加是否成功的 JSON 对象。
     */
    public JSONObject addUserContact(String username, String newAddress, String newTelephone) {
        userLock.lock();
        JSONObject response = new JSONObject();
        try {
            String query = "SELECT address, telephone FROM tblShoppingUser WHERE username = ?";

            DatabaseConnection dbConnection = new DatabaseConnection();
            Connection conn = dbConnection.connect();

            if (conn == null) {
                response.put("status", "fail").put("message", "数据库连接失败");
                return response;
            }

            String currentAddresses = "";
            String currentTelephones = "";

            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    // 获取现有的地址和电话
                    currentAddresses = resultSet.getString("address");
                    currentTelephones = resultSet.getString("telephone");
                } else {
                    response.put("status", "fail").put("message", "未找到用户");
                    return response;
                }
            }

            // 将新的地址和电话附加到现有的地址和电话后，用分隔符 ";" 分隔
            String updatedAddresses = currentAddresses.isEmpty() ? newAddress : currentAddresses + ";" + newAddress;
            String updatedTelephones = currentTelephones.isEmpty() ? newTelephone : currentTelephones + ";" + newTelephone;

            // 更新数据库
            String updateQuery = "UPDATE tblShoppingUser SET address = ?, telephone = ? WHERE username = ?";
            try (PreparedStatement updateStatement = conn.prepareStatement(updateQuery)) {
                updateStatement.setString(1, updatedAddresses);
                updateStatement.setString(2, updatedTelephones);
                updateStatement.setString(3, username);

                int rowsUpdated = updateStatement.executeUpdate();

                if (rowsUpdated > 0) {
                    response.put("status", "success").put("message", "新的地址和电话添加成功");
                } else {
                    response.put("status", "fail").put("message", "用户未找到");
                }
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("status", "fail").put("message", "SQL错误: " + e.getMessage());
        } finally {
            userLock.unlock();
        }
        return response;
    }

    /**
     * 更新用户的某个地址和电话（根据索引）。
     * @param username 用户名。
     * @param index 要更新的地址和电话的索引。
     * @param newAddress 新的地址。
     * @param newTelephone 新的电话。
     * @return 更新是否成功的 JSON 对象。
     */
    public JSONObject updateUserContactAtIndex(String username, int index, String newAddress, String newTelephone) {
        userLock.lock();
        JSONObject response = new JSONObject();
        try {
            String query = "SELECT address, telephone FROM tblShoppingUser WHERE username = ?";

            DatabaseConnection dbConnection = new DatabaseConnection();
            Connection conn = dbConnection.connect();

            if (conn == null) {
                response.put("status", "fail").put("message", "数据库连接失败");
                return response;
            }

            String[] addresses = null;
            String[] telephones = null;

            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    // 获取当前的地址和电话
                    addresses = resultSet.getString("address").split(";");
                    telephones = resultSet.getString("telephone").split(";");
                } else {
                    response.put("status", "fail").put("message", "未找到用户");
                    return response;
                }
            }

            // 检查索引是否有效
            if (index < 0 || index >= addresses.length || index >= telephones.length) {
                response.put("status", "fail").put("message", "索引无效");
                return response;
            }

            // 更新指定索引的地址和电话
            addresses[index] = newAddress;
            telephones[index] = newTelephone;

            // 将更新后的地址和电话列表重新拼接为分隔符分隔的字符串
            String updatedAddresses = String.join(";", addresses);
            String updatedTelephones = String.join(";", telephones);

            // 更新数据库
            String updateQuery = "UPDATE tblShoppingUser SET address = ?, telephone = ? WHERE username = ?";
            try (PreparedStatement updateStatement = conn.prepareStatement(updateQuery)) {
                updateStatement.setString(1, updatedAddresses);
                updateStatement.setString(2, updatedTelephones);
                updateStatement.setString(3, username);

                int rowsUpdated = updateStatement.executeUpdate();
                if (rowsUpdated > 0) {
                    response.put("status", "success").put("message", "地址和电话更新成功");
                } else {
                    response.put("status", "fail").put("message", "未找到用户");
                }
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("status", "fail").put("message", "SQL错误: " + e.getMessage());
        } finally {
            userLock.unlock();
        }
        return response;
    }



}
