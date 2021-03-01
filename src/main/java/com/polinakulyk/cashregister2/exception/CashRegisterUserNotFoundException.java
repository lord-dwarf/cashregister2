package com.polinakulyk.cashregister2.exception;

import com.polinakulyk.cashregister2.controller.dto.HttpStatus;

import static com.polinakulyk.cashregister2.util.Util.quote;

/**
 * The exception to be thrown when user not found in DB.
 */
public class CashRegisterUserNotFoundException extends CashRegisterException {

    public CashRegisterUserNotFoundException(String userIdentifier) {

        // by default, the associated HTTP status points to HTTP 401 Unauthorized
        // as a general interpretation of authentication problem for client
        super(HttpStatus.UNAUTHORIZED, quote("User not found", userIdentifier));
    }
}
