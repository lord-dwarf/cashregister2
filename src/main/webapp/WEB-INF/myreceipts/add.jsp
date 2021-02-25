<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="view" class="com.polinakulyk.cashregister2.view.MyReceiptsView"/>
<%
    view.init(request);
%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<jsp:include page="../parts/head.jsp" />
<body>
<jsp:include page="../parts/navbar.jsp" />
<h1>
    <%= "My Receipts - ADD" %>
</h1>
<form class="w-40">
    <div class="form-group mt-2">
        <label for="code-input">Code</label>
        <input name="code" type="text" class="form-control" id="code-input"
               value="${receipt.code}" readonly>
    </div>
    <div class="form-group mt-2">
        <label for="created-time-input">Created at</label>
        <input name="createdTime" type="text" class="form-control" id="created-time-input"
               value="${receipt.createdTime}" readonly>
    </div>
    <div class="form-group mt-2">
        <label for="completed-time-input">Closed at</label>
        <input name="checkoutTime" type="text" class="form-control" id="completed-time-input"
               value="${receipt.checkoutTime}" readonly>
    </div>
    <div class="form-group mt-2">
        <label for="status-input">Status</label>
        <input name="status" type="text" class="form-control" id="status-input"
               value="${receipt.status}" readonly>
    </div>
    <div class="form-group mt-2">
        <label for="teller-input">Teller</label>
        <input name="teller" type="text" class="form-control" id="teller-input"
               value="${receipt.user.username}" readonly>
    </div>
    <div class="form-group mt-2">
        <label for="sum-total-input">Sum total</label>
        <input name="sumTotal" type="text" class="form-control" id="sum-total-input"
               value="${receipt.sumTotal}" readonly>
    </div>
</form>
<h1>
    <%= "RECEIPT ITEMS" %>
</h1>
<table class="table">
    <thead>
    <tr>
        <th scope="col">Code</th>
        <th scope="col">Name</th>
        <th scope="col">Amount</th>
        <th scope="col">Unit</th>
        <th scope="col">Price</th>
        <th scope="col">Cost</th>
        <th scope="col">Actions</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="receiptItem" items="${receipt.receiptItems}">
        <tr>
            <td>
                <c:out value="${receiptItem.product.code}"/>
            </td>
            <td>
                <c:out value="${receiptItem.name}"/>
            </td>
            <td>
                <c:out value="${receiptItem.amount}"/>
            </td>
            <td>
                <c:out value="${receiptItem.amountUnit}"/>
            </td>
            <td>
                <c:out value="${receiptItem.price}"/>
            </td>
            <td>
                <c:out value="${receiptItem.cost}"/>
            </td>
            <td>
                    <%--                <a class="btn btn-outline-primary"--%>
                    <%--                   href="<%=view.makeUrl(PRODUCTS_VIEW)%>?id=${product.id}"--%>
                    <%--                   role="button">View</a>--%>
                    <%--                <a class="btn btn-outline-warning"--%>
                    <%--                   href="<%=view.makeUrl(PRODUCTS_EDIT)%>?id=${product.id}"--%>
                    <%--                   role="button">Edit</a>--%>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<jsp:include page="../parts/bodyjs.jsp" />
</body>
</html>