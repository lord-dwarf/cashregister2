package com.polinakulyk.cashregister2.controller.api;

import com.polinakulyk.cashregister2.controller.Router;
import java.util.Optional;

import static com.polinakulyk.cashregister2.util.Util.*;
import static java.util.Arrays.stream;

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
    // error
    ERROR_CLIENT,     // 400 and 4xx
    ERROR_AUTH,       // 401 and 403
    ERROR_NOTFOUND,   // 404
    ERROR_SERVER;     // 5xx

    public static String toRouteString(HttpRoute route) {
        if (INDEX == route) {
            return "/";
        }
        var routeString = route.name().toLowerCase().replaceAll("_", "/");
        return addPrefix(routeString, "/");
    }

    public static Optional<HttpRoute> fromRouteString(String routeString) {
        if ("/".equals(routeString)) {
            return Optional.of(INDEX);
        }
        routeString = removeSuffix(routeString, Router.INDEX_PATH);
        routeString = removePrefix(routeString, "/");
        var httpRouteString = routeString.replaceAll("/", "_");
        return stream(values())
                .filter(httpRoute -> httpRouteString.equalsIgnoreCase(httpRoute.name()))
                .findFirst();
    }
}
