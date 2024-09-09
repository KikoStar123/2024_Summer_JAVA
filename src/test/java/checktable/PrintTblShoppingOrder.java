package checktable;

import server.service.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PrintTblShoppingOrder {
    public static void main(String[] args) {
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn != null) {
            try {
                String query = "SELECT * FROM tblshoppingorder";
                PreparedStatement pstmt = conn.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery();

                System.out.println("Printing tblshoppingorder table...");
                System.out.println("-------------------------------------------------------------------------------------------------------------");
                System.out.println("|  ORDERID  |  PRODUCTID  |  PRODUCTNUMBER  |  WHETHERCOMMENT  |  PAIDMONEY  |  PAIDSTATUS  |  PRODUCTNAME  |  USERNAME  |  STOREID  |");
                System.out.println("-------------------------------------------------------------------------------------------------------------");

                while (rs.next()) {
                    String orderId = rs.getString("ORDERID");
                    String productId = rs.getString("PRODUCTID");
                    int productNumber = rs.getInt("PRODUCTNUMBER");
                    boolean whetherComment = rs.getBoolean("WHETHERCOMMENT");
                    float paidMoney = rs.getFloat("PAIDMONEY");
                    boolean paidStatus = rs.getBoolean("PAIDSTATUS");
                    String productName = rs.getString("PRODUCTNAME");
                    String username = rs.getString("USERNAME");
                    String storeId = rs.getString("STOREID");

                    System.out.println("|   " + orderId + "   |   " + productId + "   |   " + productNumber + "   |   " + whetherComment + "   |   " + paidMoney + "   |   " + paidStatus + "   |   " + productName + "   |   " + username + "   |   " + storeId + "   |");
                }

                System.out.println("-------------------------------------------------------------------------------------------------------------");

                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}