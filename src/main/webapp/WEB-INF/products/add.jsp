<%@ page import="static com.polinakulyk.cashregister2.controller.dto.HttpRoute.PRODUCTS_ADD" %>
<%@ page
        import="static com.polinakulyk.cashregister2.controller.router.RouterHelper.getRoutePath" %>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<jsp:include page="../parts/head.jsp"/>
<body>
<jsp:include page="../parts/navbar.jsp"/>
<h1>
    <%= "Add Product" %>
</h1>
<form class="w-40" method="post" action="<%=getRoutePath(request, PRODUCTS_ADD)%>">
    <div class="form-group mt-2">
        <label for="code-input">${messages_addProductCode}</label>
        <input name="code" type="text" class="form-control" id="code-input"
               placeholder="${messages_addProductPlaceholderCode}">
    </div>
    <div class="form-group mt-2">
        <label for="name-input">${messages_addProductName}</label>
        <input name="name" type="text" class="form-control" id="name-input"
               placeholder="${messages_addProductPlaceholderName}">
    </div>
    <div class="form-group mt-2">
        <label for="category-input">${messages_addProductCategory}</label>
        <input name="category" type="text" class="form-control" id="category-input"
               placeholder="${messages_addProductPlaceholderCategory}">
    </div>
    <div class="form-group mt-2">
        <label for="price-input">${messages_addProductPrice}</label>
        <input name="price" type="text" class="form-control" id="price-input"
               placeholder="${messages_addProductPlaceholderPrice}">
    </div>
    <div class="form-group mt-2">
        <label for="amount-available-input">${messages_addProductAvailable}</label>
        <input name="amountAvailable" type="text" class="form-control" id="amount-available-input"
               placeholder="${messages_addProductPlaceholderAvailable}">
    </div>
    <div class="form-group mt-2">
        <label for="amount-unit-input">${messages_addProductAmountUnit}</label>
        <input name="amountUnit" type="text" class="form-control" id="amount-unit-input"
               placeholder="${messages_addProductPlaceholderAmountUnit}">
    </div>
    <div class="form-group mt-2">
        <label for="details-input">${messages_addProductDetails}</label>
        <input name="details" type="text" class="form-control" id="details-input"
               placeholder="${messages_addProductPlaceholderDetails}">
    </div>
    <button type="submit" class="btn btn-primary mt-3">${messages_addProductSave}</button>
</form>
<jsp:include page="../parts/bodyjs.jsp"/>
</body>
</html>