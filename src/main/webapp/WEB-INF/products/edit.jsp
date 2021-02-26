<%@ page import="static com.polinakulyk.cashregister2.controller.api.HttpRoute.PRODUCTS_EDIT" %>
<jsp:useBean id="view" class="com.polinakulyk.cashregister2.view.ProductsView"/>
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
    <%= "Edit Product" %>
</h1>
<form class="w-40" method="post" action="<%=view.makeUrl(PRODUCTS_EDIT)%>?id=${product.id}">
    <div class="form-group mt-2">
        <label for="code-input">Code</label>
        <input name="code" type="text" class="form-control" id="code-input"
               placeholder="Enter code" value="${product.code}">
    </div>
    <div class="form-group mt-2">
        <label for="name-input">Name</label>
        <input name="name" type="text" class="form-control" id="name-input"
               placeholder="Enter name" value="${product.name}">
    </div>
    <div class="form-group mt-2">
        <label for="category-input">Category</label>
        <input name="category" type="text" class="form-control" id="category-input"
               placeholder="Enter category" value="${product.category}">
    </div>
    <div class="form-group mt-2">
        <label for="price-input">Enter price</label>
        <input name="price" type="text" class="form-control" id="price-input"
               placeholder="Enter price" value="${product.price}">
    </div>
    <div class="form-group mt-2">
        <label for="amount-available-input">Enter amount available</label>
        <input name="amountAvailable" type="text" class="form-control" id="amount-available-input"
               placeholder="Enter amount available" value="${product.amountAvailable}">
    </div>
    <div class="form-group mt-2">
        <label for="amount-unit-input">Select amount unit</label>
        <input name="amountUnit" type="text" class="form-control" id="amount-unit-input"
               placeholder="Select kilo or unit" value="${product.amountUnit}">
    </div>
    <div class="form-group mt-2">
        <label for="details-input">Enter details</label>
        <input name="details" type="text" class="form-control" id="details-input"
               placeholder="Enter product details" value="${product.details}">
    </div>
    <button type="submit" class="btn btn-primary mt-3">Save</button>
</form>
<jsp:include page="../parts/bodyjs.jsp" />
</body>
</html>