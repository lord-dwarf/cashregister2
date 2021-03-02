package com.polinakulyk.cashregister2.filter;

import com.polinakulyk.cashregister2.controller.dto.HttpMethod;
import com.polinakulyk.cashregister2.controller.router.MainRouter;
import com.polinakulyk.cashregister2.controller.router.Router;
import com.polinakulyk.cashregister2.controller.router.RouterHelper;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.polinakulyk.cashregister2.controller.dto.HttpRoute.ERROR_AUTH;
import static com.polinakulyk.cashregister2.controller.dto.HttpRoute.ERROR_CLIENT;
import static com.polinakulyk.cashregister2.controller.dto.HttpRoute.ERROR_NOTFOUND;
import static com.polinakulyk.cashregister2.controller.router.RouterHelper.getRoutePathFromServletRequest;
import static com.polinakulyk.cashregister2.controller.router.RouterHelper.redirect;
import static com.polinakulyk.cashregister2.security.AuthHelper.isAuthorized;

/**
 * Authenticates incoming requests according to routes and roles in {@link MainRouter}.
 * <p>
 * IMPORTANT for ordering of filters, see web.xml.
 */
@WebFilter(filterName = "authFilter")
public class AuthFilter implements Filter {

    private Router router;

    @Override
    public void init(FilterConfig filterConfig) {
        router = MainRouter.Singleton.INSTANCE;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        var request = (HttpServletRequest) req;
        var response = (HttpServletResponse) res;

        var routeString = getRoutePathFromServletRequest(request);
        var route = RouterHelper.httpRouteFromRoutePath(routeString);

        // for the unknown http route - redirect to 404 error page
        if (route.isEmpty()) {
            redirect(request, response, ERROR_NOTFOUND);
            return;
        }
        var httpMethod = HttpMethod.fromString(request.getMethod());
        // for the unknown http method - redirect to 400 error page
        if (httpMethod.isEmpty()) {
            redirect(request, response, ERROR_CLIENT);
            return;
        }

        var roles = router.getRoles(httpMethod.get(), route.get());
        // for the unknown http route - redirect to 404 error page
        if (roles.isEmpty()) {
            redirect(request, response, ERROR_NOTFOUND);
            return;
        }

        // for not authorized user - redirect to auth error page
        if (!isAuthorized(request, roles.get())) {
            redirect(request, response, ERROR_AUTH);
            return;
        }

        // proceed to next filter
        chain.doFilter(request, response);
    }
}
