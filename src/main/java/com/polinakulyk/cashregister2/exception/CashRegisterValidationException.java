package com.polinakulyk.cashregister2.exception;

import static com.polinakulyk.cashregister2.controller.dto.HttpStatus.BAD_REQUEST;

/**
 * The exception to be thrown when validation of user input did not succeed.
 */
public class CashRegisterValidationException extends CashRegisterException {

    public CashRegisterValidationException(String errorMessage) {
        super(BAD_REQUEST, errorMessage);
    }

    public CashRegisterValidationException(String errorMessage, Throwable cause) {
        super(BAD_REQUEST, errorMessage, cause);
    }
}
