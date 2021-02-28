<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<jsp:include page="../parts/head.jsp"/>
<body>
<jsp:include page="../parts/navbar.jsp"/>
<h1>
    <%= "View Product" %>
</h1>
<form class="w-40">
    <div class="form-group mt-2">
        <label for="code-input">Code</label>
        <input name="code" type="text" class="form-control" id="code-input"
               placeholder="Enter code" value="${product.code}" readonly>
    </div>
    <div class="form-group mt-2">
        <label for="name-input">Name</label>
        <input name="name" type="text" class="form-control" id="name-input"
               placeholder="Enter name" value="${product.name}" readonly>
    </div>
    <div class="form-group mt-2">
        <label for="category-input">Category</label>
        <input name="category" type="text" class="form-control" id="category-input"
               placeholder="Enter category" value="${product.category}" readonly>
    </div>
    <div class="form-group mt-2">
        <label for="price-input">Enter price</label>
        <input name="price" type="text" class="form-control" id="price-input"
               placeholder="Enter price" value="${product.price}" readonly>
    </div>
    <div class="form-group mt-2">
        <label for="amount-available-input">Enter amount available</label>
        <input name="amountAvailable" type="text" class="form-control" id="amount-available-input"
               placeholder="Enter amount available" value="${product.amountAvailable}" readonly>
    </div>
    <div class="form-group mt-2">
        <label for="amount-unit-input">Select amount unit</label>
        <input name="amountUnit" type="text" class="form-control" id="amount-unit-input"
               placeholder="Select kilo or unit" value="${product.amountUnit}" readonly>
    </div>
    <div class="form-group mt-2">
        <label for="details-input">Enter details</label>
        <input name="details" type="text" class="form-control" id="details-input"
               placeholder="Enter product details" value="${product.details}" readonly>
    </div>
    <jsp:include page="../parts/bodyjs.jsp"/>
</body>
</html>