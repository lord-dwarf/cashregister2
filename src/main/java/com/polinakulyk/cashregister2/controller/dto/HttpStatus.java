package com.polinakulyk.cashregister2.controller.dto;

public enum HttpStatus {
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404),
    INTERNAL_SERVER_ERROR(500);

    private final int code;

    HttpStatus(int httpStatusCode) {
        this.code = httpStatusCode;
    }

    public int getCode() {
        return code;
    }
}
