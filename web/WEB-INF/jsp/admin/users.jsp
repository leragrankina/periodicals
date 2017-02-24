<%@include file="../includes/directives.jspf"%>
<html>
<jsp:include page="../head.jsp">
    <jsp:param name="title" value="admin.header.users"/>
</jsp:include>
<my:body>
<jsp:include page="header.jsp"/>
<div class="container">
    <div class="row">
        <div class="col-xs-12 col-sm-6 col-sm-offset-3">
            <my:feedback first_param="${sessionScope.user_login}"/>
<table class="table">
    <thead>
    <tr>
        <td><fmt:message key="admin.users.headers.login"/></td>
        <td><fmt:message key="admin.users.headers.status"/></td>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${users}" var="user">
        <tr>
            <td class="col-sm-2">${user.login}</td>
            <td class="col-sm-1">
                <form action="/pages/admin/toggle" method="post">
                    <input type="hidden" name="id" value="${user.id}"/>
                    <button class="btn-link"><fmt:message key="admin.users.${user.blocked?'unblock':'block'}"/></button>
                </form>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</div></div></div>
</my:body>
</html>
