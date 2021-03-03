<%@ page import="static com.polinakulyk.cashregister2.controller.dto.HttpRoute.PRODUCTS_EDIT" %>
<%@ page import="static com.polinakulyk.cashregister2.controller.router.RouterHelper.*" %>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<jsp:include page="../parts/head.jsp"/>
<body>
<jsp:include page="../parts/navbar.jsp"/>
<h1>
    ${messages_editProduct}
</h1>
<form class="w-40" method="post"
      action="<%=getRoutePath(request, PRODUCTS_EDIT)%>?id=${product.id}">
    <div class="form-group mt-2">
        <label for="code-input">${messages_editProductCode}</label>
        <input name="code" type="text" class="form-control" id="code-input"
               placeholder="${messages_editProductPlaceholderCode}" value="${product.code}">
    </div>
    <div class="form-group mt-2">
        <label for="name-input">${messages_editProductName}</label>
        <input name="name" type="text" class="form-control" id="name-input"
               placeholder="${messages_editProductPlaceholderName}" value="${product.name}">
    </div>
    <div class="form-group mt-2">
        <label for="category-input">${messages_editProductCategory}</label>
        <input name="category" type="text" class="form-control" id="category-input"
               placeholder="${messages_editProductPlaceholderCategory}" value="${product.category}">
    </div>
    <div class="form-group mt-2">
        <label for="price-input">${messages_editProductPrice}</label>
        <input name="price" type="text" class="form-control" id="price-input"
               placeholder="${messages_editProductPlaceholderPrice}" value="${product.price}">
    </div>
    <div class="form-group mt-2">
        <label for="amount-available-input">${messages_editProductAvailable}</label>
        <input name="amountAvailable" type="text" class="form-control" id="amount-available-input"
               placeholder="${messages_editProductPlaceholderAvailable}"
               value="${product.amountAvailable}">
    </div>
    <div class="form-group mt-2">
        <label for="amount-unit-input">${messages_editProductAmountUnit}</label>
        <input name="amountUnit" type="text" class="form-control" id="amount-unit-input"
               placeholder="${messages_editProductPlaceholderAmountUnit}"
               value="${product.amountUnit}">
    </div>
    <div class="form-group mt-2">
        <label for="details-input">${messages_editProductDetails}</label>
        <input name="details" type="text" class="form-control" id="details-input"
               placeholder="${messages_editProductPlaceholderDetails}" value="${product.details}">
    </div>
    <button type="submit" class="btn btn-primary mt-3">${messages_editProductSave}</button>
</form>
<jsp:include page="../parts/bodyjs.jsp"/>
</body>
</html>