package com.polinakulyk.cashregister2.exception;

import static com.polinakulyk.cashregister2.controller.dto.HttpStatusCode.*;
import static com.polinakulyk.cashregister2.util.Util.quote;

/**
 * The exception to be thrown when user not authenticated or authorized.
 */
public class CashRegisterAuthorizationException extends CashRegisterException {

    public CashRegisterAuthorizationException(String userIdentifier) {

        // by default, the associated HTTP status points to HTTP 403 Unauthorized
        // as a general interpretation of authentication problem for client
        super(FORBIDDEN, quote("User not authorized", userIdentifier));
    }
}
