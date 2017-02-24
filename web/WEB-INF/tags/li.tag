<%@include file="/WEB-INF/jsp/includes/taglibs.jsp"%>
<%@ attribute name="href" required="true" %>
<c:set var="uri" value="${requestScope['javax.servlet.forward.request_uri']}"/>
<li <c:if test="${fn:contains(uri, href)}">class="active"</c:if>>
    <jsp:doBody/>
</li>