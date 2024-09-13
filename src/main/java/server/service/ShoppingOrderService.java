package server.service;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.io.*;
import java.net.Socket;
/**
 * 订单服务类，提供与订单相关的操作
 */
public class ShoppingOrderService {

    private final Lock createOrderLock = new ReentrantLock();
    private final Lock getOrderDetailsLock = new ReentrantLock();
    private final Lock updateOrderCommentStatusLock = new ReentrantLock();
    private final Lock getOrderCommentStatusLock = new ReentrantLock();
    private final Lock payOrderLock = new ReentrantLock();

    /**
     * 创建订单。
     * @param username 用户名。
     * @param productID 商品ID。
     * @param productName 商品名称。
     * @param productNumber 商品数量。
     * @param paidMoney 支付金额。
     * @return 包含订单状态和订单ID的 JSON 对象。
     */
    public JSONObject createOrder(String username, String productID, String productName, int productNumber, float paidMoney) {
        createOrderLock.lock();
        JSONObject response = new JSONObject();
        try {
            boolean isSuccess = false;
            DatabaseConnection dbConnection = new DatabaseConnection();
            Connection conn = dbConnection.connect();

            if (conn == null) {
                response.put("status", "fail");
                response.put("message", "Database connection failed");
                return response;
            }

            String orderID = generateOrderID();

            // 获取 storeID 关联商品
            String storeID = getStoreIDByProductID(productID);

            // 插入时添加 storeID
            String query = "INSERT INTO tblShoppingOrder (orderID, username, productID, productName, productNumber, whetherComment, paidMoney, paidStatus, storeID) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, orderID);
                preparedStatement.setString(2, username);
                preparedStatement.setString(3, productID);
                preparedStatement.setString(4, productName);
                preparedStatement.setInt(5, productNumber);
                preparedStatement.setBoolean(6, false);  // whetherComment 默认为 false (0)
                preparedStatement.setFloat(7, paidMoney);
                preparedStatement.setBoolean(8, false);  // paidStatus 默认为 false (未支付)
                preparedStatement.setString(9, storeID); // 添加 storeID

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    isSuccess = true;
                    response.put("status", "success").put("orderID", orderID);
                } else {
                    response.put("status", "fail").put("message", "Order creation failed");
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
            System.out.println("create order: " + response.toString());
            return response;
        } finally {
            createOrderLock.unlock();
        }
    }
    /**
     * 根据商品ID获取商店ID。
     * @param productID 商品ID。
     * @return 商店ID。
     */
    private String getStoreIDByProductID(String productID) {
        String storeID = null;
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        String query = "SELECT storeID FROM tblShoppingProduct WHERE productID = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, productID);
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
    }

    /**
     * 根据商店ID获取所有订单。
     * @param storeID 商店ID。
     * @return 包含订单信息的 JSON 对象。
     */
    public JSONObject getAllOrdersByStore(String storeID) {
        getOrderDetailsLock.lock();
        try {
            JSONObject response = new JSONObject();
            JSONArray ordersArray = new JSONArray();
            DatabaseConnection dbConnection = new DatabaseConnection();
            Connection conn = dbConnection.connect();

            if (conn == null) {
                response.put("status", "fail").put("message", "Database connection failed");
                return response;
            }

            String query = "SELECT * FROM tblShoppingOrder WHERE storeID = ?";

            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, storeID);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    JSONObject order = new JSONObject();
                    order.put("orderID", resultSet.getString("orderID"));
                    order.put("username", resultSet.getString("username"));
                    order.put("productID", resultSet.getString("productID"));
                    order.put("productName", resultSet.getString("productName"));
                    order.put("productNumber", resultSet.getInt("productNumber"));
                    order.put("whetherComment", resultSet.getBoolean("whetherComment"));
                    order.put("paidMoney", resultSet.getFloat("paidMoney"));
                    order.put("paidStatus", resultSet.getBoolean("paidStatus"));
                    order.put("storeID", resultSet.getString("storeID"));
                    ordersArray.put(order);
                }

