<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<jsp:include page="../parts/head.jsp" />
<body>
<jsp:include page="../parts/navbar.jsp" />
<h1>
    Bad Request
</h1>
<h3>
    ${errorMessage}
</h3>
<jsp:include page="../parts/bodyjs.jsp" />
</body>
</html>