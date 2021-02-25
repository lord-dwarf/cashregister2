package com.polinakulyk.cashregister2.exception;

import java.net.HttpURLConnection;

import static com.polinakulyk.cashregister2.util.CashRegisterUtil.quote;

/**
 * The exception to be thrown when user not found in DB.
 */
public class CashRegisterUserNotFoundException extends CashRegisterException {

    public CashRegisterUserNotFoundException(String userIdentifier) {

        // by default, the associated HTTP status points to HTTP 401 Unauthorized
        // as a general interpretation of exception for client
        super(HttpURLConnection.HTTP_UNAUTHORIZED, quote("User not found", userIdentifier));
    }
}
