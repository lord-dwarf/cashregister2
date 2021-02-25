<%@ page import="static com.polinakulyk.cashregister2.controller.api.HttpRoute.INDEX" %>
<%@ page import="static com.polinakulyk.cashregister2.controller.api.HttpRoute.PRODUCTS_LIST" %>
<%@ page import="static com.polinakulyk.cashregister2.controller.api.HttpRoute.RECEIPTS_LIST" %>
<%@ page import="static com.polinakulyk.cashregister2.controller.api.HttpRoute.MYRECEIPTS_LIST" %>
<%@ page import="static com.polinakulyk.cashregister2.controller.api.HttpRoute.REPORTS_LIST" %>
<%@ page import="static com.polinakulyk.cashregister2.controller.api.HttpRoute.AUTH_LOGIN" %>
<%@ page import="static com.polinakulyk.cashregister2.controller.api.HttpRoute.AUTH_LOGOUT" %>
<jsp:useBean id="navbar" class="com.polinakulyk.cashregister2.view.NavBarView"/>
<%
    navbar.init(request);
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
                <a href="<%=navbar.makeUrl(INDEX)%>"
                   class="nav-link <%=navbar.getNavMenuActive(INDEX)%>"
                   aria-current="page">Home</a>
                <a href="<%=navbar.makeUrl(PRODUCTS_LIST)%>"
                   class="nav-link <%=navbar.getNavMenuActive(PRODUCTS_LIST)%>">Products</a>
                <a href="<%=navbar.makeUrl(RECEIPTS_LIST)%>"
                   class="nav-link <%=navbar.getNavMenuActive(RECEIPTS_LIST)%>">Receipts</a>
                <a href="<%=navbar.makeUrl(MYRECEIPTS_LIST)%>"
                   class="nav-link <%=navbar.getNavMenuActive(MYRECEIPTS_LIST)%>">My Receipts</a>
                <a href="<%=navbar.makeUrl(REPORTS_LIST)%>"
                   class="nav-link <%=navbar.getNavMenuActive(REPORTS_LIST)%>">Reports</a>
                <a href="<%=navbar.makeUrl(AUTH_LOGIN)%>"
                   class="nav-link <%=navbar.getNavMenuActive(AUTH_LOGIN)%>">Login</a>
                <a href="<%=navbar.makeUrl(AUTH_LOGOUT)%>"
                   class="nav-link">Logout</a>
            </div>
        </div>
    </div>
</nav>