                response.put("status", "success").put("orders", ordersArray);
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
            getOrderDetailsLock.unlock();
        }
    }



    /**
     * 更新订单支付状态。
     * @param orderID 订单ID。
     * @param paidStatus 支付状态。
     * @return 更新状态是否成功。
     */
    public boolean updateOrderPaidStatus(String orderID, boolean paidStatus) {
        boolean isSuccess = false;
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn == null) {
            return false;
        }

        String query = "UPDATE tblShoppingOrder SET paidStatus = ? WHERE orderID = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setBoolean(1, paidStatus);
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



    /**
     * 生成订单ID。
     * @return 生成的订单ID。
     */
    private String generateOrderID() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        String datePart = sdf.format(new Date());
        String uniquePart = String.format("%04d", (int) (Math.random() * 10000)); // 生成四位随机数

        return datePart + uniquePart;
    }


    //---------------------------------------------------------------------------------------------------

    /**
     * 获取用户的所有订单。
     * @param username 用户名。
     * @return 包含订单信息的 JSON 对象。
     */
    public JSONObject getAllOrdersByUser(String username) {
        return searchOrders(username, null);
    }

    /**
     * 根据关键词搜索用户订单。
     * @param username 用户名。
     * @param searchTerm 搜索关键词。
     * @return 包含订单信息的 JSON 对象。
     */
    public JSONObject searchOrdersByUser(String username, String searchTerm) {
        return searchOrders(username, searchTerm);
    }

    /**
     * 在所有订单中根据关键词搜索。
     * @param searchTerm 搜索关键词。
     * @return 包含订单信息的 JSON 对象。
     */
    public JSONObject searchOrdersByKeyword(String searchTerm) {
        return searchOrders(null, searchTerm);
    }

    /**
     * 获取所有用户的订单。
     * @return 包含订单信息的 JSON 对象。
     */
    public JSONObject getAllOrders() {
        return searchOrders(null, null);
    }

    //通用搜索方法
    private JSONObject searchOrders(String username, String searchTerm) {
        getOrderDetailsLock.lock();
        try {
            JSONObject response = new JSONObject();
            JSONArray ordersArray = new JSONArray();
            DatabaseConnection dbConnection = new DatabaseConnection();
            Connection conn = dbConnection.connect();

            if (conn == null) {
                response.put("status", "fail").put("message", "Database connection failed");
                return response;
            }

            StringBuilder queryBuilder = new StringBuilder("SELECT * FROM tblShoppingOrder WHERE 1=1");

            if (username != null && !username.isEmpty()) {
                queryBuilder.append(" AND username = ?");
            }
            if (searchTerm != null && !searchTerm.isEmpty()) {
                queryBuilder.append(" AND (productID LIKE ? OR orderID LIKE ? OR productName LIKE ?)");
            }

            try (PreparedStatement preparedStatement = conn.prepareStatement(queryBuilder.toString())) {
                int paramIndex = 1;

                if (username != null && !username.isEmpty()) {
                    preparedStatement.setString(paramIndex++, username);
                }
                if (searchTerm != null && !searchTerm.isEmpty()) {
                    String searchPattern = "%" + searchTerm + "%";
                    preparedStatement.setString(paramIndex++, searchPattern);
                    preparedStatement.setString(paramIndex++, searchPattern);
                    preparedStatement.setString(paramIndex++, searchPattern);
                }

                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    JSONObject order = new JSONObject();
                    order.put("orderID", resultSet.getString("orderID"));
                    order.put("username", resultSet.getString("username"));
                    order.put("productID", resultSet.getString("productID"));
                    order.put("productName", resultSet.getString("productName"));  // 返回 productName
                    order.put("productNumber", resultSet.getInt("productNumber"));
                    order.put("whetherComment", resultSet.getBoolean("whetherComment"));
                    order.put("paidMoney", resultSet.getFloat("paidMoney"));
                    order.put("paidStatus", resultSet.getBoolean("paidStatus"));
                    order.put("storeID", resultSet.getString("storeID"));

                    ordersArray.put(order);
                }

                response.put("status", "success").put("orders", ordersArray);
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
            getOrderDetailsLock.unlock();
        }
    }



    //---------------------------------------------------------------------------------------------------


    // 用订单编号获取订单详情

    /**
     * 获取订单详情。
     * @param orderID 订单ID。
     * @return 包含订单详细信息的 JSON 对象。
     */
    public JSONObject getOrderDetails(String orderID) {
        getOrderDetailsLock.lock();
        try {
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
                    response.put("productName", resultSet.getString("productName"));  // 返回 productName
                    response.put("productNumber", resultSet.getInt("productNumber"));
                    response.put("whetherComment", resultSet.getBoolean("whetherComment"));
                    response.put("paidMoney", resultSet.getFloat("paidMoney"));
                    response.put("paidStatus", resultSet.getBoolean("paidStatus")); // 返回支付状态
                    response.put("storeID", resultSet.getString("storeID"));
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
        } finally {
            getOrderDetailsLock.unlock();
        }
    }


    /**
     * 更新订单评论状态。
     * @param orderID 订单ID。
     * @param whetherComment 是否已评论。
     * @return 更新状态是否成功。
     */
    public boolean updateOrderCommentStatus(String orderID, boolean whetherComment) {
        updateOrderCommentStatusLock.lock();
        try {
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
        } finally {
            updateOrderCommentStatusLock.unlock();
        }
    }
    /**
     * 获取订单评论状态。
     * @param orderID 订单ID。
     * @return 包含评论状态的 JSON 对象。
     */
    public JSONObject getOrderCommentStatus(String orderID) {
        getOrderCommentStatusLock.lock();
        try {
            JSONObject response = new JSONObject();
            DatabaseConnection dbConnection = new DatabaseConnection();
            Connection conn = dbConnection.connect();

            if (conn == null) {
                response.put("status", "fail").put("message", "Database connection failed");
                return response;
            }

            String query = "SELECT whetherComment FROM tblShoppingOrder WHERE orderID = ?";

            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, orderID);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    boolean whetherComment = resultSet.getBoolean("whetherComment");
                    response.put("status", "success");
                    response.put("orderID", orderID);
                    response.put("whetherComment", whetherComment);
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
        } finally {
            getOrderCommentStatusLock.unlock();
        }
    }

    //------------------------------------------------------------------------------------------------------//

    /**
     * 支付订单。
     * @param orderIDs 订单ID数组。
     * @param amount 支付金额。
     * @return 包含支付结果的 JSON 对象。
     */
    public JSONObject payOrder(String[] orderIDs, double amount) {
        payOrderLock.lock();
        JSONObject response = new JSONObject();
        try {
            boolean isPaid = false;
            String serverAddress = "localhost";  // 可以使用常量来定义
            int serverPort = 8080;

            try (Socket socket = new Socket(serverAddress, serverPort)) {
                // 构建请求
                JSONObject request = new JSONObject();
                request.put("requestType", "wait");
                request.put("parameters", new JSONObject()
                        .put("orderID", orderIDs[0]) // 只传递第一个订单ID给银行
                        .put("amount", amount));

                // 发送请求到银行服务器
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.println(request.toString());

                // 读取银行服务器的响应
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String responseFromBank = in.readLine();
                JSONObject jsonResponse = new JSONObject(responseFromBank);

                // 解析银行返回的结果
                if ("success".equalsIgnoreCase(jsonResponse.getString("status"))) {
                    isPaid = true;  // 支付成功
                    // 支付成功后，更新所有订单的支付状态
                    for (String orderID : orderIDs) {
                        updateOrderPaidStatus(orderID, true);
                    }
                    response.put("status", "success");
                    response.put("message", "Payment successful");
                } else {
                    response.put("status", "fail");
                    response.put("message", jsonResponse.getString("message"));
                }
            } catch (IOException e) {
                e.printStackTrace();
                response.put("status", "fail");
                response.put("message", "Payment system error: " + e.getMessage());
            }

            return response;
        } finally {
            payOrderLock.unlock();
        }
    }




    //------------------------------------------------------------------------------------------------------//


}



