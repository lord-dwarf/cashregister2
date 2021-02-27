package com.polinakulyk.cashregister2.security;

import com.polinakulyk.cashregister2.controller.MainRouter;
import com.polinakulyk.cashregister2.controller.Router;
import com.polinakulyk.cashregister2.controller.api.HttpRoute;
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

import static com.polinakulyk.cashregister2.controller.Router.getRouteStringFromMainServlet;
import static com.polinakulyk.cashregister2.controller.Router.redirect;
import static com.polinakulyk.cashregister2.controller.api.HttpMethod.fromString;
import static com.polinakulyk.cashregister2.controller.api.HttpRoute.ERROR_AUTH;
import static com.polinakulyk.cashregister2.controller.api.HttpRoute.ERROR_NOTFOUND;
import static com.polinakulyk.cashregister2.controller.api.HttpRoute.toRouteString;

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
        var routeString = getRouteStringFromMainServlet(request);
        var route = HttpRoute.fromRouteString(routeString);
        if (route.isEmpty()) {
            redirect(request, response, toRouteString(ERROR_NOTFOUND));
            return;
        }
        var httpMethod = fromString(request.getMethod()).get();
        var roles = router.getRoles(httpMethod, route.get());
        if (routeString.isEmpty()) {
            redirect(request, response, toRouteString(ERROR_NOTFOUND));
            return;
        }
        if (!authHelper.isAuthorized(request, roles.get())) {
            redirect(request, response, toRouteString(ERROR_AUTH));
            return;
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // nothing
    }
}
