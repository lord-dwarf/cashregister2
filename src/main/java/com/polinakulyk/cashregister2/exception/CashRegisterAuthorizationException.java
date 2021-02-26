package com.polinakulyk.cashregister2.exception;

import static com.polinakulyk.cashregister2.util.Util.quote;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;

/**
 * The exception to be thrown when user not authenticated or authrorized.
 */
public class CashRegisterAuthorizationException extends CashRegisterException {

    public CashRegisterAuthorizationException(String userIdentifier) {

        // by default, the associated HTTP status points to HTTP 401 Unauthorized
        // as a general interpretation of exception for client
        super(HTTP_UNAUTHORIZED, quote("User not authorized", userIdentifier));
    }
}
