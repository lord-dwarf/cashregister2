package com.polinakulyk.cashregister2.db.dto;

import com.polinakulyk.cashregister2.exception.CashRegisterException;

import java.util.Optional;

import static com.polinakulyk.cashregister2.util.Util.quote;

import static java.util.Arrays.stream;

public enum ProductAmountUnit {
    UNIT,
    KILO;

    /**
     * Finds the enum value by a given string.
     *
     * @param amountUnitStr
     * @return
     */
    public static Optional<ProductAmountUnit> fromString(String amountUnitStr) {
        return stream(values())
                .filter(amountUnit -> amountUnit.name().equals(amountUnitStr))
                .findFirst();
    }

    /**
     * Finds the enum value by a given integer.
     *
     * @param amountUnitInt
     * @return
     */
    public static Optional<ProductAmountUnit> fromInteger(Integer amountUnitInt) {
        return stream(values())
                .filter(amountUnit -> amountUnit.ordinal() == amountUnitInt)
                .findFirst();
    }

    /**
     * Finds the existing enum value by a given integer, otherwise throws.
     *
     * @param amountUnitInt
     * @return
     */
    public static ProductAmountUnit fromExistingInteger(Integer amountUnitInt) {
        return fromInteger(amountUnitInt).
                orElseThrow(() -> new CashRegisterException(
                        quote("Product amount unit not found", amountUnitInt)));
    }
}
