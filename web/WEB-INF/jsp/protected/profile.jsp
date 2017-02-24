<%@include file="../includes/directives.jspf"%>
<html>
<jsp:include page="../head.jsp">
    <jsp:param name="title" value="profile_jsp.title"/>
</jsp:include>
<my:body>
<jsp:include page="../header.jsp"/>
<div class="container">
    <div class="row">
        <div class="col-xs-12 col-sm-6 col-sm-offset-3">
            <p><fmt:message key="profile_jsp.balance"/>: ${user.balance} <a href="/pages/protected/recharge"><fmt:message key="profile_jsp.recharge"/></a></p>
            <table class="table">
                <c:forEach items="${requestScope.subscribtions}" var="periodical">
                    <tr>
                        <td>${periodical.title}</td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </div>
</div>
</my:body>
</html>
