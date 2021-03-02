package com.polinakulyk.cashregister2.controller.validator;

import com.polinakulyk.cashregister2.db.dto.ProductAmountUnit;
import com.polinakulyk.cashregister2.exception.CashRegisterValidationException;

import java.math.BigDecimal;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;

import static com.polinakulyk.cashregister2.util.Util.bigDecimalAmount;
import static com.polinakulyk.cashregister2.util.Util.bigDecimalMoney;
import static com.polinakulyk.cashregister2.util.Util.quote;
import static com.polinakulyk.cashregister2.util.Util.stripOrNull;

import static java.util.Optional.ofNullable;

public final class ValidatorHelper {

    private ValidatorHelper() {
        throw new UnsupportedOperationException("Cannot instantiate");
    }

    public static Optional<String> validStringNullable(HttpServletRequest request, String paramName) {
        var stringValue = stripOrNull(request.getParameter(paramName));
        return ofNullable(stringValue);
    }

    public static String validStringNotNull(HttpServletRequest request, String paramName) {
        return validStringNullable(request, paramName)
                .orElseThrow(() -> new CashRegisterValidationException(
                        paramName + " must be present"));
    }

    public static Optional<Integer> validIntegerNullable(HttpServletRequest request, String paramName) {
        var intString = validStringNullable(request, paramName);
        if (intString.isEmpty()) {
            return Optional.empty();
        }
        try {
            return Optional.of(Integer.parseInt(intString.get()));
        } catch (NumberFormatException e) {
            throw new CashRegisterValidationException(
                    quote("Number format is not valid", intString.get()), e);

        }
    }

    public static BigDecimal validBigDecimalAmountNotNull(
            HttpServletRequest request, String paramName, ProductAmountUnit amountUnit) {
        String bigDecimalString = validStringNotNull(request, paramName);
        try {
            return bigDecimalAmount(bigDecimalString, amountUnit);
        } catch (NumberFormatException e) {
            throw new CashRegisterValidationException(
                    quote("Number format is not valid", bigDecimalString), e);
        }
    }

    public static BigDecimal validBigDecimalMoneyNotNull(
            HttpServletRequest request, String paramName) {
        String bigDecimalString = validStringNotNull(request, paramName);
        try {
            return bigDecimalMoney(bigDecimalString);
        } catch (NumberFormatException e) {
            throw new CashRegisterValidationException(
                    quote("Number format is not valid", bigDecimalString), e);
        }
    }

    public static void validBigDecimalNonNegative(BigDecimal bigDecimal, String paramName) {
        if (bigDecimal.compareTo(BigDecimal.ZERO) < 0) {
            throw new CashRegisterValidationException(
                    quote(paramName + " value must be >= 0, but was ", bigDecimal));
        }
    }

    public static void validBigDecimalGreaterThanZero(BigDecimal bigDecimal, String paramName) {
        if (bigDecimal.compareTo(BigDecimal.ZERO) <= 0) {
            throw new CashRegisterValidationException(
                    quote(paramName + " value must be > 0, but was ", bigDecimal));
        }
    }

    public static void validBigDecimalLessThan(
            BigDecimal bigDecimal, BigDecimal upperBound, String paramName) {

        if (bigDecimal.compareTo(upperBound) >= 0) {
            throw new CashRegisterValidationException(quote(
                    paramName + " value must be < " + upperBound + ", but was ", bigDecimal)
            );
        }
    }
}
