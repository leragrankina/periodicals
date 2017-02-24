<%@include file="/WEB-INF/jsp/includes/taglibs.jsp"%>
<%@attribute name="next" required="false" %>
<my:navbar>
    <p class="navbar-left navbar-text">
        <a href="${not empty next?next:'/'}" class="navbar-link"><i class="fa fa-chevron-circle-left fa-2x"></i></a>
    </p>
</my:navbar>