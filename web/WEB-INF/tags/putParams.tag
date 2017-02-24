<%@include file="/WEB-INF/jsp/includes/taglibs.jsp"%>
<%@attribute name="excludes" required="false" %>
<c:forEach items="${param}" var="par">
    <c:if test="${not fn:contains(excludes, par.key)}">
        <input type="hidden" name="${par.key}" value="${par.value}"/>
    </c:if>
</c:forEach>
