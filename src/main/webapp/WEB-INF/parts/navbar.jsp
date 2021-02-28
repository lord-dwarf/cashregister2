<%@ page import="static com.polinakulyk.cashregister2.controller.api.HttpRoute.INDEX" %>
<%@ page import="static com.polinakulyk.cashregister2.controller.api.HttpRoute.PRODUCTS_LIST" %>
<%@ page import="static com.polinakulyk.cashregister2.controller.api.HttpRoute.RECEIPTS_LIST" %>
<%@ page import="static com.polinakulyk.cashregister2.controller.api.HttpRoute.MYRECEIPTS_LIST" %>
<%@ page import="static com.polinakulyk.cashregister2.controller.api.HttpRoute.REPORTS_LIST" %>
<%@ page import="static com.polinakulyk.cashregister2.controller.api.HttpRoute.AUTH_LOGIN" %>
<%@ page import="static com.polinakulyk.cashregister2.controller.api.HttpRoute.AUTH_LOGOUT" %>
<%@ page import="static com.polinakulyk.cashregister2.controller.api.HttpRoute.AUTH_LANG" %>
<%@ page import="com.polinakulyk.cashregister2.controller.router.RouterHelper" %>
<%@ page
        import="static com.polinakulyk.cashregister2.controller.router.RouterHelper.getRoutePath" %>
<%@ page
        import="static com.polinakulyk.cashregister2.controller.router.RouterHelper.getCurrentRouteFromJsp" %>

<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <div class="container-fluid">
        <button class="navbar-toggler"
                type="button"
                data-bs-toggle="collapse"
                data-bs-target="#navbarNavAltMarkup"
                aria-controls="navbarNavAltMarkup"
                aria-expanded="false"
                aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNavAltMarkup">
            <div class="navbar-nav">
                <a href="<%=getRoutePath(request, INDEX)%>"
                   class="nav-link"
                   aria-current="page">${messages_menuHome}</a>
                <a href="<%=getRoutePath(request, PRODUCTS_LIST)%>"
                   class="nav-link">${messages_menuProducts}</a>
                <a href="<%=getRoutePath(request, RECEIPTS_LIST)%>"
                   class="nav-link">${messages_menuReceipts}</a>
                <a href="<%=getRoutePath(request, MYRECEIPTS_LIST)%>"
                   class="nav-link">${messages_menuMyReceipts}</a>
                <a href="<%=getRoutePath(request, REPORTS_LIST)%>"
                   class="nav-link">${messages_menuReports}</a>
                <a href="<%=getRoutePath(request, AUTH_LOGIN)%>"
                   class="nav-link">${messages_menuLogin}</a>
                <a href="<%=getRoutePath(request, AUTH_LOGOUT)%>"
                   class="nav-link">${messages_menuLogout}</a>
                <form method="post"
                      action="<%=getRoutePath(request, AUTH_LANG)%>?lang=EN&redirectRoute=<%=getCurrentRouteFromJsp(request)%>"
                        class="mt-1">
                    <button type="submit" class="btn btn-info btn-sm">En</button>
                </form>
                <form method="post"
                      action="<%=getRoutePath(request, AUTH_LANG)%>?lang=UA&redirectRoute=<%=getCurrentRouteFromJsp(request)%>"
                        class="mt-1">
                    <button type="submit" class="btn btn-warning btn-sm">Укр</button>
                </form>
            </div>
        </div>
    </div>
</nav>
