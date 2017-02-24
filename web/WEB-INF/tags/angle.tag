<%@include file="/WEB-INF/jsp/includes/taglibs.jsp"%>
<%@attribute name="sort" required="true" %>
<c:if test="${param.sort eq sort}">
    <c:choose>
        <c:when test="${param.order eq 'a'}"> <i class="fa fa-caret-up"></i> </c:when>
        <c:otherwise> <i class="fa fa-caret-down"></i> </c:otherwise>
    </c:choose>
</c:if>