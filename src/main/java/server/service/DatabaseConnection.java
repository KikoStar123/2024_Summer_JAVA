package server.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//拿postgreSQL先测试一下功能
public class DatabaseConnection {
    private final String url = "jdbc:postgresql://localhost:5432/testStudentInfo";
    private final String user = "postgres";
    private final String password = "123456";

    public Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }
}
