<%@include file="includes/directives.jspf"%>
<html>
<jsp:include page="head.jsp">
    <jsp:param name="title" value="block_jsp.title"/>
</jsp:include>
<my:body>
<div class="jumbotron">
    <div class="container">
        <h1>Ooops!</h1>
        <p><fmt:message key="block_jsp.message"/></p>
    </div>
</div>
<div class="container">
    <a href="/pages/logout"><fmt:message key="header_jsp.logout"/></a>
</div>
</my:body>
</html>
