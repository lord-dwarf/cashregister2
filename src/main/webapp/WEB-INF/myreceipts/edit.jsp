<%@ page
        import="static com.polinakulyk.cashregister2.controller.api.HttpRoute.MYRECEIPTS_ADDITEM" %>
<%@ page
        import="static com.polinakulyk.cashregister2.controller.api.HttpRoute.MYRECEIPTS_COMPLETE" %>
<%@ page import="static com.polinakulyk.cashregister2.controller.api.HttpRoute.MYRECEIPTS_CANCEL" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="view" class="com.polinakulyk.cashregister2.view.MyReceiptsView"/>
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
    <%= "Edit My Receipt" %>
</h1>
<div class="container">
    <div class="row justify-content-left">
        <div class="col-4">
            <form method="post"
                  action="<%=view.makeUrl(MYRECEIPTS_CANCEL)%>?receiptId=${receipt.id}">
                <button type="submit" class="btn btn-primary">Cancel Receipt</button>
            </form>
        </div>
        <div class="col-4">
            <form class="mt-2" method="post"
                  action="<%=view.makeUrl(MYRECEIPTS_COMPLETE)%>?receiptId=${receipt.id}">
                <button type="submit" class="btn btn-primary">Complete Receipt</button>
            </form>
        </div>
    </div>
</div>
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
    <%= "My Receipt Items" %>
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
                <!-- Actions -->
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<a class="btn btn-primary" href="<%=view.makeUrl(MYRECEIPTS_ADDITEM)%>?receiptId=${receipt.id}" role="button">Add Item</a>
<jsp:include page="../parts/bodyjs.jsp"/>
</body>
</html>