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

/**
 * 商品服务类，提供与商品相关的操作
 */
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
    private final Lock getProductsByStoreLock = new ReentrantLock();

    private final Lock updateProductImagePathLock = new ReentrantLock();


    /**
     * 获取所有商品信息。
     * @param sortBy 按照指定字段排序（"price" 或 "rate"）。
     * @param sortOrder 排序顺序（"ASC" 或 "DESC"）。
     * @return 包含商品列表的 JSON 对象。
     */
    public JSONObject getAllProducts(String sortBy, String sortOrder) {
        getAllProductsLock.lock();
        try {
            JSONObject response = new JSONObject();
            JSONArray productsArray = new JSONArray();

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
            String query = "SELECT * FROM tblShoppingProduct p JOIN tblStore s ON p.storeID = s.storeID WHERE productStatus = true ORDER BY " + orderByColumn + " " + order;

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
                    product.put("storeID", resultSet.getString("storeID")); // 新增 storeID
                    product.put("storeName", resultSet.getString("storeName"));

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
            getAllProductsLock.unlock();
        }
    }



    /**
     * 根据商品ID查询商品详情。
     * @param productID 商品ID。
     * @return 包含商品详情的 JSON 对象。
     */
    public JSONObject getProductDetails(String productID) {
        getProductDetailsLock.lock();
        try {
            JSONObject response = new JSONObject();

            String query = "SELECT * FROM tblShoppingProduct p JOIN tblStore s ON p.storeID = s.storeID WHERE productID = ?";

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
                    product.put("storeID", resultSet.getString("storeID")); // 新增 storeID
                    product.put("storeName", resultSet.getString("storeName"));

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


    /**
     * 检索商品，根据关键词、排序字段和排序顺序。
     * @param searchTerm 搜索关键词。
     * @param sortBy 排序字段（"price" 或 "rate"）。
     * @param sortOrder 排序顺序（"ASC" 或 "DESC"）。
     * @return 包含搜索结果的 JSON 对象。
     */
    public JSONObject searchProducts(String searchTerm, String sortBy, String sortOrder) {
        searchProductsLock.lock();
        System.out.println("sortOrder: " + sortOrder);
        try {
            JSONObject response = new JSONObject();
            JSONArray productsArray = new JSONArray();

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

            System.out.println("order: " + order);

            String query = "SELECT * FROM tblShoppingProduct p JOIN tblStore s ON p.storeID = s.storeID WHERE productStatus = true " +
                    "AND (productName LIKE ? OR productID LIKE ? OR productDetail LIKE ?) " +
                    "ORDER BY " + orderByColumn + " " + order;

            System.out.println("query: " + query);

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

                System.out.println(preparedStatement.toString());

                ResultSet resultSet = preparedStatement.executeQuery();

                System.out.println(resultSet.toString());
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
                    product.put("storeID", resultSet.getString("storeID")); // 新增 storeID
                    product.put("storeName", resultSet.getString("storeName"));

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
            System.out.println(response.toString());
            return response;
        } finally {
            searchProductsLock.unlock();
        }
    }



    /**
     * 添加新商品。
     * @param productID 商品ID。
     * @param productName 商品名称。
     * @param productDetail 商品详情。
     * @param productOriginalPrice 商品原价。
     * @param productCurrentPrice 商品现价。
     * @param productInventory 商品库存。
     * @param productAddress 商品地址。
     * @param productStatus 商品状态（上架或下架）。
     * @param storeID 商店ID。
     * @return 添加是否成功。
     */
    public boolean addProduct(String productID, String productName, String productDetail,
                              float productOriginalPrice, float productCurrentPrice, int productInventory,
                              String productAddress, boolean productStatus, String storeID) {
        addProductLock.lock();
        try {
            boolean isSuccess = false;
            DatabaseConnection dbConnection = new DatabaseConnection();
            Connection conn = dbConnection.connect();

            if (conn == null) {
                return false;
            }

            String query = "INSERT INTO tblShoppingProduct (productID, productName, productDetail,  " +
                    "productOriginalPrice, productCurrentPrice, productInventory, productAddress, " +
                    "productStatus, storeID) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, productID);
                preparedStatement.setString(2, productName);
                preparedStatement.setString(3, productDetail);
                preparedStatement.setFloat(4, productOriginalPrice);
                preparedStatement.setFloat(5, productCurrentPrice);
                preparedStatement.setInt(6, productInventory);
                preparedStatement.setString(7, productAddress);
                preparedStatement.setBoolean(8, productStatus);
                preparedStatement.setString(9, storeID); // 新增 storeID

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

    /**
     * 根据商品ID删除商品。
     * @param productID 商品ID。
     * @return 删除是否成功。
     */
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
    /**
     * 上架商品。
     * @param productID 商品ID。
     * @return 上架是否成功。
     */
    public boolean enableProduct(String productID) {
        return updateProductStatus(productID, true);
    }

    /**
     * 下架商品。
     * @param productID 商品ID。
     * @return 下架是否成功。
     */
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

    /**
     * 获取同品类商品。
     * @param productID 商品ID。
     * @return 包含同品类商品列表的 JSON 对象。
     */
    public JSONObject getSameCategoryProducts(String productID) {
        getSameCategoryProductsLock.lock();
        try {
            JSONObject response = new JSONObject();
            JSONArray productsArray = new JSONArray();

            // 获取商品ID的前四位作为品类
            String categoryID = productID.substring(0, 4);

            // SQL 查询，确保查询所有需要的列
            String query = "SELECT * FROM tblShoppingProduct p JOIN tblStore s ON p.storeID = s.storeID WHERE productID LIKE ?";

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
                    product.put("storeID", resultSet.getString("storeID")); // 新增 storeID
                    product.put("storeName", resultSet.getString("storeName"));


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

    /**
     * 更新商品的原价。
     * @param productID 商品ID。
     * @param newOriginalPrice 新的商品原价。
     * @return 更新是否成功。
     */
    public boolean updateProductOriginalPrice(String productID, float newOriginalPrice) {
        return updateProductPrice(productID, "productOriginalPrice", newOriginalPrice);
    }


    /**
     * 更新商品的现价。
     * @param productID 商品ID。
     * @param newCurrentPrice 新的商品现价。
     * @return 更新是否成功。
     */
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

    /**
     * 增加商品库存。
     * @param productID 商品ID。
     * @param amount 增加的库存数量。
     * @return 增加是否成功。
     */
    public boolean increaseProductInventory(String productID, int amount) {
        return updateProductInventory(productID, amount, true);
    }

    /**
     * 减少商品库存。
     * @param productID 商品ID。
     * @param amount 减少的库存数量。
     * @return 减少是否成功。
     */
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

    /**
     * 查询商品的评论。
     * @param productID 商品ID。
     * @param commentAttitude 评论态度（可选）。
     * @return 包含评论列表的 JSON 对象。
     */
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

            // 构建 SQL 查询，根据参数是否为空决定添加条件
            StringBuilder queryBuilder = new StringBuilder("SELECT * FROM tblShoppingComment WHERE 1=1");
            if (productID != null) {
                queryBuilder.append(" AND productID = ?");
            }
            if (commentAttitude != null) {
                queryBuilder.append(" AND commentAttitude = ?");
            }

            try (PreparedStatement preparedStatement = conn.prepareStatement(queryBuilder.toString())) {
                int parameterIndex = 1; // 用于设置 PreparedStatement 的参数索引

                // 根据条件设置参数
                if (productID != null) {
                    preparedStatement.setString(parameterIndex++, productID);
                }
                if (commentAttitude != null) {
                    preparedStatement.setInt(parameterIndex++, commentAttitude);
                }

                ResultSet resultSet = preparedStatement.executeQuery();

                // 处理查询结果
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

    /**
     * 添加商品评论。
     * @param username 用户名。
     * @param productID 商品ID。
     * @param commentAttitude 评论态度（好评、中评、差评）。
     * @param commentContent 评论内容。
     * @return 添加是否成功。
     */
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
            // 修改对应商品好评率
            String updateProductRateQuery = "UPDATE tblShoppingProduct SET productCommentRate = " +
                    "(SELECT COUNT(*) FILTER (WHERE commentAttitude = 3) * 1.0 / COUNT(*) FROM tblShoppingComment WHERE productID = ?) WHERE productID = ?";
            // 修改对应商店好评率
            String updateStoreRateQuery = "UPDATE tblStore SET storeRate = " +
                    "(SELECT COUNT(*) FILTER (WHERE commentAttitude = 3) * 1.0 / COUNT(*) FROM tblShoppingComment c JOIN tblShoppingProduct p ON c.productID = p.productID WHERE p.storeID = ?) WHERE storeID = ?";

            try (PreparedStatement insertStatement = conn.prepareStatement(insertCommentQuery);
                 PreparedStatement updateOrderStatement = conn.prepareStatement(updateOrderQuery);
                 PreparedStatement updateProductRateStatement = conn.prepareStatement(updateProductRateQuery);
                 PreparedStatement updateStoreRateStatement = conn.prepareStatement(updateStoreRateQuery)) {

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

                    // 更新商品好评率
                    updateProductRateStatement.setString(1, productID);
                    updateProductRateStatement.setString(2, productID);
                    updateProductRateStatement.executeUpdate();

                    // 更新商店好评率
                    String storeID = getStoreIDByProductID(productID, conn);
                    if (storeID != null) {
                        updateStoreRateStatement.setString(1, storeID);
                        updateStoreRateStatement.setString(2, storeID);
                        updateStoreRateStatement.executeUpdate();
                    }

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

    // 获取商品对应的商店ID
    private String getStoreIDByProductID(String productID, Connection conn) {
        String storeID = null;
        String query = "SELECT storeID FROM tblShoppingProduct WHERE productID = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, productID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                storeID = resultSet.getString("storeID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return storeID;
    }



    //------------------------------------------------------------------------------------------------------//
    /**
     * 根据商店ID获取所有商品。
     * @param storeID 商店ID。
     * @return 包含商店商品列表的 JSON 对象。
     */
    public JSONObject getProductsByStore(String storeID) {
        getProductsByStoreLock.lock();
        try {
            JSONObject response = new JSONObject();
            JSONArray productsArray = new JSONArray();

            // SQL 查询语句，选择所有属于该商店的商品
            String query = "SELECT * FROM tblShoppingProduct p JOIN tblStore s ON p.storeID = s.storeID WHERE p.storeID = ?";

            DatabaseConnection dbConnection = new DatabaseConnection();
            Connection conn = dbConnection.connect();

            if (conn == null) {
                response.put("status", "fail").put("message", "Database connection failed");
                return response;
            }

            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, storeID);
                ResultSet resultSet = preparedStatement.executeQuery();

                // 将结果集中的每一项商品信息加入 JSON 数组
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
                    product.put("storeID", resultSet.getString("storeID"));
                    product.put("storeName", resultSet.getString("storeName"));

                    productsArray.put(product);
                }

                // 构建返回 JSON 响应
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
            getProductsByStoreLock.unlock();
        }
    }

    //------------------------------------------------------------------------------------------------------//
    /**
     * 更新商品图片路径。
     * @param productID 商品ID。
     * @param imagePath 新的图片路径。
     * @return 更新是否成功的 JSON 对象。
     */
    public JSONObject updateProductImagePath(String productID, String imagePath) {
        JSONObject response = new JSONObject();
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();
        updateProductImagePathLock.lock();
        if (conn == null) {
            response.put("status", "fail");
            response.put("message", "Database connection failed");
            return response;
        }

        try {
            String query = "UPDATE tblShoppingProduct SET productImage = ? WHERE productID = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, imagePath);
            pstmt.setString(2, productID);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                response.put("status", "success");
            } else {
                response.put("status", "fail");
                response.put("message", "Product not found");
            }
        } catch (SQLException e) {
            response.put("status", "fail");
            response.put("message", "SQL Error: " + e.getMessage());
        } finally {
            updateProductImagePathLock.lock();
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                response.put("status", "fail");
                response.put("message", ex.getMessage());
            }
        }
        return response;
    }


    //------------------------------------------------------------------------------------------------------//

    //------------------------------------------------------------------------------------------------------//


}
