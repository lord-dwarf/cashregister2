package com.polinakulyk.cashregister2.exception;

import com.polinakulyk.cashregister2.controller.api.HttpStatus;

import static com.polinakulyk.cashregister2.controller.api.HttpStatus.*;
import static com.polinakulyk.cashregister2.util.Util.quote;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;

/**
 * The exception to be thrown when user not authenticated or authrorized.
 */
public class CashRegisterAuthorizationException extends CashRegisterException {

    public CashRegisterAuthorizationException(String userIdentifier) {

        // by default, the associated HTTP status points to HTTP 403 Unauthorized
        // as a general interpretation of authentication problem for client
        super(FORBIDDEN, quote("User not authorized", userIdentifier));
    }
}
