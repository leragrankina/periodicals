<%@include file="../includes/directives.jspf"%>
<%@taglib prefix="my" tagdir="/WEB-INF/tags" %>
<nav class="navbar navbar-default navbar-static-top" role="navigation">
    <div class="container">
    <ul class="nav navbar-nav">
        <my:li href="/pages/admin/periodicals"><a href="/pages/admin/periodicals"><fmt:message key="admin.header.periodicals"/></a></my:li>
        <my:li href="/pages/admin/users"><a href="/pages/admin/users"><fmt:message key="admin.header.users"/></a></my:li>
    </ul>
        <p class="navbar-text navbar-left">
            <a href="/"><fmt:message key="admin.header.view_site"/></a>
        </p>
    <p class="navbar-text navbar-right">
    <fmt:message key="header_jsp.login_msg"/> <strong>${sessionScope.admin.login}</strong> | <a href="/pages/logout" class="navbar-link"><fmt:message key="header_jsp.logout"/></a>
    </p>
    </div>
</nav>