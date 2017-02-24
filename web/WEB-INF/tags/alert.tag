<%@include file="/WEB-INF/jsp/includes/taglibs.jsp"%>
<%@attribute name="color" required="true" %>
<div class="alert alert-${color} alert-dismissible" role="alert">
    <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
    <jsp:doBody/>
</div>
