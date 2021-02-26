package com.polinakulyk.cashregister2.service.dto;

import java.util.Optional;

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
                .filter((productFilterKind) -> productFilterKind.name().equals(productFilterKindStr))
                .findFirst();
    }
}
