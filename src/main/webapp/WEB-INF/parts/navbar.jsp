<%@ page import="static com.polinakulyk.cashregister2.controller.api.HttpRoute.INDEX" %>
<%@ page import="static com.polinakulyk.cashregister2.controller.api.HttpRoute.PRODUCTS_LIST" %>
<%@ page import="static com.polinakulyk.cashregister2.controller.api.HttpRoute.RECEIPTS_LIST" %>
<%@ page import="static com.polinakulyk.cashregister2.controller.api.HttpRoute.MYRECEIPTS_LIST" %>
<%@ page import="static com.polinakulyk.cashregister2.controller.api.HttpRoute.REPORTS_LIST" %>
<%@ page import="static com.polinakulyk.cashregister2.controller.api.HttpRoute.AUTH_LOGIN" %>
<%@ page import="static com.polinakulyk.cashregister2.controller.api.HttpRoute.AUTH_LOGOUT" %>
<%@ page import="static com.polinakulyk.cashregister2.controller.api.HttpRoute.AUTH_LANG" %>
<jsp:useBean id="view" class="com.polinakulyk.cashregister2.view.NavBarView"/>
<%
    view.init(request);
%>
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
                <a href="<%=view.makeUrl(INDEX)%>"
                   class="nav-link <%=view.getNavMenuActive(INDEX)%>"
                   aria-current="page">${messages_menuHome}</a>
                <a href="<%=view.makeUrl(PRODUCTS_LIST)%>"
                   class="nav-link <%=view.getNavMenuActive(PRODUCTS_LIST)%>">${messages_menuProducts}</a>
                <a href="<%=view.makeUrl(RECEIPTS_LIST)%>"
                   class="nav-link <%=view.getNavMenuActive(RECEIPTS_LIST)%>">${messages_menuReceipts}</a>
                <a href="<%=view.makeUrl(MYRECEIPTS_LIST)%>"
                   class="nav-link <%=view.getNavMenuActive(MYRECEIPTS_LIST)%>">${messages_menuMyReceipts}</a>
                <a href="<%=view.makeUrl(REPORTS_LIST)%>"
                   class="nav-link <%=view.getNavMenuActive(REPORTS_LIST)%>">${messages_menuReports}</a>
                <a href="<%=view.makeUrl(AUTH_LOGIN)%>"
                   class="nav-link <%=view.getNavMenuActive(AUTH_LOGIN)%>">${messages_menuLogin}</a>
                <a href="<%=view.makeUrl(AUTH_LOGOUT)%>"
                   class="nav-link">${messages_menuLogout}</a>
                <form method="post"
                      action="<%=view.makeUrl(AUTH_LANG)%>?lang=EN&redirectRoute=<%=view.getRoute().get()%>"
                        class="mt-1">
                    <button type="submit" class="btn btn-info btn-sm">En</button>
                </form>
                <form method="post"
                      action="<%=view.makeUrl(AUTH_LANG)%>?lang=UA&redirectRoute=<%=view.getRoute().get()%>"
                        class="mt-1">
                    <button type="submit" class="btn btn-warning btn-sm">Укр</button>
                </form>
            </div>
        </div>
    </div>
</nav>
