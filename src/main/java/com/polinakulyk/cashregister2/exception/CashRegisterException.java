package com.polinakulyk.cashregister2.exception;

import com.polinakulyk.cashregister2.controller.dto.HttpStatusCode;

import static com.polinakulyk.cashregister2.controller.dto.HttpStatusCode.INTERNAL_SERVER_ERROR;

/**
 * The parent exception class for all exceptions thrown by application.
 *
 * Holds {@link HttpStatusCode} code associated with exception.
 */
public class CashRegisterException extends RuntimeException {

    private static final HttpStatusCode DEFAULT_HTTP_STATUS = INTERNAL_SERVER_ERROR;

    private final HttpStatusCode httpStatus;

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

    public CashRegisterException(HttpStatusCode httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public CashRegisterException(HttpStatusCode httpStatus, String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public HttpStatusCode getHttpStatus() {
        return httpStatus;
    }
}
