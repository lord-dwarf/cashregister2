package com.polinakulyk.cashregister2.controller;

import com.polinakulyk.cashregister2.controller.api.Command;
import com.polinakulyk.cashregister2.controller.api.HttpMethod;
import com.polinakulyk.cashregister2.controller.api.HttpRoute;
import com.polinakulyk.cashregister2.controller.command.AddReceiptCommand;
import com.polinakulyk.cashregister2.controller.command.GetAuthLogoutCommand;
import com.polinakulyk.cashregister2.controller.command.ListMyReceiptsCommand;
import com.polinakulyk.cashregister2.controller.command.GetReceiptCommand;
import com.polinakulyk.cashregister2.controller.command.ListProductsCommand;
import com.polinakulyk.cashregister2.controller.command.ListReceiptsCommand;
import com.polinakulyk.cashregister2.controller.command.GetReportsXCommand;
import com.polinakulyk.cashregister2.controller.command.GetReportsZCommand;
import com.polinakulyk.cashregister2.controller.command.JspCommand;
import com.polinakulyk.cashregister2.controller.command.AddProductCommand;
import com.polinakulyk.cashregister2.controller.command.PostAuthLoginCommand;
import com.polinakulyk.cashregister2.controller.command.GetProductCommand;
import com.polinakulyk.cashregister2.controller.command.UpdateProductCommand;
import com.polinakulyk.cashregister2.exception.DuplicateRouteException;
import com.polinakulyk.cashregister2.security.dto.UserRole;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.polinakulyk.cashregister2.controller.api.HttpMethod.GET;
import static com.polinakulyk.cashregister2.controller.api.HttpMethod.POST;
import static com.polinakulyk.cashregister2.controller.api.HttpRoute.*;
import static com.polinakulyk.cashregister2.controller.api.HttpRoute.AUTH_LOGIN;
import static com.polinakulyk.cashregister2.controller.api.HttpRoute.AUTH_LOGOUT;
import static com.polinakulyk.cashregister2.controller.api.HttpRoute.ERROR_AUTH;
import static com.polinakulyk.cashregister2.controller.api.HttpRoute.ERROR_CLIENT;
import static com.polinakulyk.cashregister2.controller.api.HttpRoute.ERROR_NOTFOUND;
import static com.polinakulyk.cashregister2.controller.api.HttpRoute.ERROR_SERVER;
import static com.polinakulyk.cashregister2.controller.api.HttpRoute.INDEX;
import static com.polinakulyk.cashregister2.controller.api.HttpRoute.MYRECEIPTS_LIST;
import static com.polinakulyk.cashregister2.controller.api.HttpRoute.MYRECEIPTS_VIEW;
import static com.polinakulyk.cashregister2.controller.api.HttpRoute.PRODUCTS_LIST;
import static com.polinakulyk.cashregister2.controller.api.HttpRoute.PRODUCTS_VIEW;
import static com.polinakulyk.cashregister2.controller.api.HttpRoute.RECEIPTS_LIST;
import static com.polinakulyk.cashregister2.controller.api.HttpRoute.RECEIPTS_VIEW;
import static com.polinakulyk.cashregister2.controller.api.HttpRoute.REPORTS_LIST;
import static com.polinakulyk.cashregister2.security.dto.UserRole.GUEST;
import static com.polinakulyk.cashregister2.security.dto.UserRole.MERCH;
import static com.polinakulyk.cashregister2.security.dto.UserRole.SR_TELLER;
import static com.polinakulyk.cashregister2.security.dto.UserRole.any;
import static com.polinakulyk.cashregister2.security.dto.UserRole.authenticated;
import static com.polinakulyk.cashregister2.security.dto.UserRole.tellers;
import static com.polinakulyk.cashregister2.util.CashRegisterUtil.addPrefix;
import static com.polinakulyk.cashregister2.util.CashRegisterUtil.removePrefix;
import static com.polinakulyk.cashregister2.util.CashRegisterUtil.removeSuffix;

public final class MainRouter {

    public static final String JSP_CONTEXT_PATH = "/WEB-INF";
    public static final String INDEX_PATH = "/index";

