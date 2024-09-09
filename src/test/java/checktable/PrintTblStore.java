package checktable;

import server.service.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PrintTblStore {
    public static void main(String[] args) {
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn != null) {
            try {
                String query = "SELECT * FROM tblstore";
                PreparedStatement pstmt = conn.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery();

                System.out.println("Printing tblstore table...");
                System.out.println("---------------------------------------------------------------------------------------------");
                System.out.println("|  STOREID  |  STORENAME  |  STOREPHONE  |  STORERATE  |  STORESTATUS  |  USERNAME  |");
                System.out.println("---------------------------------------------------------------------------------------------");

                while (rs.next()) {
                    String storeId = rs.getString("STOREID");
                    String storeName = rs.getString("STORENAME");
                    String storePhone = rs.getString("STOREPHONE");
                    float storeRate = rs.getFloat("STORERATE");
                    boolean storeStatus = rs.getBoolean("STORESTATUS");
                    String username = rs.getString("USERNAME");

                    System.out.println("|   " + storeId + "   |   " + storeName + "   |   " + storePhone + "   |   " + storeRate + "   |   " + storeStatus + "   |   " + username + "   |");
                }

                System.out.println("---------------------------------------------------------------------------------------------");

                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}