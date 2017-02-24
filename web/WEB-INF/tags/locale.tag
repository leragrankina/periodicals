<%@include file="/WEB-INF/jsp/includes/taglibs.jsp"%>
<%@attribute name="localeName" required="true" %>
<%@attribute name="localeValue" required="true" %>
<c:choose>
    <c:when test="${currentLocale eq localeValue}"><div class="label label-default label-xs">${localeName}</div></c:when>
    <c:otherwise>
        <form action="/pages/change_locale" method="post">
            <input type="hidden" value="${requestScope['javax.servlet.forward.request_uri']}" name="next"/>
            <button class="btn-link">${localeName}</button>
        </form>
    </c:otherwise>
</c:choose>
