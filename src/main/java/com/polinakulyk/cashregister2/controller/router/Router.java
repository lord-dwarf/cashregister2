package com.polinakulyk.cashregister2.controller.router;

import com.polinakulyk.cashregister2.controller.dto.HttpMethod;
import com.polinakulyk.cashregister2.controller.dto.HttpRoute;
import com.polinakulyk.cashregister2.controller.command.BiCommand;
import com.polinakulyk.cashregister2.controller.command.Command;
import com.polinakulyk.cashregister2.controller.command.JspCommand;
import com.polinakulyk.cashregister2.exception.DuplicateRouteException;
import com.polinakulyk.cashregister2.security.dto.UserRole;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.polinakulyk.cashregister2.controller.router.RouterHelper.routePathFromHttpRoute;

/**
 * Holds mappings between:
 * {@link HttpMethod} + {@link HttpRoute},
 * AND
 * {@link Command} + authorized {@link UserRole}.
 * <p>
 * Provides builder methods for creating routes, and methods to query commands and roles.
 * <p>
 * Examples to create a route:
 * addForwardToJsp(GET, INDEX, "index.jsp", any());
 * addForwardToJsp(GET, REPORTS_LIST, "reports/list.jsp", Set.of(SR_TELLER));
 * <p>
 * Example to query command:
 * getCommand(GET, PRODUCTS_LIST)
 * <p>
 * Example to query roles:
 * getRoles(GET, REPORTS_LIST)
 */
public abstract class Router {

    private final Map<Entry<HttpMethod, HttpRoute>, Entry<Command, Set<UserRole>>> routeToCommandMap =
            new ConcurrentHashMap<>();

    /**
     * Queries command associated with {@link HttpMethod} and {@link HttpRoute}.
     *
     * @param method
     * @param route
     * @return
     */
    public Optional<Command> getCommand(HttpMethod method, HttpRoute route) {
        Entry<Command, Set<UserRole>> commandAndRoles = getCommandAndRoles(method, route);
        if (commandAndRoles == null) {
            return Optional.empty();
        }
        // key = command
        return Optional.of(commandAndRoles.getKey());
    }

    /**
     * Queries roles associated with {@link HttpMethod} and {@link HttpRoute}.
     *
     * @param method
     * @param route
     * @return
     */
    public Optional<Set<UserRole>> getRoles(
            HttpMethod method, HttpRoute route) {
        Entry<Command, Set<UserRole>> commandAndRoles = getCommandAndRoles(method, route);
        if (commandAndRoles == null) {
            return Optional.empty();
        }
        // value = set of roles
        return Optional.of(commandAndRoles.getValue());
    }

    public Router addCommand(
            HttpMethod method, HttpRoute route, Command command, Set<UserRole> roles) {
        var routeKey = new SimpleEntry<>(method, route);
        if (routeToCommandMap.containsKey(routeKey)) {
            throw new DuplicateRouteException(method.name() + " " + routePathFromHttpRoute(route));
        }
        var commandAndRoles = new SimpleEntry<>(command, roles);
        routeToCommandMap.put(routeKey, commandAndRoles);
        return this;
    }

    public Router addForwardToJsp(
            HttpMethod method, HttpRoute route, String jspName, Set<UserRole> roles) {
        return addCommand(method, route, JspCommand.of(jspName), roles);
    }

    public Router addCommandThenForwardToJsp(
            HttpMethod method,
            HttpRoute route,
            Command command,
            String jspName,
            Set<UserRole> roles
    ) {
        var biCommand = BiCommand.of(command, JspCommand.of(jspName));
        return addCommand(method, route, biCommand, roles);
    }

    private Entry<Command, Set<UserRole>> getCommandAndRoles(HttpMethod method, HttpRoute route) {
        var routeKey = new SimpleEntry<>(method, route);
        return routeToCommandMap.get(routeKey);
    }
}
