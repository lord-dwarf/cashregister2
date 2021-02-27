package com.polinakulyk.cashregister2.controller.command;

import com.polinakulyk.cashregister2.controller.Router;
import com.polinakulyk.cashregister2.controller.api.Command;
import com.polinakulyk.cashregister2.controller.api.HttpRoute;
import com.polinakulyk.cashregister2.db.entity.Product;
import com.polinakulyk.cashregister2.security.AuthHelper;
import com.polinakulyk.cashregister2.service.ProductService;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.polinakulyk.cashregister2.controller.api.HttpRoute.PRODUCTS_LIST;
import static com.polinakulyk.cashregister2.controller.api.HttpRoute.RECEIPTS_LIST;
import static com.polinakulyk.cashregister2.controller.api.HttpRoute.toRouteString;
import static com.polinakulyk.cashregister2.db.dto.ProductAmountUnit.fromString;
import static com.polinakulyk.cashregister2.util.Util.bigDecimalAmount;
import static com.polinakulyk.cashregister2.util.Util.bigDecimalMoney;

public class ChangeLangCommand implements Command {

    @Override
    public Optional<String> execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        // set cookie according to lang query param
        var lang = request.getParameter("lang");
        Cookie langCookie = new Cookie("cashregister2_lang", lang);
        langCookie.setPath("/");
        langCookie.setMaxAge(365 * 24 * 3600);
        response.addCookie(langCookie);

        // refresh page
        var redirectRoute =
                HttpRoute.fromRouteString(request.getParameter("redirectRoute")).get();
        return Optional.of(toRouteString(redirectRoute));
    }
}
