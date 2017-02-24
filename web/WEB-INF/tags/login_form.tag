<%@include file="/WEB-INF/jsp/includes/taglibs.jsp"%>
<my:nonFieldErrors/>
<form action="${requestScope['javax.servlet.forward.request_uri']}" method="post">
    <my:form-group name="login">
        <my:input label="login" name="login"/>
    </my:form-group>
    <my:form-group name="password">
        <my:input label="password" name="password" type="password"/>
    </my:form-group>
    <jsp:doBody/>
    <button class="btn btn-success btn-sm"><fmt:message key="login_form.submit"/></button>
</form>
