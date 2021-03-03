<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<jsp:include page="../parts/head.jsp" />
<body>
<jsp:include page="../parts/navbar.jsp" />
<h1>
    ${messages_myReceiptsViewOne}
</h1>
<form class="w-40">
    <div class="form-group mt-2">
        <label for="code-input">${messages_myReceiptsViewOneCode}</label>
        <input name="code" type="text" class="form-control" id="code-input"
               value="${receipt.code}" readonly>
    </div>
    <div class="form-group mt-2">
        <label for="created-time-input">${messages_myReceiptsViewOneCreatedAt}</label>
        <input name="createdTime" type="text" class="form-control" id="created-time-input"
               value="${receipt.createdTime}" readonly>
    </div>
    <div class="form-group mt-2">
        <label for="completed-time-input">${messages_myReceiptsViewOneClosedAt}</label>
        <input name="checkoutTime" type="text" class="form-control" id="completed-time-input"
               value="${receipt.checkoutTime}" readonly>
    </div>
    <div class="form-group mt-2">
        <label for="status-input">${messages_myReceiptsViewOneStatus}</label>
        <input name="status" type="text" class="form-control" id="status-input"
               value="${receipt.status}" readonly>
    </div>
    <div class="form-group mt-2">
        <label for="teller-input">${messages_myReceiptsViewOneTeller}</label>
        <input name="teller" type="text" class="form-control" id="teller-input"
               value="${receipt.user.username}" readonly>
    </div>
    <div class="form-group mt-2">
        <label for="sum-total-input">${messages_myReceiptsViewOneSumTotal}</label>
        <input name="sumTotal" type="text" class="form-control" id="sum-total-input"
               value="${receipt.sumTotal}" readonly>
    </div>
</form>
<h1>
    ${messages_myReceiptItems}
</h1>
<table class="table">
    <thead>
    <tr>
        <th scope="col">${messages_myReceiptItemsCode}</th>
        <th scope="col">${messages_myReceiptItemsName}</th>
        <th scope="col">${messages_myReceiptItemsAmount}</th>
        <th scope="col">${messages_myReceiptItemsAmountUnit}</th>
        <th scope="col">${messages_myReceiptItemsPrice}</th>
        <th scope="col">${messages_myReceiptItemsCost}</th>
        <th scope="col">${messages_myReceiptItemsActions}</th>
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
<jsp:include page="../parts/bodyjs.jsp" />
</body>
</html>