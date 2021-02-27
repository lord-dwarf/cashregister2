package com.polinakulyk.cashregister2.exception;

import com.polinakulyk.cashregister2.controller.Router;
import com.polinakulyk.cashregister2.controller.api.HttpRoute;
import com.polinakulyk.cashregister2.controller.api.Language;
import com.polinakulyk.cashregister2.service.ReportService;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.polinakulyk.cashregister2.controller.Router.redirect;
import static com.polinakulyk.cashregister2.controller.api.HttpRoute.ERROR_AUTH;
import static com.polinakulyk.cashregister2.controller.api.HttpRoute.ERROR_CLIENT;
import static com.polinakulyk.cashregister2.controller.api.HttpRoute.ERROR_NOTFOUND;
import static com.polinakulyk.cashregister2.controller.api.HttpRoute.ERROR_SERVER;
import static com.polinakulyk.cashregister2.controller.api.HttpRoute.toRouteString;
import static java.net.HttpURLConnection.*;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Optional.ofNullable;

@WebFilter(filterName = "errorFilter")
public class ErrorFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(ErrorFilter.class);

    @Override
    public void init(FilterConfig filterConfig) {
        // do nothing
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException {
        var request = (HttpServletRequest) req;
        var response = (HttpServletResponse) res;
        try {
            chain.doFilter(request, response);
        } catch (CashRegisterException e) {
            log.error(e.getMessage(), e);
            int httpStatusCode = e.getHttpStatus();
            HttpRoute errorRedirect;
            String errorMessage = null;
            if (httpStatusCode == HTTP_BAD_REQUEST) {
                errorRedirect = ERROR_CLIENT;
                errorMessage = e.getMessage();
            } else if (httpStatusCode == HTTP_UNAUTHORIZED || httpStatusCode == HTTP_FORBIDDEN) {
                errorRedirect = ERROR_AUTH;
            } else if (httpStatusCode == HTTP_NOT_FOUND) {
                errorRedirect = ERROR_NOTFOUND;
            } else if (httpStatusCode > HTTP_BAD_REQUEST && httpStatusCode < HTTP_INTERNAL_ERROR) {
                errorRedirect = ERROR_CLIENT;
            } else if (httpStatusCode >= HTTP_INTERNAL_ERROR) {
                errorRedirect = ERROR_SERVER;
            } else {
                errorRedirect = ERROR_SERVER;
            }
            var redirectUrl = toRouteString(errorRedirect);
            if (errorMessage != null) {
                errorMessage = new String(
                        Base64.getUrlEncoder().encode(errorMessage.getBytes(UTF_8)),
                        UTF_8);
                redirectUrl += "?errorMessage=" + errorMessage;
            }

            redirect(request, response,  redirectUrl);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            redirect(request, response, toRouteString(ERROR_SERVER));
        }
    }

    @Override
    public void destroy() {
        // do nothing
    }
}
