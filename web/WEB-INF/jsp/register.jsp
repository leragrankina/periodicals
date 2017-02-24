<%@include file="includes/directives.jspf"%>
<html>
<head>
    <jsp:include page="links.jspf"/>
    <script src="https://www.google.com/recaptcha/api.js?hl=${sessionScope.currentLocale}" async defer></script>
</head>
<jsp:include page="head.jsp">
    <jsp:param name="title" value="register_jsp.title"/>
</jsp:include>
<my:body>
<jsp:include page="header.jsp"/>
<div class="container">
    <div class="row">
       <div class="col-xs-12 col-sm-6 col-sm-offset-3">
           <my:nonFieldErrors/>
            <form action="/pages/register" method="post">
                <my:form-group name="login">
                    <my:input label="login" name="login" />
                </my:form-group>
                <my:form-group name="password1">
                    <my:input label="password" name="password1" type="password" />
                </my:form-group>
                <my:form-group name="password2">
                    <my:input label="repeat_password" name="password2" type="password" />
                </my:form-group>
                <my:form-group name="email">
                    <my:input label="email" name="email" />
                </my:form-group>
                <div class="g-recaptcha" data-sitekey="${requestScope.public_key}"></div>
                <button class="btn btn-success"><fmt:message key="register_jsp.submit"/></button>
            </form>
   </div></div></div>
</my:body>
</html>

