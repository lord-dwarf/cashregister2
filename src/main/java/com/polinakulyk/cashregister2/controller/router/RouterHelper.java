package com.polinakulyk.cashregister2.controller.router;

import com.polinakulyk.cashregister2.controller.api.HttpRoute;
import com.polinakulyk.cashregister2.controller.api.RouteString;
import com.polinakulyk.cashregister2.exception.CashRegisterException;
import com.polinakulyk.cashregister2.util.Util;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.polinakulyk.cashregister2.util.Util.addPrefix;
import static com.polinakulyk.cashregister2.util.Util.quote;
import static com.polinakulyk.cashregister2.util.Util.removePrefix;
import static com.polinakulyk.cashregister2.util.Util.removeSuffix;
import static java.util.Arrays.stream;

public final class RouterHelper {

    public static final String INDEX_PATH = "/index";
    public static final String JSP_CONTEXT_PATH = "/WEB-INF";

    private RouterHelper() {
        throw new UnsupportedOperationException("Cannot instantiate");
    }

    public static String getRoutePath(HttpServletRequest request, HttpRoute route) {
        return request.getContextPath() + httpRouteToRoutePath(route);
    }

    public static String getCurrentRoutePathFromServlet(HttpServletRequest request) {
        var routePath = removePrefix(request.getRequestURI(), request.getContextPath());
        routePath = removeSuffix(routePath, INDEX_PATH);
        return addPrefix(routePath, "/");
    }

    public static String getCurrentRoutePathFromJsp(HttpServletRequest request) {
        var routePath = removePrefix(request.getRequestURI(), request.getContextPath());
        routePath = removeSuffix(routePath, ".jsp");
        routePath = removeSuffix(routePath, INDEX_PATH);
        routePath = removePrefix(routePath, JSP_CONTEXT_PATH);
        return addPrefix(routePath, "/");
    }

    public static HttpRoute getCurrentRouteFromJsp(HttpServletRequest request) {
        var routePath = getCurrentRoutePathFromJsp(request);
        return routePathToHttpRoute(routePath).orElseThrow(() -> new CashRegisterException(
                quote("Can't get current route from JSP", routePath)));
    }

    public static void forwardToJsp(
            HttpServletRequest request, HttpServletResponse response, String jspName)
            throws ServletException, IOException {
        jspName = addPrefix(jspName, "/");
        request.getRequestDispatcher(JSP_CONTEXT_PATH + jspName).forward(request, response);
    }

    public static void redirect(
            HttpServletRequest request, HttpServletResponse response, String routeString)
            throws IOException {
        response.sendRedirect(request.getContextPath() + routeString);
    }

    public static void redirect(
            HttpServletRequest request, HttpServletResponse response, HttpRoute httpRoute)
            throws IOException {
        response.sendRedirect(request.getContextPath() + httpRouteToRoutePath(httpRoute));
    }

    public static void redirect(
            HttpServletRequest request, HttpServletResponse response, RouteString routeString)
            throws IOException {
        response.sendRedirect(
                request.getContextPath()
                        + httpRouteToRoutePath(routeString.getHttpRoute())
                + routeString.getQueryString().orElse("")
        );
    }

    public static String httpRouteToRoutePath(HttpRoute route) {
        if (HttpRoute.INDEX == route) {
            return "/";
        }
        var routeString = route.name().toLowerCase().replaceAll("_", "/");
        return addPrefix(routeString, "/");
    }

    public static Optional<HttpRoute> routePathToHttpRoute(String routePath) {
        if ("/".equals(routePath)) {
            return Optional.of(HttpRoute.INDEX);
        }
        routePath = removeSuffix(routePath, INDEX_PATH);
        routePath = removePrefix(routePath, "/");
        var httpRouteString = routePath.replaceAll("/", "_");
        return Arrays.stream(HttpRoute.values())
                .filter(httpRoute -> httpRouteString.equalsIgnoreCase(httpRoute.name()))
                .findFirst();
    }
}
