<%@include file="../jsp/includes/taglibs.jsp"%>
<%@attribute name="label" required="true" %>
<%@attribute name="name" required="true" %>
<%@attribute name="value" required="true" %>
<%@attribute name="checked" required="false" %>
<div class="checkbox">
    <label>
        <input type="checkbox" name="${name}" ${not empty checked?'checked':''}> <fmt:message key="${label}"/>
    </label>
</div>