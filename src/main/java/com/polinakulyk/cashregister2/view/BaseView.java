package com.polinakulyk.cashregister2.view;

import com.polinakulyk.cashregister2.controller.Router;
import com.polinakulyk.cashregister2.controller.api.HttpRoute;
import com.polinakulyk.cashregister2.controller.api.Language;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.StreamSupport;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import static java.util.Optional.ofNullable;

public class BaseView {

    private HttpServletRequest request;

    public void init(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public String makeUrl(HttpRoute route) {
        return Router.makeUrl(getRequest(), route);
    }

    public Optional<HttpRoute> getRoute() {
        var routeString = Router.getRouteStringFromJsp(request);
        return HttpRoute.fromRouteString(routeString);
    }
}
