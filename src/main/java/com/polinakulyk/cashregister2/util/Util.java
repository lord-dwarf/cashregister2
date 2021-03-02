package com.polinakulyk.cashregister2.util;

import com.polinakulyk.cashregister2.db.dto.ProductAmountUnit;
import com.polinakulyk.cashregister2.exception.CashRegisterException;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Optional.ofNullable;

/**
 * Application wide static utility class.
 */
public final class Util {

    public static final int PRECISION = 9;
    public static final RoundingMode RM = RoundingMode.HALF_UP;
    public static final MathContext MC = new MathContext(PRECISION, RM);
    public static final BigDecimal ZERO_MONEY = bigDecimalMoney("0");

    public static final int MONEY_SCALE = 2;
    public static final int AMOUNT_UNIT_SCALE = 0;
    public static final int AMOUNT_KILO_SCALE = 3;

    public static final String PROPERTIES_FILE_NAME = "application.properties";

    private Util() {
        throw new UnsupportedOperationException("Cannot instantiate");
    }

    /**
     * A shorthand for constructing developer-friendly exception messages.
     * <p>
     * Quotes are single quote for JSON-friendliness.
     *
     * @param message
     * @param value
     * @return
     */
    public static String quote(String message, Object value) {
        return String.format("%s: '%s'", message, value);
    }

    /**
     * An overloaded version of {@link Util#quote(String, Object)} with 2 object params.
     *
     * @param message
     * @param value1
     * @param value2
     * @return
     */
    public static String quote(String message, Object value1, Object value2) {
        return String.format("%s: '%s', '%s'", message, value1, value2);
    }

    /**
     * Returns stripped string or a null for null or blank strings.
     *
     * @param s
     * @return stripped string or null
     */
    public static String stripOrNull(String s) {
        if (s == null || s.isBlank()) {
            return null;
        }
        return s.strip();
    }

    public static String removePrefix(String s, String prefix) {
        return s.startsWith(prefix) ? s.substring(prefix.length()) : s;
    }

    public static String removeSuffix(String s, String suffix) {
        return s.endsWith(suffix) ? s.substring(0, s.length() - suffix.length()) : s;
    }

    public static String addPrefix(String s, String prefix) {
        return s.startsWith(prefix) ? s : (prefix + s);
    }

    /**
     * Current server-side time in UTC.
     *
     * @return
     */
    public static LocalDateTime now() {
        return LocalDateTime.now(Clock.systemUTC());
    }

    /**
     * Obtains {@link LocalDateTime} from {@link Date} in UTC.
     * TODO change to convert from TIMESTAMP
     * @param date
     * @return
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        return date.toInstant().atZone(zoneId()).toLocalDateTime();
    }

    public static ZoneId zoneId() {
        return ZoneId.of("Z");
    }

    public static TimeZone timeZone() {
        return TimeZone.getTimeZone(zoneId());
    }

    public static Calendar calendar() {
        return Calendar.getInstance(timeZone(), Locale.US);
    }

    /**
     * Generates UUID v4 using {@link java.security.SecureRandom}.
     *
     * @return
     */
    public static String generateUuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * A shorthand for creating {@link BigDecimal} for a money value,
     * based on a given fixed point string number.
     * The resulting {@link BigDecimal} will have default precision and rounding mode,
     * and a scale which is appropriate for money,
     *
     * @param fixedPointValue
     * @return
     */
    public static BigDecimal bigDecimalMoney(String fixedPointValue) {
        return new BigDecimal(fixedPointValue, MC).setScale(MONEY_SCALE, RM);
    }

    /**
     * A shorthand for creating {@link BigDecimal} for an amount value,
     * based on a given fixed point string number and amount unit.
     * The resulting {@link BigDecimal} will have default precision and rounding mode,
     * and a scale which is appropriate for a given amount unit,
     *
     * @param fixedPointValue
     * @param amountUnit
     * @return
     */
    public static BigDecimal bigDecimalAmount(
            String fixedPointValue, ProductAmountUnit amountUnit) {
        switch (amountUnit) {
            case UNIT:
                return new BigDecimal(fixedPointValue, MC).setScale(AMOUNT_UNIT_SCALE, RM);
            case KILO:
                return new BigDecimal(fixedPointValue, MC).setScale(AMOUNT_KILO_SCALE, RM);
            default:
                throw new UnsupportedOperationException(quote(
                        "Product amount unit not supported", amountUnit));
        }
    }

    public static BigDecimal add(BigDecimal a, BigDecimal b) {
        return a.add(b, MC);
    }

    public static BigDecimal subtract(BigDecimal a, BigDecimal b) {
        return a.subtract(b, MC);
    }

    /**
     * Reads application.properties.
     *
     * @return Properties object
     */
    public static Properties getProperties() {
        // get our class loader, which is capable of locating application.properties on classpath.
        var classLoader = Util.class.getClassLoader();
        try (InputStream input = classLoader.getResourceAsStream(PROPERTIES_FILE_NAME)) {
            var properties = new Properties();
            properties.load(input);
            return properties;
        } catch (IOException e) {
            throw new CashRegisterException(e);
        }
    }

    /**
     * Reads mandatory property.
     *
     * @param properties
     * @param propertyName
     * @return
     */
    public static String getPropertyNotBlank(Properties properties, String propertyName) {
        return ofNullable(stripOrNull(properties.getProperty(propertyName)))
                .orElseThrow(() -> new CashRegisterException(
                        quote("Property not found", propertyName))
                );
    }

    /**
     * Converts given bytes array to a Base64 string, URL-safe.
     *
     * @param bytes
     * @return
     */
    public static String toBase64(byte[] bytes) {
        return new String(Base64.getUrlEncoder().encode(bytes), UTF_8);
    }

    /**
     * Converts given string to a Base64 string, URL-safe.
     *
     * @param s
     * @return
     */
    public static String toBase64(String s) {
        return toBase64(s.getBytes(UTF_8));
    }

    /**
     * Obtains a string from Base64 bytes array, URL-safe.
     *
     * @param bytes
     * @return
     */
    public static String fromBase64(byte[] bytes) {
        return new String(Base64.getUrlDecoder().decode(bytes), UTF_8);
    }

    /**
     * Obtains a string from Base64 string, URL-safe.
     *
     * @param s
     * @return
     */
    public static String fromBase64(String s) {
        return fromBase64(s.getBytes(UTF_8));
    }
}
