package com.polinakulyk.cashregister2.view;

import com.polinakulyk.cashregister2.controller.MainRouter;
import com.polinakulyk.cashregister2.controller.api.HttpRoute;
import java.util.Optional;

public class RouterView extends RequestView {

    public String makeUrl(HttpRoute route) {
        return MainRouter.makeUrl(getRequest(), route);
    }

    public Optional<HttpRoute> getRoute() {
        var routeString = MainRouter.getRouteStringFromJsp(getRequest());
        return HttpRoute.fromRouteString(routeString);
    }
}
