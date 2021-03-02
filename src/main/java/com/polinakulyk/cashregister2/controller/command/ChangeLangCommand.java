package com.polinakulyk.cashregister2.controller.command;

import com.polinakulyk.cashregister2.controller.dto.RouteString;
import com.polinakulyk.cashregister2.exception.CashRegisterValidationException;

import java.io.IOException;
import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.polinakulyk.cashregister2.controller.router.RouterHelper.httpRouteFromRoutePath;
import static com.polinakulyk.cashregister2.controller.validator.ValidatorHelper.validStringNotNull;

public class ChangeLangCommand implements Command {

    private static final String LANG_PARAM = "lang";
    private static final String COOKIE_LANG_NAME = "cashregister2_lang";
    private static final String COOKIE_PATH = "/";
    private static final int COOKIE_AGE_YEAR = 365 * 24 * 3600;
    private static final String REDIRECT_ROUTE = "redirectRoute";

    @Override
    public Optional<RouteString> execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // validate lang
        var lang = validStringNotNull(request, LANG_PARAM);

        // set cookie according to lang query param
        Cookie langCookie = new Cookie(COOKIE_LANG_NAME, lang);
        langCookie.setPath(COOKIE_PATH);
        langCookie.setMaxAge(COOKIE_AGE_YEAR);

        // set lang cookie in response
        response.addCookie(langCookie);

        // redirect to the original page provided via query param
        var originalPage = validStringNotNull(request, REDIRECT_ROUTE);
        var redirectRoute = httpRouteFromRoutePath(originalPage)
                .orElseThrow(() -> new CashRegisterValidationException(
                        "Redirect route must be valid"));

        // do redirect
        return Optional.of(RouteString.of(redirectRoute));
    }
}
