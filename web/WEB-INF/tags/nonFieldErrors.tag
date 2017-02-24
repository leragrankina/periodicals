<%@include file="/WEB-INF/jsp/includes/taglibs.jsp"%>
<c:if test="${not empty sessionScope.nonFieldErrors}">
    <c:forEach items="${sessionScope.nonFieldErrors}" var="error">
        <p class="text-danger"><fmt:message key="${error}"/></p>
    </c:forEach>
</c:if>