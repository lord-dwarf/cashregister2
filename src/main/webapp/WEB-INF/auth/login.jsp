<%@ page import="static com.polinakulyk.cashregister2.controller.api.HttpRoute.*" %>
<%@ page
        import="static com.polinakulyk.cashregister2.controller.router.RouterHelper.getRoutePath" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<jsp:include page="../parts/head.jsp"/>
<body>
<jsp:include page="../parts/navbar.jsp"/>
<div class="d-flex justify-content-center align-items-center container">
    <form class="w-25" method="post" action="<%=getRoutePath(request, AUTH_LOGIN)%>">
        <div class="form-group mt-2">
            <label for="login-input">${messages_loginUsernameLabel}</label>
            <input name="login" type="text" class="form-control" id="login-input"
                   placeholder="${messages_loginUsernamePlaceholder}">
        </div>
        <div class="form-group mt-2">
            <label for="password-input">${messages_loginPasswordLabel}</label>
            <input name="password" type="password" class="form-control" id="password-input"
                   placeholder="${messages_loginPasswordPlaceholder}">
        </div>
        <button type="submit" class="btn btn-primary mt-3">${messages_loginSignIn}</button>
    </form>
</div>
<jsp:include page="../parts/bodyjs.jsp"/>
</body>
</html>