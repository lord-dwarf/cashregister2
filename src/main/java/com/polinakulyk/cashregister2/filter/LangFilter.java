package com.polinakulyk.cashregister2.filter;

import com.polinakulyk.cashregister2.controller.dto.Language;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static java.util.Optional.ofNullable;

/**
 * Enriches the incoming request with localized messages from resource bundle selected according
 * to language cookie.
 * <p>
 * IMPORTANT for ordering of filters, see web.xml.
 */
@WebFilter(filterName = "langFilter")
public class LangFilter implements Filter {

    private static final String LANG_COOKIE_NAME = "cashregister2_lang";
    public static final Language DEFAULT_LANGUAGE = Language.EN;
    private static final String LOCALIZATION_MESSAGES_PREFIX = "messages_";
    private static final String RESOURCE_BUNDLE_NAME = "messages";

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        var request = (HttpServletRequest) req;
        var response = (HttpServletResponse) res;

        // get lang string from cookie
        // for an empty or invalid lang string - select default language
        var langString = getLangStringFromCookie(request).orElse("");
        var language = Language.fromString(langString)
                .orElse(DEFAULT_LANGUAGE);

        // get resource bundle according to the locale
        var locale = language.getLocale();
        ResourceBundle messages = ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME, locale);

        // iterate over all bundle keys and set request attributes with prefix "messages_"
        var messageKeys = messages.getKeys();
        while (messageKeys.hasMoreElements()) {
            String key = messageKeys.nextElement();
            String value = messages.getString(key);
            request.setAttribute(LOCALIZATION_MESSAGES_PREFIX + key, value);
        }

        // proceed to next filter
        chain.doFilter(request, response);
    }

    private Optional<String> getLangStringFromCookie(HttpServletRequest request) {
        var cookies = ofNullable(request.getCookies())
                // to prevent exceptions, return an empty array
                .orElseGet(() -> new Cookie[]{});

        var langString = Arrays.stream(cookies)
                .filter(Objects::nonNull)
                .filter(cookie -> LANG_COOKIE_NAME.equals(cookie.getName()))
                .map(Cookie::getValue)
                .filter(Objects::nonNull)
                .findFirst();
        return langString;
    }
}
