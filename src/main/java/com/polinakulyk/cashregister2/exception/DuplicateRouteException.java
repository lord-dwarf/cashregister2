package com.polinakulyk.cashregister2.exception;

import static com.polinakulyk.cashregister2.util.Util.quote;

public class DuplicateRouteException extends CashRegisterException {

    public DuplicateRouteException(String routeString) {
        super(quote("Duplicate route", routeString));
    }
}
