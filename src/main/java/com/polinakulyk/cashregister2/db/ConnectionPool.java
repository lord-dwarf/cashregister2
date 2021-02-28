package com.polinakulyk.cashregister2.db;

import com.polinakulyk.cashregister2.exception.CashRegisterException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static com.polinakulyk.cashregister2.db.DbHelper.tryClose;
import static com.polinakulyk.cashregister2.db.DbHelper.tryCommit;
import static com.polinakulyk.cashregister2.db.DbHelper.tryRollback;
import static com.polinakulyk.cashregister2.util.Util.MC;
import static com.polinakulyk.cashregister2.util.Util.MONEY_SCALE;
import static com.polinakulyk.cashregister2.util.Util.RM;
import static com.polinakulyk.cashregister2.util.Util.calendar;
import static com.polinakulyk.cashregister2.util.Util.getExistingProperty;
import static com.polinakulyk.cashregister2.util.Util.getProperties;
import static com.polinakulyk.cashregister2.util.Util.toLocalDateTime;

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
