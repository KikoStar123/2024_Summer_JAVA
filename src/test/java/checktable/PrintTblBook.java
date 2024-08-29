package checktable;

import server.service.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PrintTblBook {
    public static void main(String[] args) {
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn != null) {
            try {
                String query = "SELECT * FROM tblBook";
                PreparedStatement pstmt = conn.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery();

                System.out.println("Printing tblBook table...");
                System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------");
                System.out.println("|  bookId  |  name  |  author  |  publishHouse  |  publishingYear  |  classification  |  libNumber  |  curNumber  |  location  |");
                System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------");

                while (rs.next()) {
                    String bookId = rs.getString("bookId");
                    String name = rs.getString("name");
                    String author = rs.getString("author");
                    String publishHouse = rs.getString("publishHouse");
                    String publishingYear = rs.getString("publishingYear");
                    String classification = rs.getString("classification");
                    int libNumber = rs.getInt("libNumber");
                    int curNumber = rs.getInt("curNumber");
                    String location = rs.getString("location");

                    System.out.println("|   " + bookId + "   |   " + name + "   |   " + author + "   |   " + publishHouse + "   |   " + publishingYear + "   |   " + classification + "   |   " + libNumber + "   |   " + curNumber + "   |   " + location + "   |");
                }

                System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------");

                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}