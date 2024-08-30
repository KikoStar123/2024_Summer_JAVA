package server.service;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ShoppingProductService {

    private final Lock getAllProductsLock = new ReentrantLock();
    private final Lock getProductDetailsLock = new ReentrantLock();
    private final Lock searchProductsLock = new ReentrantLock();
    private final Lock addProductLock = new ReentrantLock();
    private final Lock deleteProductLock = new ReentrantLock();
    private final Lock updateProductStatusLock = new ReentrantLock();
    private final Lock getSameCategoryProductsLock = new ReentrantLock();

    // 获取所有的商品
    public JSONObject getAllProducts() {
        getAllProductsLock.lock();
        try {
            JSONObject response = new JSONObject();
            JSONArray productsArray = new JSONArray();

            String query = "SELECT * FROM tblShoppingProduct WHERE productStatus = true ORDER BY productCurrentPrice ASC";

            DatabaseConnection dbConnection = new DatabaseConnection();
            Connection conn = dbConnection.connect();

            if (conn == null) {
                response.put("status", "fail").put("message", "Database connection failed");
                return response;
            }

            try (PreparedStatement preparedStatement = conn.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    JSONObject product = new JSONObject();
                    product.put("productID", resultSet.getString("productID"));
                    product.put("productName", resultSet.getString("productName"));
                    product.put("productDetail", resultSet.getString("productDetail"));
                    product.put("productImage", resultSet.getBytes("productImage"));  // Assuming image is stored as a BLOB
                    product.put("productOriginalPrice", resultSet.getFloat("productOriginalPrice"));
                    product.put("productCurrentPrice", resultSet.getFloat("productCurrentPrice"));
                    product.put("productInventory", resultSet.getInt("productInventory"));
                    product.put("productAddress", resultSet.getString("productAddress"));
                    product.put("productCommentRate", resultSet.getFloat("productCommentRate"));

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
                    product.put("productImage", resultSet.getBytes("productImage"));
                    product.put("productOriginalPrice", resultSet.getFloat("productOriginalPrice"));
                    product.put("productCurrentPrice", resultSet.getFloat("productCurrentPrice"));
                    product.put("productInventory", resultSet.getInt("productInventory"));
                    product.put("productAddress", resultSet.getString("productAddress"));
                    product.put("productCommentRate", resultSet.getFloat("productCommentRate"));

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
    public JSONObject searchProducts(String searchKeyword, String category, String specification) {
        searchProductsLock.lock();
        try {
            JSONObject response = new JSONObject();
            JSONArray productsArray = new JSONArray();

            StringBuilder query = new StringBuilder("SELECT * FROM tblShoppingProduct WHERE productStatus = true");

            if (searchKeyword != null && !searchKeyword.isEmpty()) {
                query.append(" AND productName LIKE ?");
            }

            if (category != null && !category.isEmpty()) {
                query.append(" AND productID LIKE ?");
            }

            if (specification != null && !specification.isEmpty()) {
                query.append(" AND productDetail LIKE ?");
            }

            DatabaseConnection dbConnection = new DatabaseConnection();
            Connection conn = dbConnection.connect();

            if (conn == null) {
                response.put("status", "fail").put("message", "Database connection failed");
                return response;
            }

            try (PreparedStatement preparedStatement = conn.prepareStatement(query.toString())) {
                int index = 1;

                if (searchKeyword != null && !searchKeyword.isEmpty()) {
                    preparedStatement.setString(index++, "%" + searchKeyword + "%");
                }

                if (category != null && !category.isEmpty()) {
                    preparedStatement.setString(index++, category + "%");
                }

                if (specification != null && !specification.isEmpty()) {
                    preparedStatement.setString(index++, "%" + specification + "%");
                }

                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    JSONObject product = new JSONObject();
                    product.put("productID", resultSet.getString("productID"));
                    product.put("productName", resultSet.getString("productName"));
                    product.put("productDetail", resultSet.getString("productDetail"));
                    product.put("productImage", resultSet.getBytes("productImage"));
                    product.put("productOriginalPrice", resultSet.getFloat("productOriginalPrice"));
                    product.put("productCurrentPrice", resultSet.getFloat("productCurrentPrice"));
                    product.put("productInventory", resultSet.getInt("productInventory"));
                    product.put("productAddress", resultSet.getString("productAddress"));
                    product.put("productCommentRate", resultSet.getFloat("productCommentRate"));

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
            searchProductsLock.unlock();
        }
    }

    // 添加商品
    public boolean addProduct(String productID, String productName, String productDetail, byte[] productImage,
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
                preparedStatement.setBytes(4, productImage);
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

    // 上架或者下架商品
    public boolean updateProductStatus(String productID, boolean status) {
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

    // 获取同品类的商品
    public JSONObject getSameCategoryProducts(String productID) {
        getSameCategoryProductsLock.lock();
        try {
            JSONObject response = new JSONObject();
            JSONArray productsArray = new JSONArray();

            // 获取商品ID的前四位作为品类
            String categoryID = productID.substring(0, 4);

            String query = "SELECT productID, productName, productImage, productOriginalPrice, productCurrentPrice, productInventory " +
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

                while (resultSet.next()) {
                    JSONObject product = new JSONObject();
                    product.put("productID", resultSet.getString("productID"));
                    product.put("productName", resultSet.getString("productName"));
                    product.put("productImage", resultSet.getBytes("productImage")); // Assuming image is stored as a BLOB
                    product.put("productOriginalPrice", resultSet.getFloat("productOriginalPrice"));
                    product.put("productCurrentPrice", resultSet.getFloat("productCurrentPrice"));
                    product.put("productInventory", resultSet.getInt("productInventory"));

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
}
