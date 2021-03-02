package com.polinakulyk.cashregister2.db;

import com.polinakulyk.cashregister2.exception.CashRegisterException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static com.polinakulyk.cashregister2.util.Util.MC;
import static com.polinakulyk.cashregister2.util.Util.MONEY_SCALE;
import static com.polinakulyk.cashregister2.util.Util.RM;
import static com.polinakulyk.cashregister2.util.Util.calendar;
import static com.polinakulyk.cashregister2.util.Util.toLocalDateTime;

public final class DbHelper {

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

    public static void tryCommit(Connection conn) {
        try {
            conn.commit();
        } catch (SQLException e) {
            throw new CashRegisterException("Could not commit", e);
        }
    }

    public static void tryRollback(Connection conn) {
        try {
            if (!conn.isClosed()) {
                conn.rollback();
            }
        } catch (SQLException e) {
            throw new CashRegisterException("Could not rollback", e);
        }
    }

    public static void tryClose(Connection conn) {
        try {
            if (!conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            throw new CashRegisterException("Could not close connection", e);
        }
    }
}
