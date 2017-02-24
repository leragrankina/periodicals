<%@include file="includes/directives.jspf"%>
<html>
<jsp:include page="head.jsp">
    <jsp:param name="title" value="success_jsp.title"/>
</jsp:include>
<my:body>
<jsp:include page="header.jsp"/>
<div class="container">
    <div class="row">
        <div class="col-xs-12 col-sm-6 col-sm-offset-3">
            <p class="text-success"><fmt:message key="success_jsp.message"/> <a href="/pages/login"><fmt:message key="success_jsp.login"/></a></p>
</div></div></div>
</my:body>
</html>
