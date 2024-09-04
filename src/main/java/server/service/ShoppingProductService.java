package server.service;

import client.service.ShoppingProduct;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.UUID;


public class ShoppingProductService {

    private final Lock getAllProductsLock = new ReentrantLock();
    private final Lock getProductDetailsLock = new ReentrantLock();
    private final Lock searchProductsLock = new ReentrantLock();
    private final Lock addProductLock = new ReentrantLock();
    private final Lock deleteProductLock = new ReentrantLock();
    private final Lock updateProductStatusLock = new ReentrantLock();
    private final Lock getSameCategoryProductsLock = new ReentrantLock();
    private final Lock updateProductPriceLock = new ReentrantLock();
    private final Lock updateProductInventoryLock = new ReentrantLock();
    private final Lock getProductCommentsLock = new ReentrantLock();
    private final Lock addCommentLock = new ReentrantLock();




    // 获取所有的商品
    public JSONObject getAllProducts(String sortBy, String sortOrder) {
        getAllProductsLock.lock();
        try {
            JSONObject response = new JSONObject();
            JSONArray productsArray = new JSONArray();

            // 确定排序列和顺序
            String orderByColumn;
            switch (sortBy) {
                case "price":
                    orderByColumn = "productCurrentPrice";
                    break;
                case "rate":
                    orderByColumn = "productCommentRate";
                    break;
                default:
                    response.put("status", "fail").put("message", "无效的排序参数");
                    return response;
            }

            String order = "ASC".equalsIgnoreCase(sortOrder) ? "ASC" : "DESC";
            String query = "SELECT * FROM tblShoppingProduct WHERE productStatus = true ORDER BY " + orderByColumn + " " + order;

            DatabaseConnection dbConnection = new DatabaseConnection();
            Connection conn = dbConnection.connect();

            if (conn == null) {
                response.put("status", "fail").put("message", "数据库连接失败");
                return response;
            }

            try (PreparedStatement preparedStatement = conn.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    JSONObject product = new JSONObject();
                    product.put("productID", resultSet.getString("productID"));
                    product.put("productName", resultSet.getString("productName"));
                    product.put("productDetail", resultSet.getString("productDetail"));
                    product.put("productImage", resultSet.getString("productImage"));
                    product.put("productOriginalPrice", resultSet.getFloat("productOriginalPrice"));
                    product.put("productCurrentPrice", resultSet.getFloat("productCurrentPrice"));
                    product.put("productInventory", resultSet.getInt("productInventory"));
                    product.put("productAddress", resultSet.getString("productAddress"));
                    product.put("productCommentRate", resultSet.getFloat("productCommentRate"));
                    product.put("productStatus", resultSet.getBoolean("productStatus"));


                    productsArray.put(product);
                }

                response.put("status", "success").put("products", productsArray);
                System.out.println(response.toString());
            } catch (SQLException e) {
                e.printStackTrace();
                response.put("status", "fail").put("message", "SQL错误: " + e.getMessage());
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
            getAllProductsLock.unlock();
        }
    }


