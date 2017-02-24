<%@include file="/WEB-INF/jsp/includes/taglibs.jsp"%>
<%@ attribute name="field" required="true" %>
<c:set var="reverseOrder" value="${param.order eq 'a'?'d':'a'}"/>
<input type="hidden" name="sort" value="${field}"/>
<input type="hidden" name="order" value="${field eq param.sort?reverseOrder:'a'}"/>
