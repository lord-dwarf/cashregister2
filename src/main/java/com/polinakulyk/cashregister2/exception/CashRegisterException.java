package com.polinakulyk.cashregister2.exception;

import java.net.HttpURLConnection;

public class CashRegisterException extends RuntimeException {

    private static final Integer DEFAULT_HTTP_STATUS = HttpURLConnection.HTTP_INTERNAL_ERROR;

    private final Integer httpStatus;

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

    public CashRegisterException(Integer httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public CashRegisterException(Integer httpStatus, String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public Integer getHttpStatus() {
        return httpStatus;
    }
}
