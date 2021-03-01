package com.polinakulyk.cashregister2.controller.command;

import com.polinakulyk.cashregister2.controller.dto.RouteString;
import com.polinakulyk.cashregister2.controller.router.RouterHelper;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.polinakulyk.cashregister2.controller.dto.HttpRoute.INDEX;

public class ChangeLangCommand implements Command {

    @Override
    public Optional<RouteString> execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        // set cookie according to lang query param
        var lang = request.getParameter("lang");
        Cookie langCookie = new Cookie("cashregister2_lang", lang);
        langCookie.setPath("/");
        langCookie.setMaxAge(365 * 24 * 3600);
        response.addCookie(langCookie);

        // redirect to a page provided via query param, otherwise redirect to home
        var redirectRoute =
                RouterHelper.routePathToHttpRoute(request.getParameter("redirectRoute"))
                .orElse(INDEX);

        return Optional.of(RouteString.of(redirectRoute));
    }
}
