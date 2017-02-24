<%@include file="../includes/directives.jspf"%>
<html>
<c:set var="title" value="periodical_jsp.${empty param.id?'add':'edit'}.title"/>
<jsp:include page="../head.jsp">
    <jsp:param name="title" value="${pageScope.title}"/>
</jsp:include>
<my:body>
<jsp:include page="header.jsp"/>
<div class="container">
    <ol class="breadcrumb">
        <li><a href="/pages/admin/periodicals"><fmt:message key="admin.header.periodicals"/></a></li>
        <li class="active"><fmt:message key="${title}"/></li>
    </ol>
    <div class="row">
        <div class="col-xs-12 col-sm-6 col-sm-offset-3">
            <my:nonFieldErrors/>
            <form action="${requestScope['javax.servlet.forward.request_uri']}" method="post">
                <input type="hidden" name="id" value="${param.id}"/>
                <my:form-group name="title">
                    <my:input label="title" name="title"/>
                </my:form-group>
                <my:form-group name="price">
                    <my:input label="price" name="price" type="number"/>
                </my:form-group>
                <my:form-group name="theme">
                    <my:select label="theme" name="theme">
                        <c:forEach items="${themes}" var="theme">
                            <option value="${theme.id}" ${sessionScope.theme eq theme?'selected':''}>
                                <fmt:message key="${theme.name}"/>
                            </option>
                        </c:forEach>
                    </my:select>
                </my:form-group>
                <my:form-group name="period">
                    <my:select label="period" name="period">
                        <c:forEach items="${periods}" var="period">
                            <option value="${period.id}" ${sessionScope.period eq period?'selected':''}>
                                <fmt:message key="${period.name}"/>
                            </option>
                        </c:forEach>
                    </my:select>
                </my:form-group>
                <button class="btn btn-success btn-sm"><fmt:message key="form.periodical.submit"/></button>
</form></div></div></div>
</my:body>
</html>
