<%@include file="includes/directives.jspf"%>
<%@taglib prefix="my" tagdir="/WEB-INF/tags" %>
<c:set var="authorized" value="${not empty sessionScope.user}"/>
<c:set var="anonymous" value="${empty sessionScope.user}"/>
<my:navbar>
    <ul class="nav navbar-nav">
        <my:li href="/pages/periodicals"><a href="/"><fmt:message key="header_jsp.home"/></a></my:li>
        <c:if test="${authorized}">
            <my:li href="/pages/protected/profile"><a href="/pages/protected/profile"><fmt:message key="header_jsp.profile"/></a></my:li>
        </c:if>
    </ul>
    <p class="navbar-text navbar-right">
    <c:if test="${authorized}">
        <fmt:message key="header_jsp.login_msg"/> <strong>${sessionScope.user.login}</strong> | <a href="/pages/logout" class="navbar-link"><fmt:message key="header_jsp.logout"/></a>
    </c:if>
    <c:if test="${anonymous}" >
        <a href="/pages/login" class="navbar-link"><fmt:message key="header_jsp.login"/></a>
    </c:if>
    </p>
</my:navbar>
