<%@ page import="static com.polinakulyk.cashregister2.controller.dto.HttpRoute.PRODUCTS_VIEW" %>
<%@ page import="static com.polinakulyk.cashregister2.controller.dto.HttpRoute.PRODUCTS_ADD" %>
<%@ page import="static com.polinakulyk.cashregister2.controller.dto.HttpRoute.PRODUCTS_EDIT" %>
<%@ page import="static com.polinakulyk.cashregister2.controller.dto.HttpRoute.PRODUCTS_LIST" %>
<%@ page
        import="static com.polinakulyk.cashregister2.controller.router.RouterHelper.getRoutePath" %>
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
    ${messages_productsLabel}
</h1>
<a class="btn btn-primary" href="<%=getRoutePath(request, PRODUCTS_ADD)%>"
   role="button">${messages_addProductLabel}</a>
<table class="table">
    <thead>
    <tr>
        <th scope="col">${messages_codeLabel}</th>
        <th scope="col">${messages_nameLabel}</th>
        <th scope="col">${messages_categoryLabel}</th>
        <th scope="col">${messages_priceLabel}</th>
        <th scope="col">${messages_inStockLabel}</th>
        <th scope="col">${messages_unitLabel}</th>
        <th scope="col">${messages_actionsLabel}</th>
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
                   href="<%=getRoutePath(request, PRODUCTS_VIEW)%>?id=${product.id}"
                   role="button">${messages_actionsViewLabel}</a>
                <a class="btn btn-outline-warning"
                   href="<%=getRoutePath(request, PRODUCTS_EDIT)%>?id=${product.id}"
                   role="button">${messages_actionsEditLabel}</a>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<nav aria-label="Navigation">
    <ul class="pagination">
        <c:forEach begin="1" end="${pagesTotal}" var="i">
            <c:choose>
                <c:when test="${currentPage eq i}">
                    <li class="page-item active">
                        <a class="page-link">
                                ${i} <span class="sr-only">(current)</span>
                        </a>
                    </li>
                </c:when>
                <c:otherwise>
                    <li class="page-item">
                        <a class="page-link"
                           href="<%=getRoutePath(request, PRODUCTS_LIST)%>?currentPage=${i}"
                        >
                                ${i}
                        </a>
                    </li>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </ul>
</nav>
<jsp:include page="../parts/bodyjs.jsp"/>
</body>
</html>