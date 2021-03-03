package com.polinakulyk.cashregister2.controller.dto;

import java.util.Optional;

import static java.util.Arrays.stream;

/**
 * HTTP route paths known to the application.
 */
public enum HttpRoute {
    // Home
    INDEX,
    // Products
    PRODUCTS_LIST,
    PRODUCTS_ADD,
    PRODUCTS_VIEW,
    PRODUCTS_EDIT,
    // Receipts
    RECEIPTS_LIST,
    RECEIPTS_VIEW,
    RECEIPTS_EDIT,
    RECEIPTS_CANCEL,
    // My receipts
    MYRECEIPTS_LIST,
    MYRECEIPTS_ADD,
    MYRECEIPTS_VIEW,
    MYRECEIPTS_EDIT,
    MYRECEIPTS_COMPLETE,
    MYRECEIPTS_CANCEL,
    MYRECEIPTS_ADDITEM_SEARCH,
    MYRECEIPTS_ADDITEM,
    // Reports
    REPORTS_LIST,
    REPORTS_X,
    REPORTS_Z,
    // Auth
    AUTH_LOGIN,
    AUTH_LOGOUT,
    // Localization
    AUTH_LANG,
    // error
    ERROR_CLIENT,     // 400 and 4xx
    ERROR_AUTH,       // 401 and 403
    ERROR_NOTFOUND,   // 404
    ERROR_SERVER;     // 5xx

    /**
     * Finds the enum value by a given string.
     *
     * @param httpRouteStr
     * @return
     */
    public static Optional<HttpRoute> fromString(String httpRouteStr) {
        return stream(values())
                .filter(httpRoute -> httpRoute.name().equals(httpRouteStr))
                .findFirst();
    }
}
