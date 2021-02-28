package com.polinakulyk.cashregister2.exception;

import com.polinakulyk.cashregister2.controller.api.HttpStatus;

import static com.polinakulyk.cashregister2.controller.api.HttpStatus.*;

public class CashRegisterException extends RuntimeException {

    private static final HttpStatus DEFAULT_HTTP_STATUS = INTERNAL_SERVER_ERROR;

    private final HttpStatus httpStatus;

    public CashRegisterException(String message) {
        this(DEFAULT_HTTP_STATUS, message);
    }

    public CashRegisterException(String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = DEFAULT_HTTP_STATUS;
    }

    public CashRegisterException(Throwable cause) {
        super(cause);
        this.httpStatus = DEFAULT_HTTP_STATUS;
    }

    public CashRegisterException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public CashRegisterException(HttpStatus httpStatus, String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
