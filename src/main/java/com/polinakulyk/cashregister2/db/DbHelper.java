package com.polinakulyk.cashregister2.db;

import java.math.BigDecimal;
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

/**
 * Utility class for the application database layer.
 */
public final class DbHelper {

    private DbHelper() {
        throw new UnsupportedOperationException("Can not instantiate");
    }

    /**
     * Extracts non-null {@link LocalDateTime} from result set.
     *
     * @param rs
     * @param fieldName
     * @return
     * @throws SQLException
     */
    public static LocalDateTime getLocalDateTime(ResultSet rs, String fieldName)
            throws SQLException {
        return toLocalDateTime(
                rs.getTimestamp(fieldName, calendar()));
    }

    /**
     * Extracts nullable {@link LocalDateTime} from result set.
     *
     * @param rs
     * @param fieldName
     * @return
     * @throws SQLException
     */
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
     * Converts non-null {@link LocalDateTime} to SQL {@link Timestamp}.
     *
     * @param localDateTime
     * @return
     */
    public static Timestamp toTimestamp(LocalDateTime localDateTime) {
        return Timestamp.from(localDateTime.toInstant(ZoneOffset.UTC));
    }

    /**
     * Converts nullable {@link LocalDateTime} to SQL {@link Timestamp}.
     *
     * @param localDateTime
     * @return
     */
    public static Timestamp toTimestampNullable(LocalDateTime localDateTime) {
        return localDateTime != null ? toTimestamp(localDateTime) : null;
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

    /**
     * Calculates cost with money precision.
     *
     * @param price
     * @param amount
     * @return
     */
    public static BigDecimal calcCostByPriceAndAmount(BigDecimal price, BigDecimal amount) {
        return amount.multiply(price, MC).setScale(MONEY_SCALE, RM);
    }
}
