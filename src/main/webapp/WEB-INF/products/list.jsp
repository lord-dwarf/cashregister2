<%@ page import="static com.polinakulyk.cashregister2.controller.api.HttpRoute.PRODUCTS_VIEW" %>
<%@ page import="static com.polinakulyk.cashregister2.controller.api.HttpRoute.PRODUCTS_ADD" %>
<%@ page import="static com.polinakulyk.cashregister2.controller.api.HttpRoute.PRODUCTS_EDIT" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="view" class="com.polinakulyk.cashregister2.view.ProductsView"/>
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
    <%= "Products" %>
</h1>
<a class="btn btn-primary" href="<%=view.makeUrl(PRODUCTS_ADD)%>" role="button">Add Product</a>
<table class="table">
    <thead>
    <tr>
        <th scope="col">Code</th>
        <th scope="col">Name</th>
        <th scope="col">Category</th>
        <th scope="col">Price</th>
        <th scope="col">In Stock</th>
        <th scope="col">Amount Unit</th>
        <th scope="col">Actions</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="product" items="${products}">
        <tr>
            <td>
                <c:out value="${product.code}"/>
            </td>
            <td>
                <c:out value="${product.name}"/>
            </td>
            <td>
                <c:out value="${product.category}"/>
            </td>
            <td>
                <c:out value="${product.price}"/>
            </td>
            <td>
                <c:out value="${product.amountAvailable}"/>
            </td>
            <td>
                <c:out value="${product.amountUnit}"/>
            </td>
            <td>
                <a class="btn btn-outline-primary"
                   href="<%=view.makeUrl(PRODUCTS_VIEW)%>?id=${product.id}"
                   role="button">View</a>
                <a class="btn btn-outline-warning"
                   href="<%=view.makeUrl(PRODUCTS_EDIT)%>?id=${product.id}"
                   role="button">Edit</a>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<jsp:include page="../parts/bodyjs.jsp"/>
</body>
</html>