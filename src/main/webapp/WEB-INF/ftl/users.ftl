<#-- @ftlvariable name="users" type="java.util.List<gr.uoa.di.digibid.model.WebUser>" -->
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">

    <title>DigiBid | Manage Users</title>
</head>
<body>
<#include "header.ftl">
<script>
    $(document).ready(function () {

        $("#users").DataTable(
                {
                    responsive: true,
                    searching: false
                }
        );

        $("#download-items-button").on("click", function () {
            window.open("/api/items/download", "_blank");
        });
    });
</script>
<div class="container" id="container">
    <div class="row">
        <h1 style="text-align: center;">Manage Users</h1>
    </div>
    <div class="row">
        <div style="margin-bottom: 25px;">
            <button class="btn btn-primary" id="download-items-button" type="button">
                <span class="glyphicon glyphicon-download"></span> Download Items
            </button>
        </div>
        <table id="users" class="table" cellspacing="0" width="100%">
            <thead>
            <tr>
                <th>Username</th>
                <th>E-mail</th>
                <th>Role(s)</th>
                <th>Create Date</th>
                <th>Active</th>
            </tr>
            </thead>
            <tbody>
            <#list users as user>
            <tr>
                <td><a href="/user/${user.username}">${user.username}</a></td>
                <td>${user.email}</td>
                <td>${user.roles?join(", ")}</td>
                <td>${user.creationDate}</td>
                <#if user.active>
                    <#if user.username != currentUser.username>
                        <td>
                            <a href="/user/${user.username}/activate/false" target="_self">Deactivate</a>
                        </td>
                    <#else>
                        <td>${user.active?c}</td>
                    </#if>
                <#else>
                    <td>
                        <a href="/user/${user.username}/activate/true" target="_self">Activate</a>
                    </td>
                </#if>
            </tr>
            </#list>
            </tbody>
        </table>
    </div>
</div> <!-- /container -->
<#include "footer.ftl">
</body>
</html>

