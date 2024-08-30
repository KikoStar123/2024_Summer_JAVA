package server.service;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShoppingProductService {

    public JSONObject getAllProducts() {
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
    }

    public JSONObject getProductDetails(String productID) {
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
    }

    // Other methods like searchProducts, addProduct, etc., can be added similarly
}
