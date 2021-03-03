package com.polinakulyk.cashregister2.db;

import com.polinakulyk.cashregister2.config.Config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static com.polinakulyk.cashregister2.config.Config.getConfig;
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

        // get application config instance
        var config = getConfig();

        // create connection pool config
        var connPoolConfig = new HikariConfig();
        connPoolConfig.setJdbcUrl(config.getDatasourceUrl());
        connPoolConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        connPoolConfig.setUsername(config.getDatasourceUsername());
        connPoolConfig.setPassword(config.getDatasourcePassword());
        connPoolConfig.setConnectionTimeout(config.getDbpoolTimeout());
        connPoolConfig.setMaximumPoolSize(config.getDbpoolSize());
        connPoolConfig.setAutoCommit(true);

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
