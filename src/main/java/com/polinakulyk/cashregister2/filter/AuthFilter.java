package com.polinakulyk.cashregister2.filter;

import com.polinakulyk.cashregister2.controller.router.MainRouter;
import com.polinakulyk.cashregister2.controller.router.Router;
import com.polinakulyk.cashregister2.controller.router.RouterHelper;
import com.polinakulyk.cashregister2.security.AuthHelper;
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

import static com.polinakulyk.cashregister2.controller.router.RouterHelper.getRoutePathFromServletRequest;
import static com.polinakulyk.cashregister2.controller.router.RouterHelper.redirect;
import static com.polinakulyk.cashregister2.controller.dto.HttpMethod.fromString;
import static com.polinakulyk.cashregister2.controller.dto.HttpRoute.ERROR_AUTH;
import static com.polinakulyk.cashregister2.controller.dto.HttpRoute.ERROR_NOTFOUND;

@WebFilter(filterName = "authFilter")
public class AuthFilter implements Filter {

    private Router router;
    private AuthHelper authHelper;


    @Override
    public void init(FilterConfig filterConfig) {
        router = MainRouter.Singleton.INSTANCE;
        authHelper = new AuthHelper();
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        var request = (HttpServletRequest) req;
        var response = (HttpServletResponse) res;
        var routeString = getRoutePathFromServletRequest(request);
        var route = RouterHelper.httpRouteFromRoutePath(routeString);
        if (route.isEmpty()) {
            redirect(request, response, ERROR_NOTFOUND);
            return;
        }
        var httpMethod = fromString(request.getMethod()).get();
        var roles = router.getRoles(httpMethod, route.get());
        if (routeString.isEmpty()) {
            redirect(request, response, ERROR_NOTFOUND);
            return;
        }
        if (!authHelper.isAuthorized(request, roles.get())) {
            redirect(request, response, ERROR_AUTH);
            return;
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // nothing
    }
}
