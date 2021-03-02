package com.polinakulyk.cashregister2.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static com.polinakulyk.cashregister2.util.Util.getProperties;
import static com.polinakulyk.cashregister2.util.Util.getPropertyNotBlank;

/**
 * Connection pool utilities.
 */
public final class ConnectionPool {

    // connection pool is created as a singleton, because its connections
    // are meant to be reused concurrently
    private static class Singleton {
        public static final ConnectionPool INSTANCE = new ConnectionPool();
    }

    private final HikariDataSource connPool;

    private ConnectionPool() {

        // get date source properties
        var properties = getProperties();
        var datasourceUrl = getPropertyNotBlank(properties, "datasource.url");
        var datasourceUsername =
                getPropertyNotBlank(properties, "datasource.username");
        var datasourcePassword =
                getPropertyNotBlank(properties, "datasource.password");

        // create connection pool config
        var connPoolConfig = new HikariConfig();
        connPoolConfig.setJdbcUrl(datasourceUrl);
        connPoolConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        connPoolConfig.setUsername(datasourceUsername);
        connPoolConfig.setPassword(datasourcePassword);
        connPoolConfig.setAutoCommit(true); // transactions disabled by default
        connPoolConfig.setConnectionTimeout(5000);
        connPoolConfig.setMaximumPoolSize(50);

        // create connection pool
        connPool = new HikariDataSource(connPoolConfig);
    }

    /**
     * Get connection from underlying connection pool.
     *
     * @return
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        return Singleton.INSTANCE.connPool.getConnection();
    }
}
