<%@include file="/WEB-INF/jsp/includes/taglibs.jsp"%>
<%@attribute name="name" required="true" %>
<c:set var="errorName" value="${name.concat('Error')}"/>
<c:set var="errors" value="${sessionScope[errorName]}"/>
<c:set var="hasErrors" value="${not empty errors}"/>
<div class="form-group ${hasErrors?'has-error':''}">
    <jsp:doBody/>
    <c:if test="${hasErrors}">
        <c:forEach items="${errors}" var="error">
            <span class="help-block"><fmt:message key="${error}"/></span>
        </c:forEach>
    </c:if>
</div>
