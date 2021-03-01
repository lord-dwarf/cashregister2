package com.polinakulyk.cashregister2.controller.dto;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

public final class RouteString {

    private final HttpRoute httpRoute;
    private final String queryString;

    private RouteString(HttpRoute httpRoute) {
        requireNonNull(httpRoute, "HTTP route must not be null");
        this.httpRoute = httpRoute;
        this.queryString = null;
    }

    private RouteString(HttpRoute httpRoute, String queryString) {
        requireNonNull(httpRoute, "HTTP route must not be null");
        requireNonNull(queryString, "Query string must not be null");
        this.httpRoute = httpRoute;
        this.queryString = queryString;
    }

    public HttpRoute getHttpRoute() {
        return this.httpRoute;
    }

    public Optional<String> getQueryString() {
        return Optional.ofNullable(this.queryString);
    }

    public static RouteString of(HttpRoute httpRoute) {
        return new RouteString(httpRoute);
    }

    public static RouteString of(HttpRoute httpRoute, String queryString) {
        return new RouteString(httpRoute, queryString);
    }
}
