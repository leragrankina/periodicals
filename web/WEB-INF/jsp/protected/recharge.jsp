<%@include file="../includes/directives.jspf"%>
<html>
<jsp:include page="../head.jsp">
    <jsp:param name="title" value="profile_jsp.recharge"/>
</jsp:include>
<my:body>
<jsp:include page="../header.jsp"/>
<div class="container">
    <ol class="breadcrumb">
        <li><a href="/pages/protected/profile"><fmt:message key="header_jsp.profile"/></a></li>
        <li class="active"><fmt:message key="profile_jsp.recharge"/></li>
    </ol>
    <div class="row">
        <div class="col-xs-12 col-sm-6 col-sm-offset-3">
<form action="/pages/protected/recharge" method="post">
    <my:form-group name="amount">
        <!--<my:input label="amount" name="amount" type="number" min="0" max="10000"/>-->
        <my:input label="amount" name="amount"/>
    </my:form-group>
    <button class="btn btn-success"><fmt:message key="profile_jsp.recharge"/></button>
</form>
</div></div></div>
</my:body>
</html>
