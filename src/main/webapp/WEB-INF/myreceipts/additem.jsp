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
    <%= "Add My Receipt Item" %>
</h1>
<form class="w-40" method="post"
      action="<%=getRoutePath(request, MYRECEIPTS_ADDITEM_SEARCH)%>?receiptId=${receiptId}&productFilterKind=NAME">
    <div class="form-group mt-2">
        <label for="search-by-name-input">Search by Name</label>
        <input name="filterValue" type="text" class="form-control" id="search-by-name-input"
               placeholder="Enter product name">
    </div>
    <button type="submit" class="btn btn-primary mt-3">Search</button>
</form>
<form class="w-40" method="post"
      action="<%=getRoutePath(request, MYRECEIPTS_ADDITEM_SEARCH)%>?receiptId=${receiptId}&productFilterKind=CODE">
    <div class="form-group mt-2">
        <label for="search-by-code-input">Search by Code</label>
        <input name="filterValue" type="text" class="form-control" id="search-by-code-input"
               placeholder="Enter product code">
    </div>
    <button type="submit" class="btn btn-primary mt-3">Search</button>
</form>
<c:forEach var="product" items="${products}">
    <form class="w-40" method="post"
        <%-- receipt id to return to after adding receipt item --%>
          action="<%=getRoutePath(request, MYRECEIPTS_ADDITEM)%>?receiptId=${receiptId}">
        <div class="form-group mt-2 invisible">
            <label for="product-id-input">Product Id</label>
            <input name="productId" type="text" class="form-control" id="product-id-input"
                   placeholder="Enter product id"
                   value="${product.id}" readonly>
        </div>
        <div class="form-group mt-2">
            <label for="product-code-input">Product Code</label>
            <input name="productCode" type="text" class="form-control" id="product-code-input"
                   placeholder="Enter product code"
                   value="${product.code}" readonly>
        </div>
        <div class="form-group mt-2">
            <label for="product-name-input">Product Name</label>
            <input name="productName" type="text" class="form-control" id="product-name-input"
                   placeholder="Enter product name"
                   value="${product.name}" readonly>
        </div>
        <div class="form-group mt-2">
            <label for="product-amount-input">Amount</label>
            <input name="productAmount" type="text" class="form-control" id="product-amount-input"
                   placeholder="Enter amount">
        </div>
        <div class="form-group mt-2">
            <label for="product-amount-unit-input">Enter amount unit</label>
            <input name="productAmountUnit" type="text" class="form-control"
                   id="product-amount-unit-input"
                   placeholder="Enter amount unit"
                   value="${product.amountUnit}" readonly>
        </div>
        <div class="form-group mt-2">
            <label for="product-price-input">Enter price</label>
            <input name="productPrice" type="text" class="form-control" id="product-price-input"
                   placeholder="Enter price"
                   value="${product.price}" readonly>
        </div>
        <button type="submit" class="btn btn-primary mt-3">Save</button>
    </form>
</c:forEach>
<jsp:include page="../parts/bodyjs.jsp"/>
</body>
</html>