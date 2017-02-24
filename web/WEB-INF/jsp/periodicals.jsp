<%@include file="includes/directives.jspf"%>
<html>
<jsp:include page="head.jsp">
    <jsp:param name="title" value="periodicals_jsp.title"/>
</jsp:include>
<c:set var="authorized" value="${not empty sessionScope.user}"/>
<c:set var="anonymous" value="${empty sessionScope.user}"/>
<my:body>
<jsp:include page="header.jsp"/>
<div class="container">
<div class="row">
    <my:changeLocale/>
<div class="col-xs-12 col-sm-6 col-sm-offset-3">
<my:feedback first_param="${sessionScope.periodical_title}"/>
<c:if test="${not empty param.theme or not empty param.title}">
<a href="/pages/periodicals" class="btn-link">
    <i class="fa fa-arrow-left"></i> <fmt:message key="periodicals_jsp.all"/></a>
</c:if>
<form action="/pages/periodicals" method="get" class="form-inline">
    <my:putParams excludes="title"/>
    <div class="form-group">
        <input name="title" class="form-control" value="${param.title}">
    </div>
    <button class="form-control">
        <i class="fa fa-search"></i>
    </button>
</form>
<table class="table">
    <thead>
    <tr>
        <th class="col-md-2">
            <form action="/pages/periodicals">
                <my:putParams excludes="sort,order"/>
                <my:sortQuery field="id"/>
                <button class="btn-link"><fmt:message key="periodicals_jsp.table.id"/><my:angle sort="id"/></button>
            </form>
        </th>
        <th>
            <form action="/pages/periodicals">
                <my:putParams excludes="sort,order"/>
                <my:sortQuery field="title"/>
                <button class="btn-link"><fmt:message key="periodicals_jsp.table.title"/><my:angle sort="title"/></button>
            </form>
        </th>
        <th>
            <form action="/pages/periodicals">
                <my:putParams excludes="sort,order"/>
                <my:sortQuery field="price"/>
                <button class="btn-link"><fmt:message key="periodicals_jsp.table.price"/><my:angle sort="price"/></button>
            </form>
        </th>
        <th><fmt:message key="periodicals_jsp.table.theme"/></th>
        <th><fmt:message key="periodicals_jsp.table.period"/></th>
    </tr>
    </thead>
    <tbody>
        <c:forEach items="${requestScope.periodicals}" var="periodical">
            <tr>
                <td class="col-md-2">${periodical.id}</td>
                <td class="col-md-3">
                <c:if test="${not empty param.title}">
                        ${custom:replaceAll(periodical.title, param.title, "<span class=\"text-highlight\">$1</span>")}
                </c:if>
                <c:if test="${empty param.title}">
                    ${periodical.title}
                </c:if>
                </td>
                <td class="col-md-3">${periodical.price}</td>
                <td>
                    <form action="/pages/periodicals">
                        <my:putParams excludes="theme"/>
                        <input type="hidden" name="theme" value="${periodical.theme.name}"/>
                        <button class="btn-link"><fmt:message key="${periodical.theme.name}"/></button>
                    </form>
                </td>
                <td><fmt:message key="${periodical.period.name}"/></td>
                <c:if test="${authorized}">
                    <c:choose>
                        <c:when test="${sessionScope.user.isSubscribed(periodical)}">
                            <td><span class="label label-success label-xs"><fmt:message key="periodicals_jsp.subscribed"/></span></td>
                        </c:when>
                        <c:otherwise>
                            <td>
                                <form action="/pages/protected/subscribe" method="post">
                                    <input type="hidden" name="periodicalId" value="${periodical.id}"/>
                                    <button class="btn-link"><fmt:message key="periodicals_jsp.subscribe"/></button>
                                </form>
                            </td>
                        </c:otherwise>
                    </c:choose>
                </c:if>
            </tr>
        </c:forEach>
    </tbody>
</table>
</div></div></div>
</my:body>
</html>
