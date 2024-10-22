package server.service;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {
    private static HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:h2:tcp://localhost:9092/./testdb"); // 使用服务器模式的连接URL
        //config.setJdbcUrl("jdbc:h2:file:./testdb");
        //config.setJdbcUrl("jdbc:h2:tcp://47.101.210.178:9092//data/h2_database/testdb");
        config.setUsername("sa");
        config.setPassword("");
        dataSource = new HikariDataSource(config);
    }

    public Connection connect() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
