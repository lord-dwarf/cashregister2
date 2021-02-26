package com.polinakulyk.cashregister2.view;

import com.polinakulyk.cashregister2.controller.Router;
import com.polinakulyk.cashregister2.controller.api.HttpRoute;
import java.util.Optional;

public class RouterView extends RequestView {

    public String makeUrl(HttpRoute route) {
        return Router.makeUrl(getRequest(), route);
    }

    public Optional<HttpRoute> getRoute() {
        var routeString = Router.getRouteStringFromJsp(getRequest());
        return HttpRoute.fromRouteString(routeString);
    }
}
