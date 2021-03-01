package com.polinakulyk.cashregister2.controller.router;

import com.polinakulyk.cashregister2.controller.dto.HttpRoute;
import com.polinakulyk.cashregister2.controller.dto.RouteString;
import com.polinakulyk.cashregister2.exception.CashRegisterException;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.polinakulyk.cashregister2.util.Util.addPrefix;
import static com.polinakulyk.cashregister2.util.Util.quote;
import static com.polinakulyk.cashregister2.util.Util.removePrefix;
import static com.polinakulyk.cashregister2.util.Util.removeSuffix;

public final class RouterHelper {

    public static final String INDEX_PATH = "/index";
    public static final String JSP_CONTEXT_PATH = "/WEB-INF";

    private RouterHelper() {
        throw new UnsupportedOperationException("Cannot instantiate");
    }

    public static String getRoutePath(HttpServletRequest request, HttpRoute route) {
        return request.getContextPath() + routePathFromHttpRoute(route);
    }

    /**
     * Takes incoming HTTP request to servlet, and gets rid from servlet context prefix.
     * The resulting route path is guaranteed to start with "/".
     * <p>
     * Examples:
     * "/cashregister_war" -> "/"
     * "/cashregister_war/products/list" -> "/products/list"
     * <p>
     * Important: is intended to be called for requests handled by
     * {@link com.polinakulyk.cashregister2.controller.MainServlet}
     *
     * @param request
     * @return request route path
     */
    public static String getRoutePathFromServletRequest(HttpServletRequest request) {
        var routePath = removePrefix(request.getRequestURI(), request.getContextPath());
        return addPrefix(routePath, "/");
    }

    /**
     * Converts route path
     * (see {@link RouterHelper#getRoutePathFromServletRequest} for details of what a route path is)
     * to the known {@link HttpRoute}.
     * <p>
     * Examples:
     * "/" -> HttpRoute.INDEX
     * "/products/list" -> HttpRoute.PRODUCTS_LIST
     * <p>
     *
     * @param routePath
     * @return
     */
    public static Optional<HttpRoute> httpRouteFromRoutePath(String routePath) {
        if ("/".equals(routePath)) {
            return Optional.of(HttpRoute.INDEX);
        }
        routePath = removePrefix(routePath, "/");
        // "products/list" -> "PRODUCTS_LIST"
        var httpRouteString = routePath.replaceAll("/", "_").toUpperCase();
        return HttpRoute.fromString(httpRouteString);
    }

    /**
     * Converts the known {@link HttpRoute} to route path
     * (see {@link RouterHelper#getRoutePathFromServletRequest} for details of what a route path is).
     *
     * <p>
     * Examples:
     * HttpRoute.INDEX -> "/"
     * HttpRoute.PRODUCTS_LIST -> "/products/list"
     * <p>
     *
     * @param route
     * @return
     */
    public static String routePathFromHttpRoute(HttpRoute route) {
        if (HttpRoute.INDEX == route) {
            return "/";
        }
        var routeString = route.name().toLowerCase().replaceAll("_", "/");
        return addPrefix(routeString, "/");
    }

    /**
     * Takes incoming HTTP request to servlet that handles JSPs, and gets rid from
     * servlet context prefix "/WEB-INF", ".jsp" and "/index" suffixes. The resulting route path
     * is guaranteed to start with "/".
     * <p>
     * Examples:
     * "/cashregister_war/WEB-INF/index.jsp" -> "/"
     * "/cashregister_war/WEB-INF/products/list.jsp" -> "/products/list"
     * <p>
     * Important: is intended to be called for requests handled within JSPs.
     *
     * @param request
     * @return request route path
     */
    public static String getRoutePathFromJspRequest(HttpServletRequest request) {
        var routePath = removePrefix(request.getRequestURI(), request.getContextPath());
        routePath = removeSuffix(routePath, ".jsp");
        routePath = removeSuffix(routePath, INDEX_PATH);
        routePath = removePrefix(routePath, JSP_CONTEXT_PATH);
        return addPrefix(routePath, "/");
    }

    /**
     * Takes incoming HTTP request to servlet that handles JSPs, and finds known
     * {@link HttpRoute} route that corresponds to the request path.
     * <p>
     * Examples:
     * "/cashregister_war/WEB-INF/index.jsp" -> HttpRoute.INDEX
     * "/cashregister_war/WEB-INF/products/list.jsp" -> HttpRoute.PRODUCTS_LIST
     * <p>
     * Important: is intended to be called for requests handled within JSPs.
     *
     * @param request
     * @return request route
     */
    public static HttpRoute getRouteFromJspRequest(HttpServletRequest request) {
        var routePath = getRoutePathFromJspRequest(request);
        return httpRouteFromRoutePath(routePath).orElseThrow(() -> new CashRegisterException(
                quote("Can't get current route from JSP", routePath)));
    }

    public static void forwardToJsp(
            HttpServletRequest request, HttpServletResponse response, String jspName)
            throws ServletException, IOException {
        jspName = addPrefix(jspName, "/");
        request.getRequestDispatcher(JSP_CONTEXT_PATH + jspName).forward(request, response);
    }

    /**
     * Send redirect to client through HTTP response, to the URL specified via known {@link HttpRoute}.
     * <p>
     * Example of redirect to a custom 404 error page:
     * redirect(request, response, ERROR_NOTFOUND);
     *
     * @param request
     * @param response
     * @param httpRoute
     * @throws IOException
     */
    public static void redirect(
            HttpServletRequest request, HttpServletResponse response, HttpRoute httpRoute)
            throws IOException {
        response.sendRedirect(request.getContextPath() + routePathFromHttpRoute(httpRoute));
    }

    /**
     * Send redirect to client through HTTP response, to the URL specified
     * via known {@link HttpRoute} + optional query parameters string (see {@link RouteString}).
     * <p>
     * Example of redirect to a custom validation error page (400) with error message:
     * var redirectQueryString = "?errorMessage=" + toBase64(e.getMessage());
     * redirect(request, response, RouteString.of(errorRedirect, redirectQueryString));
     *
     * @param request
     * @param response
     * @param routeString
     * @throws IOException
     */
    public static void redirect(
            HttpServletRequest request, HttpServletResponse response, RouteString routeString)
            throws IOException {
        response.sendRedirect(
                request.getContextPath()
                        + routePathFromHttpRoute(routeString.getHttpRoute())
                        + routeString.getQueryString().orElse("")
        );
    }
}
