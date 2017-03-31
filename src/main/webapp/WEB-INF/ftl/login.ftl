<#-- @ftlvariable name="error" type="java.util.Optional<String>" -->
<#import "/spring.ftl" as spring>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">

    <title>DigiBid | Log in</title>
</head>
<body>
<#include "header.ftl">
<div class="container" id="container">
    <form role="form" action="/login" method="post" class="form-digibid">
        <h2>Log in</h2>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

        <label for="username" class="sr-only">Username</label>
        <input type="text" name="username" class="form-control" placeholder="Username" id="username" required
               autofocus>
        <label for="password" class="sr-only">Password</label>
        <input type="password" name="password" id="password" class="form-control" placeholder="Password"
               required/>
        <div class="checkbox">
            <label>
                <input type="checkbox" name="remember-me" id="remember-me"> Remember me
            </label>
        </div>
        <button class="btn btn-primary btn-block" type="submit" id="login">Log in</button>
    <#if error.isPresent()>
        <div>
            <#if Session?? && Session.SPRING_SECURITY_LAST_EXCEPTION?? && Session.SPRING_SECURITY_LAST_EXCEPTION.message?has_content>
                <p style="margin: 10px; color:red;">${Session.SPRING_SECURITY_LAST_EXCEPTION.message}</p>
            <#else>
                <#if !spring.message??>
                    <p style="margin: 10px; color:red;"><@spring.message "login.bad.credentials"/></p>
                </#if>
            </#if>
        </div>
    </#if>
    </form>
</div> <!-- /container -->

<#include "footer.ftl">
</body>
</html>