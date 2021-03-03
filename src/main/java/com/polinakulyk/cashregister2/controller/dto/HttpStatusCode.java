package com.polinakulyk.cashregister2.controller.dto;

/**
 * HTTP status codes known to the application.
 * Used extensively by {@link com.polinakulyk.cashregister2.exception.CashRegisterException}
 * and its descendants.
 */
public enum HttpStatusCode {
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404),
    INTERNAL_SERVER_ERROR(500);

    private final int code;

    HttpStatusCode(int httpStatusCode) {
        this.code = httpStatusCode;
    }

    public int getCode() {
        return code;
    }
}
