package com.polinakulyk.cashregister2.db;

import com.polinakulyk.cashregister2.exception.CashRegisterException;
import com.polinakulyk.cashregister2.util.CashRegisterUtil;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Optional;
import java.util.Properties;

import static com.polinakulyk.cashregister2.util.CashRegisterUtil.*;
import static com.polinakulyk.cashregister2.util.CashRegisterUtil.getProperties;
import static com.polinakulyk.cashregister2.util.CashRegisterUtil.getProperty;

public class DbHelper {

    private static final String datasourceUrl;
    private static final String datasourceUsername;
    private static final String datasourcePassword;

    static {
        var properties = getProperties();
        datasourceUrl = getProperty(properties, "datasource.url");
        datasourceUsername = getProperty(properties, "datasource.username");
        datasourcePassword = getProperty(properties, "datasource.password");
    }

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new CashRegisterException("Can't find MySQL Driver", e);
        }
        Properties dbProperties = new Properties();
        dbProperties.put("user", datasourceUsername);
        dbProperties.put("password", datasourcePassword);
        try {

            Connection connection = DriverManager.getConnection(datasourceUrl, dbProperties);
            connection.setAutoCommit(true);
            return connection;
        } catch (SQLException e) {
            throw new CashRegisterException("Can't establish the connection to DB.", e);
        }
    }

    public static LocalDateTime getLocalDateTime(ResultSet rs, String fieldName)
            throws SQLException {
        return toLocalDateTime(
                rs.getTimestamp(fieldName, calendar()));
    }

    public static Optional<LocalDateTime> getLocalDateTimeNullable(ResultSet rs, String fieldName)
            throws SQLException {
        var timestamp = rs.getTimestamp(fieldName, calendar());
        if (timestamp != null) {
            return Optional.of(toLocalDateTime(timestamp));
        } else {
            return Optional.empty();
        }
    }

    /**
     * Create user-friendly (4 chars) but still relatively unique id for receipt,
     * based on UUID id of receipt
     *
     * @param receiptIdUuid
     * @return
     */
    public static String calcReceiptCode(String receiptIdUuid) {
        return receiptIdUuid.substring(32);
    }

    public static BigDecimal calcCostByPriceAndAmount(BigDecimal price, BigDecimal amount) {
        return amount.multiply(price, MC).setScale(MONEY_SCALE, RM);
    }

    public static Timestamp toTimestamp(LocalDateTime localDateTime) {
        return Timestamp.from(localDateTime.toInstant(ZoneOffset.UTC));
    }

    public static Timestamp toTimestampNullable(LocalDateTime localDateTime) {
        return localDateTime != null ? toTimestamp(localDateTime) : null;
    }
}
