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

import static com.polinakulyk.cashregister2.controller.router.RouterHelper.httpRouteToRoutePath;

public abstract class Router {

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
            throw new DuplicateRouteException(method.name() + " " + httpRouteToRoutePath(route));
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
