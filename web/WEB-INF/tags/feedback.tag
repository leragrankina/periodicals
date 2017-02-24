<%@include file="/WEB-INF/jsp/includes/taglibs.jsp"%>
<%@attribute name="first_param" required="false" %>
<c:set var="hasMessage" value="${not empty sessionScope.successMessage or not empty sessionScope.errorMessage}"/>
<c:set var="message" value="${not empty sessionScope.successMessage?sessionScope.successMessage:sessionScope.errorMessage}"/>
<c:set var="color" value="${not empty sessionScope.successMessage?'success':'danger'}"/>
<c:if test="${hasMessage}">
    <my:alert color="${color}">
        <fmt:message key="${message}">
            <fmt:param value="${first_param}"/>
        </fmt:message>
    </my:alert>
</c:if>
