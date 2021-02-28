package com.polinakulyk.cashregister2.filter;

import com.polinakulyk.cashregister2.controller.api.Language;
import com.polinakulyk.cashregister2.security.AuthHelper;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static java.util.Optional.ofNullable;

@WebFilter(filterName = "langFilter")
public class LangFilter implements Filter {

    public static final String LANG_COOKIE_NAME = "cashregister2_lang";
    public static final Language DEFAULT_LANGUAGE = Language.EN;

    @Override
    public void init(FilterConfig filterConfig) {
        // do nothing
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        var request = (HttpServletRequest) req;
        var response = (HttpServletResponse) res;
        var langString = getLangStringFromCookie(request);
        var language =
                Language.fromString(langString.orElse("")).orElse(DEFAULT_LANGUAGE);
        var locale = language.getLocale();
        ResourceBundle messages = ResourceBundle.getBundle("messages", locale);
        var messageKeys = messages.getKeys();
        while (messageKeys.hasMoreElements()) {
            String key = messageKeys.nextElement();
            String value = messages.getString(key);
            request.setAttribute("messages_" + key, value);
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // do nothing
    }

    private Optional<String> getLangStringFromCookie(HttpServletRequest request) {
        var cookies = ofNullable(request.getCookies()).orElseGet(() -> new Cookie[]{});
        var langString = Arrays.stream(cookies)
                .filter(Objects::nonNull)
                .filter((cookie) -> LANG_COOKIE_NAME.equals(cookie.getName()))
                .map(Cookie::getValue)
                .filter(Objects::nonNull)
                .findFirst();
        return langString;
    }
}
