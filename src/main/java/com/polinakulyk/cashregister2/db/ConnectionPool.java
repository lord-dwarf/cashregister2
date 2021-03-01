package com.polinakulyk.cashregister2.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static com.polinakulyk.cashregister2.util.Util.getExistingProperty;
import static com.polinakulyk.cashregister2.util.Util.getProperties;

public class ConnectionPool {

    private static final String datasourceUrl;
    private static final String datasourceUsername;
    private static final String datasourcePassword;

    static {
        var properties = getProperties();
        datasourceUrl = getExistingProperty(properties, "datasource.url");
        datasourceUsername = getExistingProperty(properties, "datasource.username");
        datasourcePassword = getExistingProperty(properties, "datasource.password");
    }

    private static final HikariConfig connPoolConfig;
    private static final HikariDataSource connPool;

    static {
        connPoolConfig = new HikariConfig();
        connPoolConfig.setJdbcUrl(datasourceUrl);
        connPoolConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        connPoolConfig.setUsername(datasourceUsername);
        connPoolConfig.setPassword(datasourcePassword);
        connPoolConfig.setAutoCommit(true);
        connPoolConfig.setConnectionTimeout(5000);
        connPoolConfig.setMaximumPoolSize(5);
        connPool = new HikariDataSource(connPoolConfig);
    }

    public static Connection getConnection() throws SQLException {
        return connPool.getConnection();
    }
}
