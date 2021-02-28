package com.polinakulyk.cashregister2.exception;

import com.polinakulyk.cashregister2.controller.api.HttpStatus;

import static com.polinakulyk.cashregister2.controller.api.HttpStatus.BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;

/**
 * The exception to be thrown when validation of user input did not succeed.
 */
public class CashRegisterValidationException extends CashRegisterException {

    public CashRegisterValidationException(String errorMessage) {
        super(BAD_REQUEST, errorMessage);
    }
}
