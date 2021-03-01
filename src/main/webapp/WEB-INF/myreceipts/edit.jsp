<%@ page
        import="static com.polinakulyk.cashregister2.controller.dto.HttpRoute.MYRECEIPTS_ADDITEM" %>
<%@ page
        import="static com.polinakulyk.cashregister2.controller.dto.HttpRoute.MYRECEIPTS_COMPLETE" %>
<%@ page import="static com.polinakulyk.cashregister2.controller.dto.HttpRoute.MYRECEIPTS_CANCEL" %>
<%@ page import="com.polinakulyk.cashregister2.controller.router.RouterHelper" %>
<%@ page
        import="static com.polinakulyk.cashregister2.controller.router.RouterHelper.getRoutePath" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<jsp:include page="../parts/head.jsp"/>
<body>
<jsp:include page="../parts/navbar.jsp"/>
<h1>
    <%= "Edit My Receipt" %>
</h1>
<div class="container-fluid mt-2 px-0">
    <div class="row">
            <form class="box col-auto px-0" method="post"
                  action="<%=getRoutePath(request, MYRECEIPTS_COMPLETE)%>?receiptId=${receipt.id}">
                <button type="submit" class="btn btn-success">Complete</button>
            </form>
            <form class="box col-auto px-0" method="post"
                  action="<%=getRoutePath(request, MYRECEIPTS_CANCEL)%>?receiptId=${receipt.id}">
                <button type="submit" class="btn btn-danger">Cancel</button>
            </form>
            <div class="col w-100">
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
<a class="btn btn-primary" href="<%=getRoutePath(request, MYRECEIPTS_ADDITEM)%>?receiptId=${receipt.id}" role="button">Add Item</a>
<jsp:include page="../parts/bodyjs.jsp"/>
</body>
</html>