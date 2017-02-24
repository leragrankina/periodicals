<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@attribute name="title" required="true" %>
<head>
    <%@ include file="/WEB-INF/jsp/links.jspf" %>
    <title><fmt:message key="${title}"/></title>
    <jsp:doBody/>
</head>
