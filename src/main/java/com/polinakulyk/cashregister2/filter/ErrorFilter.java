package com.polinakulyk.cashregister2.filter;

import com.polinakulyk.cashregister2.controller.dto.RouteString;
import com.polinakulyk.cashregister2.exception.CashRegisterException;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.polinakulyk.cashregister2.controller.dto.HttpRoute.ERROR_AUTH;
import static com.polinakulyk.cashregister2.controller.dto.HttpRoute.ERROR_CLIENT;
import static com.polinakulyk.cashregister2.controller.dto.HttpRoute.ERROR_NOTFOUND;
import static com.polinakulyk.cashregister2.controller.dto.HttpRoute.ERROR_SERVER;
import static com.polinakulyk.cashregister2.controller.dto.HttpStatusCode.BAD_REQUEST;
import static com.polinakulyk.cashregister2.controller.router.RouterHelper.redirect;
import static com.polinakulyk.cashregister2.util.Util.toBase64;

/**
 * Provides routing of incoming requests that caused exceptions withing application,
 * and also logging for happened exceptions.
 */
@WebFilter(filterName = "errorFilter")
public class ErrorFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(ErrorFilter.class);

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException {
        var request = (HttpServletRequest) req;
        var response = (HttpServletResponse) res;

        try {
            // try to proceed to next filters
            chain.doFilter(request, response);
        } catch (CashRegisterException e) {
            // log with stacktrace
            log.error(e.getMessage(), e);
            var httpStatus = e.getHttpStatus();
            // generalized routing according to exception's HTTP code
            var errorRedirect =
                    switch (httpStatus) {
                        case BAD_REQUEST -> ERROR_CLIENT;
                        case UNAUTHORIZED, FORBIDDEN -> ERROR_AUTH;
                        case NOT_FOUND -> ERROR_NOTFOUND;
                        case INTERNAL_SERVER_ERROR -> ERROR_SERVER;
                        // by default display server error page
                        default -> ERROR_SERVER;
                    };

            // for bad request pages, provide the detailed error message, if available
            if (httpStatus == BAD_REQUEST && e.getMessage() != null) {
                // convert error message to URL-safe Base64 to prevent breaking of URL
                var redirectQueryString = "?errorMessage=" + toBase64(e.getMessage());
                // redirect to error page route with query param containing error message
                redirect(request, response, RouteString.of(errorRedirect, redirectQueryString));
            } else {
                // by default, do not reveal error message details
                redirect(request, response, errorRedirect);
            }

        } catch (Exception e) {
            // log with stacktrace
            log.error(e.getMessage(), e);
            // for the exceptions that do not posses the HTTP code - redirect to 500 error page
            redirect(request, response, ERROR_SERVER);
        }
    }
}
