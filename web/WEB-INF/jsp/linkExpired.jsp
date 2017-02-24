<%@include file="includes/directives.jspf"%>
<html>
<jsp:include page="head.jsp">
    <jsp:param name="title" value="expired_jsp.title"/>
</jsp:include>
<my:body>
<div class="container">
    <div class="row">
        <div class="col-xs-12 col-sm-6 col-sm-offset-3">
            <fmt:message key="link_expired"/>
        </div>
    </div>
</div>
</my:body>
</html>
