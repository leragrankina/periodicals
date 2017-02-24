<%@include file="/WEB-INF/jsp/includes/taglibs.jsp"%>
<%@attribute name="label" required="true" %>
<%@attribute name="name" required="true" %>
<label for="${name}Id"><fmt:message key="form.label.${label}"/></label>
<select name="${name}" id="${name}Id" class="form-control">
    <jsp:doBody/>
</select>