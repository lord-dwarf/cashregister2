<%@ page import="com.polinakulyk.cashregister2.controller.api.HttpRoute" %>
<%@ page import="static com.polinakulyk.cashregister2.controller.api.HttpRoute.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="view" class="com.polinakulyk.cashregister2.view.ReceiptsView"/>
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
    <%= "All Receipts" %>
</h1>
<table class="table">
    <thead>
    <tr>
        <th scope="col">Code</th>
        <th scope="col">Created At</th>
        <th scope="col">Closed At</th>
        <th scope="col">Status</th>
        <th scope="col">Teller</th>
        <th scope="col">Sum Total</th>
        <th scope="col">Actions</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="receipt" items="${receipts}">
        <tr>
            <td>
                <c:out value="${receipt.code}"/>
            </td>
            <td>
                <c:out value="${receipt.createdTime}"/>
            </td>
            <td>
                <c:out value="${receipt.checkoutTime}"/>
            </td>
            <td>
                <c:out value="${receipt.status}"/>
            </td>
            <td>
                <c:out value="${receipt.user.username}"/>
            </td>
            <td>
                <c:out value="${receipt.sumTotal}"/>
            </td>
            <td>
                <a class="btn btn-outline-primary"
                   href="<%=view.makeUrl(RECEIPTS_VIEW)%>?receiptId=${receipt.id}"
                   role="button">View</a>
                <a class="btn btn-outline-warning"
                   href="<%=view.makeUrl(RECEIPTS_EDIT)%>?receiptId=${receipt.id}"
                   role="button">Edit</a>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<jsp:include page="../parts/bodyjs.jsp"/>
</body>
</html>