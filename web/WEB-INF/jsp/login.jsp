<%@include file="includes/directives.jspf"%>
<html>
<my:head title="login_jsp.title">
    <link href="/bower_components/bootstrap-social/bootstrap-social.css" rel="stylesheet" type="text/css"/>
</my:head>
<my:body>
<my:back_header/>
<div class="container">
    <div class="row">
        <div class="col-xs-12 col-sm-4 col-sm-offset-4">
            <my:login_form>
                <my:checkbox label="remember_me" name="remember_me" value="true"/>
            </my:login_form>
            </div>
    </div>
    <div class="row">
        <div class="col-xs-12 col-sm-4 col-sm-offset-4">
            <div class="alert alert-warning">
                <fmt:message key="login_jsp.invitation"/> <a href="/pages/register"><fmt:message key="login_jsp.sign_up"/></a>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-xs-12 col-sm-4 col-sm-offset-4">
            <p class="text-center"><fmt:message key="login_jsp.or"/></p>
        </div>
    </div>
    <div class="row">
        <div class="col-xs-12 col-sm-4 col-sm-offset-4">
            <my:fb-button/>
        </div>
    </div>
</div>
</my:body>
</html>
