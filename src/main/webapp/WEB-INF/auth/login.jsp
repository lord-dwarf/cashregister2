<%@ page import="static com.polinakulyk.cashregister2.controller.api.HttpRoute.*" %>
<jsp:useBean id="router" class="com.polinakulyk.cashregister2.view.RouterView"/>
<%
    router.init(request);
%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<jsp:include page="../parts/head.jsp"/>
<body>
<jsp:include page="../parts/navbar.jsp"/>
<div class="d-flex justify-content-center align-items-center container">
    <form class="w-25" method="post" action="<%=router.makeUrl(AUTH_LOGIN)%>">
        <div class="form-group mt-2">
            <label for="login-input">Login</label>
            <input name="login" type="text" class="form-control" id="login-input"
                   placeholder="Enter login">
        </div>
        <div class="form-group mt-2">
            <label for="password-input">Password</label>
            <input name="password" type="password" class="form-control" id="password-input"
                   placeholder="Password">
        </div>
        <button type="submit" class="btn btn-primary mt-3">Login</button>
    </form>
</div>
<jsp:include page="../parts/bodyjs.jsp"/>
</body>
</html>