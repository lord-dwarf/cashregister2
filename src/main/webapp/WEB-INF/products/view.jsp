<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<jsp:include page="../parts/head.jsp"/>
<body>
<jsp:include page="../parts/navbar.jsp"/>
<h1>
    ${messages_viewProduct}
</h1>
<form class="w-40">
    <div class="form-group mt-2">
        <label for="code-input">${messages_viewProductCode}</label>
        <input name="code" type="text" class="form-control" id="code-input"
               value="${product.code}" readonly>
    </div>
    <div class="form-group mt-2">
        <label for="name-input">${messages_viewProductName}</label>
        <input name="name" type="text" class="form-control" id="name-input"
               value="${product.name}" readonly>
    </div>
    <div class="form-group mt-2">
        <label for="category-input">${messages_viewProductCategory}</label>
        <input name="category" type="text" class="form-control" id="category-input"
               value="${product.category}" readonly>
    </div>
    <div class="form-group mt-2">
        <label for="price-input">${messages_viewProductPrice}</label>
        <input name="price" type="text" class="form-control" id="price-input"
               value="${product.price}" readonly>
    </div>
    <div class="form-group mt-2">
        <label for="amount-available-input">${messages_viewProductAvailable}</label>
        <input name="amountAvailable" type="text" class="form-control" id="amount-available-input"
               placeholder="Enter amount available" value="${product.amountAvailable}" readonly>
    </div>
    <div class="form-group mt-2">
        <label for="amount-unit-input">${messages_viewProductAmountUnit}</label>
        <input name="amountUnit" type="text" class="form-control" id="amount-unit-input"
               placeholder="Select kilo or unit" value="${product.amountUnit}" readonly>
    </div>
    <div class="form-group mt-2">
        <label for="details-input">${messages_viewProductDetails}</label>
        <input name="details" type="text" class="form-control" id="details-input"
               value="${product.details}" readonly>
    </div>
    <jsp:include page="../parts/bodyjs.jsp"/>
</body>
</html>