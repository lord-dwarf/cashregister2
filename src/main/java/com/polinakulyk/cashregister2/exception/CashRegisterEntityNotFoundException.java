package com.polinakulyk.cashregister2.exception;

import com.polinakulyk.cashregister2.controller.api.HttpStatus;

import static com.polinakulyk.cashregister2.controller.api.HttpStatus.*;
import static com.polinakulyk.cashregister2.util.Util.quote;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;

/**
 * The exception to be thrown when entity not found in DB.
 */
public class CashRegisterEntityNotFoundException extends CashRegisterException {

    public CashRegisterEntityNotFoundException(String entityId) {

        // by default, the associated HTTP status points to HTTP 404 Not Found
        // as a general interpretation of exception for client
        super(NOT_FOUND, quote("Entity not found", entityId));
    }
}
