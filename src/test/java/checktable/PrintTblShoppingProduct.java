package checktable;

import server.service.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PrintTblShoppingProduct {
    public static void main(String[] args) {
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn != null) {
            try {
                String query = "SELECT * FROM tblshoppingproduct";
                PreparedStatement pstmt = conn.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery();

                System.out.println("Printing tblshoppingproduct table...");
                System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------");
                System.out.println("|  PRODUCTID  |  PRODUCTNAME  |  PRODUCTDETAIL  |  PRODUCTORIGINALPRICE  |  PRODUCTCURRENTPRICE  |  PRODUCTINVENTORY  |  PRODUCTADDRESS  |  PRODUCTCOMMENTRATE  |  PRODUCTSTATUS  |  PRODUCTIMAGE  |  STOREID  |");
                System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------");

                while (rs.next()) {
                    String productId = rs.getString("PRODUCTID");
                    String productName = rs.getString("PRODUCTNAME");
                    String productDetail = rs.getString("PRODUCTDETAIL");
                    float productOriginalPrice = rs.getFloat("PRODUCTORIGINALPRICE");
                    float productCurrentPrice = rs.getFloat("PRODUCTCURRENTPRICE");
                    int productInventory = rs.getInt("PRODUCTINVENTORY");
                    String productAddress = rs.getString("PRODUCTADDRESS");
                    float productCommentRate = rs.getFloat("PRODUCTCOMMENTRATE");
                    boolean productStatus = rs.getBoolean("PRODUCTSTATUS");
                    String productImage = rs.getString("PRODUCTIMAGE");
                    String storeId = rs.getString("STOREID");

                    System.out.println("|   " + productId + "   |   " + productName + "   |   " + productDetail + "   |   " + productOriginalPrice + "   |   " + productCurrentPrice + "   |   " + productInventory + "   |   " + productAddress + "   |   " + productCommentRate + "   |   " + productStatus + "   |   " + productImage + "   |   " + storeId + "   |");
                }

                System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------");

                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}