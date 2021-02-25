package com.polinakulyk.cashregister2.view;

import com.polinakulyk.cashregister2.controller.api.HttpRoute;
import java.util.Optional;

public class NavBarView extends RouterView {

    public String getNavMenuActive(HttpRoute navMenuRoute) {
        return getRoute().equals(Optional.of(navMenuRoute)) ? "active" : "";
    }
}
