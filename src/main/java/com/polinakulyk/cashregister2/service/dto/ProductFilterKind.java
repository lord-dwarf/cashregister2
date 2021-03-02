package com.polinakulyk.cashregister2.service.dto;

import com.polinakulyk.cashregister2.exception.CashRegisterValidationException;
import java.util.Optional;

import static com.polinakulyk.cashregister2.util.Util.quote;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;

public enum ProductFilterKind {
    CODE, NAME;

    /**
     * Finds the enum value by a given string.
     *
     * @param productFilterKindStr
     * @return
     */
    public static Optional<ProductFilterKind> fromString(String productFilterKindStr) {
        return stream(values())
                .filter(productFilterKind -> productFilterKind.name().equals(productFilterKindStr))
                .findFirst();
    }

    public static ProductFilterKind fromExistingString(String productFilterKindStr) {
        return fromString(productFilterKindStr)
                .orElseThrow(() -> new CashRegisterValidationException(
                        quote("Product filter kind must be one of",
                                asList(ProductFilterKind.values()))));
    }
}
