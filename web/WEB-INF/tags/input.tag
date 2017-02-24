<%@include file="/WEB-INF/jsp/includes/taglibs.jsp"%>
<%@attribute name="label" required="true" %>
<%@attribute name="name" required="true" %>
<%@attribute name="type" required="false" %>
<%@attribute name="min" required="false" %>
<%@attribute name="max" required="false" %>
<c:set var="type" value="${empty type?'text':type}"/>
<label for="${name}Id"><fmt:message key="form.label.${label}"/></label>
<input class="form-control"
id="${name}Id"
value="${sessionScope[name]}"
type="${type}"
<c:if test="${not empty min}">
      min="${min}"
</c:if>
<c:if test="${not empty max}">
    max="${max}"
</c:if>
name="${name}"/>