<%@ page
        import="static com.polinakulyk.cashregister2.controller.dto.HttpRoute.MYRECEIPTS_ADDITEM" %>
<%@ page
        import="static com.polinakulyk.cashregister2.controller.dto.HttpRoute.MYRECEIPTS_ADDITEM_SEARCH" %>
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
    ${messages_myReceiptAddItem}
</h1>
<form class="w-40" method="post"
      action="<%=getRoutePath(request, MYRECEIPTS_ADDITEM_SEARCH)%>?receiptId=${receiptId}&productFilterKind=NAME">
    <div class="form-group mt-2">
        <label for="search-by-name-input">${messages_myReceiptAddItemSearchName}</label>
        <input name="filterValue" type="text" class="form-control" id="search-by-name-input"
               placeholder="${messages_myReceiptAddItemSearchNamePlaceholder}">
    </div>
    <button type="submit" class="btn btn-primary mt-3">${messages_myReceiptAddItemSearch}</button>
</form>
<form class="w-40" method="post"
      action="<%=getRoutePath(request, MYRECEIPTS_ADDITEM_SEARCH)%>?receiptId=${receiptId}&productFilterKind=CODE">
    <div class="form-group mt-2">
        <label for="search-by-code-input">${messages_myReceiptAddItemSearchCode}</label>
        <input name="filterValue" type="text" class="form-control" id="search-by-code-input"
               placeholder="${messages_myReceiptAddItemSearchCodePlaceholder}">
    </div>
    <button type="submit" class="btn btn-primary mt-3">${messages_myReceiptAddItemSearch}</button>
</form>
<c:forEach var="product" items="${products}">
    <form class="w-40" method="post"
        <%-- receipt id to return to after adding receipt item --%>
          action="<%=getRoutePath(request, MYRECEIPTS_ADDITEM)%>?receiptId=${receiptId}">
        <div class="form-group mt-2 invisible">
            <label for="product-id-input">${messages_myReceiptAddItemId}</label>
            <input name="productId" type="text" class="form-control" id="product-id-input"
                   value="${product.id}" readonly>
        </div>
        <div class="form-group mt-2">
            <label for="product-code-input">${messages_myReceiptAddItemCode}</label>
            <input name="productCode" type="text" class="form-control" id="product-code-input"
                   value="${product.code}" readonly>
        </div>
        <div class="form-group mt-2">
            <label for="product-name-input">${messages_myReceiptAddItemName}</label>
            <input name="productName" type="text" class="form-control" id="product-name-input"
                   value="${product.name}" readonly>
        </div>
        <div class="form-group mt-2">
            <label for="product-amount-input">${messages_myReceiptAddItemAmount}</label>
            <input name="productAmount" type="text" class="form-control" id="product-amount-input">
        </div>
        <div class="form-group mt-2">
            <label for="product-amount-unit-input">${messages_myReceiptAddItemAmountUnit}</label>
            <input name="productAmountUnit" type="text" class="form-control"
                   id="product-amount-unit-input"
                   value="${product.amountUnit}" readonly>
        </div>
        <div class="form-group mt-2">
            <label for="product-price-input">${messages_myReceiptAddItemPrice}</label>
            <input name="productPrice" type="text" class="form-control" id="product-price-input"
                   value="${product.price}" readonly>
        </div>
        <button type="submit" class="btn btn-primary mt-3">${messages_myReceiptAddItemSave}</button>
    </form>
</c:forEach>
<jsp:include page="../parts/bodyjs.jsp"/>
</body>
</html>