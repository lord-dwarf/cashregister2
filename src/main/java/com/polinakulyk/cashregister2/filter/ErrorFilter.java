package com.polinakulyk.cashregister2.filter;

import com.polinakulyk.cashregister2.controller.api.HttpStatus;
import com.polinakulyk.cashregister2.controller.api.RouteString;
import com.polinakulyk.cashregister2.exception.CashRegisterException;
import com.polinakulyk.cashregister2.util.Util;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.polinakulyk.cashregister2.controller.api.HttpRoute.ERROR_AUTH;
import static com.polinakulyk.cashregister2.controller.api.HttpRoute.ERROR_CLIENT;
import static com.polinakulyk.cashregister2.controller.api.HttpRoute.ERROR_NOTFOUND;
import static com.polinakulyk.cashregister2.controller.api.HttpRoute.ERROR_SERVER;
import static com.polinakulyk.cashregister2.controller.api.HttpStatus.BAD_REQUEST;
import static com.polinakulyk.cashregister2.controller.router.RouterHelper.redirect;
import static com.polinakulyk.cashregister2.util.Util.toBase64;

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
            var httpStatus = e.getHttpStatus();
            var errorRedirect =
                    switch (httpStatus) {
                        case BAD_REQUEST -> ERROR_CLIENT;
                        case UNAUTHORIZED, FORBIDDEN -> ERROR_AUTH;
                        case NOT_FOUND -> ERROR_NOTFOUND;
                        case INTERNAL_SERVER_ERROR -> ERROR_SERVER;
                        default -> ERROR_SERVER;
                    };
            if (httpStatus == BAD_REQUEST && e.getMessage() != null) {
                var redirectQueryString = "?errorMessage=" + toBase64(e.getMessage());
                redirect(request, response, RouteString.of(errorRedirect, redirectQueryString));
            } else {
                redirect(request, response, errorRedirect);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            redirect(request, response, ERROR_SERVER);
        }
    }

    @Override
    public void destroy() {
        // do nothing
    }
}
