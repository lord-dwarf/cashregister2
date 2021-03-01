package com.polinakulyk.cashregister2.controller.dto;

import java.util.Optional;

import static java.util.Arrays.stream;

public enum HttpMethod {
    GET,
    POST,
    PATCH,
    PUT,
    DELETE,
    OPTIONS,
    HEAD,
    TRACE;

    /**
     * Finds the enum value by a given string.
     *
     * @param s
     * @return
     */
    public static Optional<HttpMethod> fromString(String s) {
        return stream(values())
                .filter((httpMethod) -> httpMethod.name().equals(s))
                .findFirst();
    }
}
