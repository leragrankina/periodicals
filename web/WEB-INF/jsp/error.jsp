<%@include file="includes/directives.jspf"%>
<%@page isErrorPage="true" %>
<html>
<jsp:include page="head.jsp">
    <jsp:param name="title" value="error_jsp.title"/>
</jsp:include>
<my:body>
<div class="jumbotron">
    <div class="container">
        <h1>Ooops!</h1>
        <p><fmt:message key="error_jsp.sorry_phrase"/></p>
    </div>
</div>
</my:body>
</html>
