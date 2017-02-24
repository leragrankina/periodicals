<%@include file="/WEB-INF/jsp/includes/taglibs.jsp"%>
<%@attribute name="attribute" required="true" %>
<c:set target="${sessionScope}" property="${attribute}" value=""/>