package com.polinakulyk.cashregister2.controller;

import com.polinakulyk.cashregister2.controller.api.Command;
import com.polinakulyk.cashregister2.controller.api.HttpMethod;
import com.polinakulyk.cashregister2.controller.api.HttpRoute;
import com.polinakulyk.cashregister2.controller.command.JspCommand;
import com.polinakulyk.cashregister2.exception.DuplicateRouteException;
import com.polinakulyk.cashregister2.security.dto.UserRole;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.polinakulyk.cashregister2.controller.api.HttpRoute.toRouteString;
import static com.polinakulyk.cashregister2.util.Util.addPrefix;
import static com.polinakulyk.cashregister2.util.Util.removePrefix;
import static com.polinakulyk.cashregister2.util.Util.removeSuffix;

public class Router {

    public static final String JSP_CONTEXT_PATH = "/WEB-INF";
    public static final String INDEX_PATH = "/index";
    public static final String REDIRECT_PARAMS_ATTRIBUTE = "redirectParams";

    private final Map<Entry<HttpMethod, HttpRoute>, Entry<Command, Set<UserRole>>> routeToCommandMap =
            new ConcurrentHashMap<>();

    public Optional<Command> getCommand(HttpMethod method, HttpRoute route) {
        Entry<Command, Set<UserRole>> commandAndRoles = getCommandAndRoles(method, route);
        if (commandAndRoles == null) {
            return Optional.empty();
        }
        return Optional.of(commandAndRoles.getKey());
    }

    public Optional<Set<UserRole>> getRoles(
            HttpMethod method, HttpRoute route) {
        Entry<Command, Set<UserRole>> commandAndRoles = getCommandAndRoles(method, route);
        if (commandAndRoles == null) {
            return Optional.empty();
        }
        return Optional.of(commandAndRoles.getValue());
    }

    public Router addCommand(
            HttpMethod method, HttpRoute route, Command command, Set<UserRole> roles) {
        var routeKey = new SimpleEntry<>(method, route);
        if (routeToCommandMap.containsKey(routeKey)) {
            throw new DuplicateRouteException(method.name() + " " + toRouteString(route));
        }
        var commandAndRoles = new SimpleEntry<>(command, roles);
        routeToCommandMap.put(routeKey, commandAndRoles);
        return this;
    }

    public Router addCommand(
            HttpMethod method, HttpRoute route, String jspName, Set<UserRole> roles) {
        return addCommand(method, route, new JspCommand(jspName), roles);
    }

    public Router addCommandThenForward(
            HttpMethod method,
            HttpRoute route,
            Command command,
            String jspName,
            Set<UserRole> roles
    ) {
        return addCommand(method, route, JspCommand.commandThenJsp(command, jspName), roles);
    }

    private Entry<Command, Set<UserRole>> getCommandAndRoles(HttpMethod method, HttpRoute route) {
        var routeKey = new SimpleEntry<>(method, route);
        return routeToCommandMap.get(routeKey);
    }

    public static String makeUrl(HttpServletRequest request, HttpRoute route) {
        return request.getContextPath() + toRouteString(route);
    }

    public static String getRouteStringFromMainServlet(HttpServletRequest request) {
        var stringRoute = removePrefix(request.getRequestURI(), request.getContextPath());
        stringRoute = removeSuffix(stringRoute, INDEX_PATH);
        return addPrefix(stringRoute, "/");
    }

    public static String getRouteStringFromJsp(HttpServletRequest request) {
        var stringRoute = removePrefix(request.getRequestURI(), request.getContextPath());
        stringRoute = removeSuffix(stringRoute, ".jsp");
        stringRoute = removeSuffix(stringRoute, INDEX_PATH);
        stringRoute = removePrefix(stringRoute, JSP_CONTEXT_PATH);
        return addPrefix(stringRoute, "/");
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
}
