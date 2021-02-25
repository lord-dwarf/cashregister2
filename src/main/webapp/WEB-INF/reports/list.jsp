<%@ page import="static com.polinakulyk.cashregister2.controller.api.HttpRoute.REPORTS_X" %>
<%@ page import="static com.polinakulyk.cashregister2.controller.api.HttpRoute.REPORTS_Z" %>
<jsp:useBean id="view" class="com.polinakulyk.cashregister2.view.ReportsView"/>
<%
    view.init(request);
%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<jsp:include page="../parts/head.jsp"/>
<body>
<jsp:include page="../parts/navbar.jsp"/>
<h1>
    <%= "Reports" %>
</h1>
<table class="table">
    <thead>
    <tr>
        <th scope="col">Name</th>
        <th scope="col">Actions</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td>X Report</td>
        <td>
            <a class="btn btn-outline-primary" href="<%=view.makeUrl(REPORTS_X)%>"
               role="button">Create</a>
        </td>
    </tr>
    <tr>
        <td>Z Report</td>
        <td>
            <a class="btn btn-outline-primary" href="<%=view.makeUrl(REPORTS_Z)%>"
               role="button">Create</a>
        </td>
    </tr>
    </tbody>
</table>
<jsp:include page="../parts/bodyjs.jsp"/>
</body>
</html>