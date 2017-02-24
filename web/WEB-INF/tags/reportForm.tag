<%@include file="/WEB-INF/jsp/includes/taglibs.jsp"%>
<%@tag pageEncoding="UTF-8"%>
<c:set var="currLocaleLabel" value="${currentLocale eq 'ru'?'Русский':'English'}"/>
<c:set var="otherLocaleLabel" value="${currentLocale eq 'ru'?'English':'Русский'}"/>
<c:set var="currLocaleValue" value="${currentLocale}"/>
<c:set var="otherLocaleValue" value="${currentLocale eq 'ru'?'en':'ru'}"/>
<my:nonFieldErrors/>
<my:clear attribute="nonFieldErrors"/>
<form action="/static/admin/report" method="get" >
    <my:form-group name="month">
        <my:input label="month" name="month" type="month" />
    </my:form-group>
    <my:clear attribute="month"/>
    <my:form-group name="year">
        <my:input label="year" name="year" type="year" />
    </my:form-group>
    <my:clear attribute="year"/>
    <my:form-group name="lang">
        <div class="btn-group" data-toggle="buttons">
            <label class="btn btn-warning active">
                <input type="radio" name="lang" id="option1" autocomplete="off" value="${currLocaleValue}" checked> ${currLocaleLabel}
            </label>
            <label class="btn btn-warning">
                <input type="radio" name="lang" id="option2" autocomplete="off" value="${otherLocaleValue}"> ${otherLocaleLabel}
            </label>
        </div>
    </my:form-group>
    <button class="btn btn-success btn-xs"><fmt:message key="report_btn"/></button>
</form>