    public static class Router {
        public static final MainRouter INSTANCE = new MainRouter();
    }

    private final Map<Entry<HttpMethod, HttpRoute>, Entry<Command, Set<UserRole>>> routeToCommandMap =
            new ConcurrentHashMap<>();

    private MainRouter() {
        // Home
        addCommand(GET, INDEX, "index.jsp", any());
        // Products
        addCommandThenForward(GET, PRODUCTS_LIST, new ListProductsCommand(), "products/list.jsp", authenticated());
        addCommand(GET, PRODUCTS_ADD, "products/add.jsp", Set.of(MERCH));
        addCommand(POST, PRODUCTS_ADD, new AddProductCommand(), Set.of(MERCH));
        addCommandThenForward(GET, PRODUCTS_VIEW, new GetProductCommand(), "products/view.jsp", authenticated());
        addCommandThenForward(GET, PRODUCTS_EDIT, new GetProductCommand(), "products/edit.jsp", Set.of(MERCH));
        addCommand(POST, PRODUCTS_EDIT, new UpdateProductCommand(), Set.of(MERCH));
        // Receipts
        addCommandThenForward(GET, RECEIPTS_LIST, new ListReceiptsCommand(), "receipts/list.jsp", Set.of(SR_TELLER));
        addCommandThenForward(GET, RECEIPTS_VIEW, new GetReceiptCommand(), "receipts/view.jsp", Set.of(SR_TELLER));
        addCommandThenForward(GET, RECEIPTS_EDIT, new GetReceiptCommand(), "receipts/edit.jsp", Set.of(SR_TELLER));
        // My Receipts
        addCommandThenForward(GET, MYRECEIPTS_LIST, new ListMyReceiptsCommand(), "myreceipts/list.jsp", tellers());
        addCommandThenForward(GET, MYRECEIPTS_VIEW, new GetReceiptCommand(), "myreceipts/view.jsp", tellers());
        addCommandThenForward(GET, MYRECEIPTS_EDIT, new GetReceiptCommand(), "myreceipts/edit.jsp", tellers());
        addCommandThenForward(GET, MYRECEIPTS_ADD, new AddReceiptCommand(), "myreceipts/add.jsp", tellers());
        // Reports
        addCommand(GET, REPORTS_LIST, "reports/list.jsp", Set.of(SR_TELLER));
        addCommand(GET, REPORTS_X, new GetReportsXCommand(), Set.of(SR_TELLER));
        addCommand(GET, REPORTS_Z, new GetReportsZCommand(), Set.of(SR_TELLER));
        // Auth
        addCommand(GET, AUTH_LOGIN, "auth/login.jsp", Set.of(GUEST));
        addCommand(POST, AUTH_LOGIN, new PostAuthLoginCommand(), any());
        addCommand(GET, AUTH_LOGOUT, new GetAuthLogoutCommand(), authenticated());
        // Errors
        addCommand(GET, ERROR_CLIENT, "error/client.jsp", any());
        addCommand(GET, ERROR_AUTH, "error/auth.jsp", any());
        addCommand(GET, ERROR_NOTFOUND, "error/notfound.jsp", any());
        addCommand(GET, ERROR_SERVER, "error/server.jsp", any());
    }

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

    public MainRouter addCommand(
            HttpMethod method, HttpRoute route, Command command, Set<UserRole> roles) {
        var routeKey = new SimpleEntry<>(method, route);
        if (routeToCommandMap.containsKey(routeKey)) {
            throw new DuplicateRouteException(method.name() + " " + toRouteString(route));
        }
        var commandAndRoles = new SimpleEntry<>(command, roles);
        routeToCommandMap.put(routeKey, commandAndRoles);
        return this;
    }

    public MainRouter addCommand(
            HttpMethod method, HttpRoute route, String jspName, Set<UserRole> roles) {
        return addCommand(method, route, new JspCommand(jspName), roles);
    }

    public MainRouter addCommandThenForward(
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
            HttpServletRequest request, HttpServletResponse response, HttpRoute route)
            throws IOException {
        response.sendRedirect(makeUrl(request, route));
    }
}
