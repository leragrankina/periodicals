<%@include file="../includes/directives.jspf"%>
<html>
<jsp:include page="../head.jsp">
    <jsp:param name="title" value="admin.header.periodicals"/>
</jsp:include>
<my:body>
<jsp:include page="header.jsp"/>
<div class="container">
    <div class="row">
        <my:changeLocale/>
        <div class="col-xs-12 col-sm-6 col-sm-offset-2">
          <my:feedback first_param="${sessionScope.periodical_title}"/>
            <p>
                <a href="/pages/admin/periodicals/add" class="btn btn-success btn-sm"><i class="fa fa-plus"></i></a>
            </p>
<table class="table table-striped">
<c:forEach items="${periodicals}" var="periodical">
    <tr>
        <td class="col-xs-4">${periodical.title}</td>
        <td class="col-xs-1">
            <form action="/pages/admin/periodicals/edit">
                <input type="hidden" name="id" value="${periodical.id}"/>
                <button class="btn-link"><i class="fa fa-pencil text-warning"></i></button>
            </form>
        </td>
        <td class="col-xs-1">
            <form action="/pages/admin/periodicals/delete" method="post">
                <input type="hidden" name="id" value="${periodical.id}"/>
                <button class="btn-link"><i class="fa fa-close text-danger"></i></button>
            </form>
        </td>
    </tr>
</c:forEach>
</table>
</div>
        <div class="col-xs-12 col-sm-3 col-sm-offset-1">
            <div class="panel panel-primary">
                <div class="panel-heading"><fmt:message key="report.panel.title"/></div>
                <div class="panel-body">
                    <my:reportForm/>
                </div>
            </div>
        </div>
    </div>
</div>
</my:body>
</html>
