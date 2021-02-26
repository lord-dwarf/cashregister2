package com.polinakulyk.cashregister2.exception;

import static com.polinakulyk.cashregister2.util.Util.quote;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;

/**
 * The exception to be thrown when entity not found in DB.
 */
public class CashRegisterEntityNotFoundException extends CashRegisterException {

    public CashRegisterEntityNotFoundException(String entityId) {

        // by default, the associated HTTP status points to HTTP 404 Not Found
        // as a general interpretation of exception for client
        super(HTTP_NOT_FOUND, quote("Entity not found", entityId));
    }
}
