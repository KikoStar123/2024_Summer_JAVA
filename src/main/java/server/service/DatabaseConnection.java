package server.service;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
/**
 * 数据库连接服务类，使用 HikariCP 连接池管理数据库连接。
 */
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
    /**
     * 获取数据库连接。
     * @return 数据库连接对象，如果获取失败则返回 null。
     */
    public Connection connect() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 关闭数据库连接。
     * @param conn 需要关闭的数据库连接对象。
     */
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
