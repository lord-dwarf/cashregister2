package com.polinakulyk.cashregister2.exception;

import static com.polinakulyk.cashregister2.util.Util.quote;

/**
 * Sent by {@link com.polinakulyk.cashregister2.controller.router.Router} at an attempt to
 * add the already existing route.
 */
public class DuplicateRouteException extends CashRegisterException {

    public DuplicateRouteException(String routeString) {
        super(quote("Duplicate route", routeString));
    }
}