    // 根据商品ID查询商品详情
    public JSONObject getProductDetails(String productID) {
        getProductDetailsLock.lock();
        try {
            JSONObject response = new JSONObject();

            String query = "SELECT * FROM tblShoppingProduct WHERE productID = ?";

            DatabaseConnection dbConnection = new DatabaseConnection();
            Connection conn = dbConnection.connect();

            if (conn == null) {
                response.put("status", "fail").put("message", "Database connection failed");
                return response;
            }

            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, productID);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    JSONObject product = new JSONObject();
                    product.put("productID", resultSet.getString("productID"));
                    product.put("productName", resultSet.getString("productName"));
                    product.put("productDetail", resultSet.getString("productDetail"));
                    product.put("productImage", resultSet.getString("productImage"));
                    product.put("productOriginalPrice", resultSet.getFloat("productOriginalPrice"));
                    product.put("productCurrentPrice", resultSet.getFloat("productCurrentPrice"));
                    product.put("productInventory", resultSet.getInt("productInventory"));
                    product.put("productAddress", resultSet.getString("productAddress"));
                    product.put("productCommentRate", resultSet.getFloat("productCommentRate"));
                    product.put("productStatus", resultSet.getBoolean("productStatus"));

                    response.put("status", "success").put("product", product);
                } else {
                    response.put("status", "fail").put("message", "Product not found");
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
            getProductDetailsLock.unlock();
        }
    }

    // 检索商品
    public JSONObject searchProducts(String searchTerm, String sortBy, String sortOrder) {
        searchProductsLock.lock();
        try {
            JSONObject response = new JSONObject();
            JSONArray productsArray = new JSONArray();

            // 确定排序列和顺序
            String orderByColumn;
            switch (sortBy) {
                case "price":
                    orderByColumn = "productCurrentPrice";
                    break;
                case "rate":
                    orderByColumn = "productCommentRate";
                    break;
                default:
                    orderByColumn = "productCurrentPrice"; // 默认按价格排序
                    break;
            }

            String order = "ASC".equalsIgnoreCase(sortOrder) ? "ASC" : "DESC";

            String query = "SELECT * FROM tblShoppingProduct WHERE productStatus = true " +
                    "AND (productName LIKE ? OR productID LIKE ? OR productDetail LIKE ?) " +
                    "ORDER BY " + orderByColumn + " " + order;

             DatabaseConnection dbConnection = new DatabaseConnection();
            Connection conn = dbConnection.connect();

            if (conn == null) {
                response.put("status", "fail").put("message", "数据库连接失败");
                return response;
            }

            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                String searchPattern = "%" + searchTerm + "%";
                preparedStatement.setString(1, searchPattern);
                preparedStatement.setString(2, searchPattern);
                preparedStatement.setString(3, searchPattern);

                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    JSONObject product = new JSONObject();
                    product.put("productID", resultSet.getString("productID"));
                    product.put("productName", resultSet.getString("productName"));
                    product.put("productDetail", resultSet.getString("productDetail"));
                    product.put("productImage", resultSet.getString("productImage"));
                    product.put("productOriginalPrice", resultSet.getFloat("productOriginalPrice"));
                    product.put("productCurrentPrice", resultSet.getFloat("productCurrentPrice"));
                    product.put("productInventory", resultSet.getInt("productInventory"));
                    product.put("productAddress", resultSet.getString("productAddress"));
                    product.put("productCommentRate", resultSet.getFloat("productCommentRate"));
                    product.put("productStatus", resultSet.getBoolean("productStatus"));

                    productsArray.put(product);
                }

                response.put("status", "success").put("products", productsArray);
            } catch (SQLException e) {
                e.printStackTrace();
                response.put("status", "fail").put("message", "SQL错误: " + e.getMessage());
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
            searchProductsLock.unlock();
        }
    }



    // 添加商品
    public boolean addProduct(String productID, String productName, String productDetail, String productImage,
                              float productOriginalPrice, float productCurrentPrice, int productInventory,
                              String productAddress, float productCommentRate, boolean productStatus) {
        addProductLock.lock();
        try {
            boolean isSuccess = false;
            DatabaseConnection dbConnection = new DatabaseConnection();
            Connection conn = dbConnection.connect();

            if (conn == null) {
                return false;
            }

            String query = "INSERT INTO tblShoppingProduct (productID, productName, productDetail, productImage, " +
                    "productOriginalPrice, productCurrentPrice, productInventory, productAddress, " +
                    "productCommentRate, productStatus) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, productID);
                preparedStatement.setString(2, productName);
                preparedStatement.setString(3, productDetail);
                preparedStatement.setString(4, productImage);
                preparedStatement.setFloat(5, productOriginalPrice);
                preparedStatement.setFloat(6, productCurrentPrice);
                preparedStatement.setInt(7, productInventory);
                preparedStatement.setString(8, productAddress);
                preparedStatement.setFloat(9, productCommentRate);
                preparedStatement.setBoolean(10, productStatus);

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
            addProductLock.unlock();
        }
    }

    // 删除商品
    public boolean deleteProduct(String productID) {
        deleteProductLock.lock();
        try {
            boolean isSuccess = false;
            DatabaseConnection dbConnection = new DatabaseConnection();
            Connection conn = dbConnection.connect();

            if (conn == null) {
                return false;
            }

            String query = "DELETE FROM tblShoppingProduct WHERE productID = ?";

            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, productID);
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
            deleteProductLock.unlock();
        }
    }

    //------------------------------------------------------------------------------------------------------//
    // 上架或者下架商品
    // 上架商品
    public boolean enableProduct(String productID) {
        return updateProductStatus(productID, true);
    }

    // 下架商品
    public boolean disableProduct(String productID) {
        return updateProductStatus(productID, false);
    }

    // 私有方法，用于更新商品状态
    private boolean updateProductStatus(String productID, boolean status) {
        updateProductStatusLock.lock();
        try {
            boolean isSuccess = false;
            DatabaseConnection dbConnection = new DatabaseConnection();
            Connection conn = dbConnection.connect();

            if (conn == null) {
                return false;
            }

            String query = "UPDATE tblShoppingProduct SET productStatus = ? WHERE productID = ?";

            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setBoolean(1, status);
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
            updateProductStatusLock.unlock();
        }
    }
    //-------------------------------------------------------------------------------------//

    // 获取同品类的商品
    public JSONObject getSameCategoryProducts(String productID) {
        getSameCategoryProductsLock.lock();
        try {
            JSONObject response = new JSONObject();
            JSONArray productsArray = new JSONArray();

            // 获取商品ID的前四位作为品类
            String categoryID = productID.substring(0, 4);

            // SQL 查询，确保查询所有需要的列
            String query = "SELECT productID, productName, productImage, productOriginalPrice, productCurrentPrice, " +
                    "productInventory, productDetail, productAddress, productCommentRate, productStatus " +
                    "FROM tblShoppingProduct WHERE productID LIKE ?";

            DatabaseConnection dbConnection = new DatabaseConnection();
            Connection conn = dbConnection.connect();

            if (conn == null) {
                response.put("status", "fail").put("message", "Database connection failed");
                return response;
            }

            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, categoryID + "%");
                ResultSet resultSet = preparedStatement.executeQuery();

                // 遍历查询结果，构建 JSON 响应
                while (resultSet.next()) {
                    JSONObject product = new JSONObject();
                    product.put("productID", resultSet.getString("productID"));
                    product.put("productName", resultSet.getString("productName"));
                    product.put("productImage", resultSet.getString("productImage"));
                    product.put("productOriginalPrice", resultSet.getFloat("productOriginalPrice"));
                    product.put("productCurrentPrice", resultSet.getFloat("productCurrentPrice"));
                    product.put("productInventory", resultSet.getInt("productInventory"));
                    product.put("productDetail", resultSet.getString("productDetail"));
                    product.put("productAddress", resultSet.getString("productAddress"));
                    product.put("productCommentRate", resultSet.getFloat("productCommentRate"));
                    product.put("productStatus", resultSet.getBoolean("productStatus"));

                    productsArray.put(product);
                }

                response.put("status", "success").put("products", productsArray);
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
            getSameCategoryProductsLock.unlock();
        }
    }


    //---------------------------------------------------------------------------------------------------

    //调整商品价格

    // 调整商品原价
    public boolean updateProductOriginalPrice(String productID, float newOriginalPrice) {
        return updateProductPrice(productID, "productOriginalPrice", newOriginalPrice);
    }


    // 调整商品现价
    public boolean updateProductCurrentPrice(String productID, float newCurrentPrice) {
        return updateProductPrice(productID, "productCurrentPrice", newCurrentPrice);
    }


    // 私有方法，用于更新商品价格
    private boolean updateProductPrice(String productID, String priceColumn, float newPrice) {
        updateProductPriceLock.lock();
        try {
            boolean isSuccess = false;
            DatabaseConnection dbConnection = new DatabaseConnection();
            Connection conn = dbConnection.connect();

            if (conn == null) {
                return false;
            }

            String query = "UPDATE tblShoppingProduct SET " + priceColumn + " = ? WHERE productID = ?";

            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setFloat(1, newPrice);
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
            updateProductPriceLock.unlock();
        }
    }

    //---------------------------------------------------------------------------------------------------

    // 增加商品库存
    public boolean increaseProductInventory(String productID, int amount) {
        return updateProductInventory(productID, amount, true);
    }

    // 减少商品库存
    public boolean decreaseProductInventory(String productID, int amount) {
        return updateProductInventory(productID, amount, false);
    }

    // 私有方法，用于更新商品库存
    private boolean updateProductInventory(String productID, int amount, boolean increase) {
        updateProductInventoryLock.lock();
        try {
            boolean isSuccess = false;
            DatabaseConnection dbConnection = new DatabaseConnection();
            Connection conn = dbConnection.connect();

            if (conn == null) {
                return false;
            }

            // 根据是增加还是减少库存，选择相应的操作
            String operation = increase ? "productInventory + ?" : "productInventory - ?";
            String query = "UPDATE tblShoppingProduct SET productInventory = " + operation + " WHERE productID = ?";

            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setInt(1, amount);
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
            updateProductInventoryLock.unlock();
        }
    }

    //------------------------------------------------------------------------------------------------------//

    //查询商品的评论
    public JSONObject getProductComments(String productID, Integer commentAttitude) {
        getProductCommentsLock.lock();
        try {
            JSONObject response = new JSONObject();
            JSONArray commentsArray = new JSONArray();
            DatabaseConnection dbConnection = new DatabaseConnection();
            Connection conn = dbConnection.connect();

            if (conn == null) {
                response.put("status", "fail").put("message", "Database connection failed");
                return response;
            }

            StringBuilder queryBuilder = new StringBuilder("SELECT * FROM tblShoppingComment WHERE productID = ?");
            if (commentAttitude != null) {
                queryBuilder.append(" AND commentAttitude = ?");
            }

            try (PreparedStatement preparedStatement = conn.prepareStatement(queryBuilder.toString())) {
                preparedStatement.setString(1, productID);
                if (commentAttitude != null) {
                    preparedStatement.setInt(2, commentAttitude);
                }
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    JSONObject comment = new JSONObject();
                    comment.put("username", resultSet.getString("username"));
                    comment.put("productID", resultSet.getString("productID"));
                    comment.put("commentID", resultSet.getString("commentID"));
                    comment.put("commentAttitude", resultSet.getInt("commentAttitude"));
                    comment.put("commentContent", resultSet.getString("commentContent"));
                    commentsArray.put(comment);
                }

                response.put("status", "success").put("comments", commentsArray);
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
            getProductCommentsLock.unlock();
        }
    }



    //------------------------------------------------------------------------------------------------------//

    //查看所有商品的评论

    public JSONObject getAllProductComments() {
        return getProductComments(null, null);
    }

    public JSONObject searchProductComments(String username, String productID) {
        getProductCommentsLock.lock();
        try {
            JSONObject response = new JSONObject();
            JSONArray commentsArray = new JSONArray();
            DatabaseConnection dbConnection = new DatabaseConnection();
            Connection conn = dbConnection.connect();

            if (conn == null) {
                response.put("status", "fail").put("message", "Database connection failed");
                return response;
            }

            StringBuilder queryBuilder = new StringBuilder("SELECT * FROM tblShoppingComment WHERE 1=1");
            if (username != null && !username.isEmpty()) {
                queryBuilder.append(" AND username = ?");
            }
            if (productID != null && !productID.isEmpty()) {
                queryBuilder.append(" AND productID = ?");
            }

            try (PreparedStatement preparedStatement = conn.prepareStatement(queryBuilder.toString())) {
                int paramIndex = 1;
                if (username != null && !username.isEmpty()) {
                    preparedStatement.setString(paramIndex++, username);
                }
                if (productID != null && !productID.isEmpty()) {
                    preparedStatement.setString(paramIndex++, productID);
                }

                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    JSONObject comment = new JSONObject();
                    comment.put("username", resultSet.getString("username"));
                    comment.put("productID", resultSet.getString("productID"));
                    comment.put("commentID", resultSet.getString("commentID"));
                    comment.put("commentAttitude", resultSet.getInt("commentAttitude"));
                    comment.put("commentContent", resultSet.getString("commentContent"));
                    commentsArray.put(comment);
                }

                response.put("status", "success").put("comments", commentsArray);
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
            getProductCommentsLock.unlock();
        }
    }

    //------------------------------------------------------------------------------------------------------//

    //根据订单添加评论
    public boolean addComment(String username, String productID, int commentAttitude, String commentContent) {
        addCommentLock.lock();
        try {
            boolean isSuccess = false;
            DatabaseConnection dbConnection = new DatabaseConnection();
            Connection conn = dbConnection.connect();

            if (conn == null) {
                return false;
            }

            String insertCommentQuery = "INSERT INTO tblShoppingComment (username, productID, commentID, commentAttitude, commentContent) " +
                    "VALUES (?, ?, ?, ?, ?)";
            String updateOrderQuery = "UPDATE tblShoppingOrder SET whetherComment = true WHERE username = ? AND productID = ?";
            //修改对应商品好评率
            String updateProductRateQuery = "UPDATE tblShoppingProduct SET productCommentRate = " +
                    "(SELECT AVG(commentAttitude) FROM tblShoppingComment WHERE productID = ?) WHERE productID = ?";

            try (PreparedStatement insertStatement = conn.prepareStatement(insertCommentQuery);
                 PreparedStatement updateOrderStatement = conn.prepareStatement(updateOrderQuery);
                 PreparedStatement updateProductRateStatement = conn.prepareStatement(updateProductRateQuery)) {

                String commentID = UUID.randomUUID().toString(); // 生成唯一评论ID

                insertStatement.setString(1, username);
                insertStatement.setString(2, productID);
                insertStatement.setString(3, commentID);
                insertStatement.setInt(4, commentAttitude);
                insertStatement.setString(5, commentContent);
                int rowsAffected = insertStatement.executeUpdate();

                if (rowsAffected > 0) {
                    updateOrderStatement.setString(1, username);
                    updateOrderStatement.setString(2, productID);
                    updateOrderStatement.executeUpdate();

                    //更新商品好评率
                    updateProductRateStatement.setString(1, productID);
                    updateProductRateStatement.setString(2, productID);
                    updateProductRateStatement.executeUpdate();

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
            addCommentLock.unlock();
        }
    }


    //------------------------------------------------------------------------------------------------------//

    //------------------------------------------------------------------------------------------------------//
}
