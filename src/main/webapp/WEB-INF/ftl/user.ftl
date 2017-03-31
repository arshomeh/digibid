<#-- @ftlvariable name="itemCategories" type="java.util.List<gr.uoa.di.digibid.model.WebItemCategory>" -->
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

    <title>DigiBid | Profile</title>

    <style>
        .bold {
            font-weight: bold;
        }
    </style>
</head>
<body>
<#include "header.ftl">

<script>
    $(document).ready(function () {

        $("#info-button").on("click", function () {
            $("#pageTitle").html("Profile");
            $(".list-group-item").removeClass("active");
            $(this).addClass("active");

            $("#userInfo").show();
            $("#userData").html("");
            $("#collapseItemsMenu").collapse("hide");
            $("#collapseMessagesMenu").collapse("hide");
        });

        $("#items-button").on("click", function () {
            $("#collapseItemsMenu").collapse("toggle");
            $("#collapseMessagesMenu").collapse("hide");
        });

        $("#messages-button").on("click", function () {
            $("#collapseMessagesMenu").collapse("toggle");
            $("#collapseItemsMenu").collapse("hide");
        });

        $("#messages-button, #inbox-button").on("click", function () {

            var currentUserId = $(".currentUserId").attr("id");
            if (currentUserId > 0) {
                $.ajax(
                        {
                            type: "GET",
                            url: "/api/user/" + currentUserId + "/messages/unread",
                            success: function (result) {
                                if (result > 0) {
                                    $(".badge").html(result);
                                }
                            },
                            error: function (XMLHttpRequest, textStatus, errorThrown) {
                                window.location.href = "/error";
                            }
                        }
                );
            }

            $("#pageTitle").html("Inbox Messages");
            $(".list-group-item").removeClass("active");
            $("#inbox-button").addClass("active");

            $.get("/user/${user.username}/inbox")
                    .done(function (data) {
                        $("#userInfo").hide();
                        $("#userData").html(data);
                    });
        });

        $("#outbox-button").on("click", function () {
            $("#pageTitle").html("Outbox Messages");
            $(".list-group-item").removeClass("active");
            $(this).addClass("active");

            $.get("/user/${user.username}/outbox")
                    .done(function (data) {
                        $("#userInfo").hide();
                        $("#userData").html(data);
                    });
        });

        $("#items-button, #items-manage-button").on("click", function () {
            $("#pageTitle").html("Items");
            $(".list-group-item").removeClass("active");
            $("#items-manage-button").addClass("active");

            $.get("/items/user/${user.username}")
                    .done(function (data) {
                        $("#userInfo").hide();
                        $("#userData").html(data);
                    });
        });

        $("#items-history-button").on("click", function () {
            $("#pageTitle").html("History");
            $(".list-group-item").removeClass("active");
            $("#items-history-button").addClass("active");

            $.get("/items/user/${user.username}/history")
                    .done(function (data) {
                        $("#userInfo").hide();
                        $("#userData").html(data);
                    });
        });

        $("#items-new-button").on("click", function () {
            $("#pageTitle").html("Add New Item");
            $(".list-group-item").removeClass("active");
            $(this).addClass("active");

            $.get("/items/user/${user.username}/new")
                    .done(function (data) {
                        $("#userInfo").hide();
                        $("#userData").html(data);
                    });
        });

        $("#download-items-button").on("click", function () {
            window.open("/api/items/user/${user.username}/download", "_blank");
        });
    });
</script>

<div class="container" id="container">
    <div class="row">
        <div class="col-xs-6 col-md-2"></div>
        <div class="col-xs-12 col-sm-6 col-md-10"><h1 id="pageTitle" style="text-align: center;">Profile</h1></div>
    </div>
    <div class="row">
        <div class="col-xs-6 col-sm-2">
            <div class="list-group">
                <button class="btn btn-link list-group-item active" id="info-button" type="button">
                    <span class="glyphicon glyphicon-user"></span> Info</button>
            <#if !currentUser.roles?seq_contains("ADMIN")>
                <button class="btn btn-link list-group-item" id="messages-button" type="button" data-toggle="collapse"
                        data-target="#collapseMessagesMenu" aria-expanded="false" aria-controls="collapseMessagesMenu">
                    <span class="glyphicon glyphicon-envelope"></span> Messages
                </button>
                <div class="list-group collapse" id="collapseMessagesMenu">
                    <button class="btn btn-link list-group-item" id="inbox-button" type="button">
                        <span class="glyphicon glyphicon-inbox"></span> Incoming<span
                            class="badge pull-right" style="margin-left: 2px;"></span></button>
                    <button class="btn btn-link list-group-item" id="outbox-button" type="button">
                        <span class="glyphicon glyphicon-send"></span> Outgoing</button>
                </div>
                <button class="btn btn-link list-group-item" id="items-button" type="button" data-toggle="collapse"
                        data-target="#collapseItemsMenu" aria-expanded="false" aria-controls="collapseItemsMenu">
                    <span class="glyphicon glyphicon-list"></span> Items
                </button>
                <div class="list-group collapse" id="collapseItemsMenu">
                    <button class="btn btn-link list-group-item" id="items-manage-button" type="button">
                        <span class="glyphicon glyphicon-cog"></span> Manage</button>
                    <button class="btn btn-link list-group-item" id="items-history-button" type="button">
                        <span class="glyphicon glyphicon-time"></span> History</button>
                    <button class="btn btn-link list-group-item" id="items-new-button" type="button">
                        <span class="glyphicon glyphicon-plus"></span> Add New</button>
                </div>
            </#if>
            </div>
        </div>
        <div class="col-xs-6 col-sm-10">
            <div id="userInfo">
                <table class="table">
                    <tbody>
                    <tr>
                        <td>E-mail Address</td>
                        <td>${user.email}</td>
                    </tr>
                    <tr>
                        <td>Username</td>
                        <td>${user.username}</td>
                    </tr>
                    <tr>
                        <td>Roles</td>
                        <td>${user.roles?join(", ")}</td>
                    </tr>
                    <tr>
                        <td>First Name</td>
                        <td>${user.firstName}</td>
                    </tr>
                    <tr>
                        <td>Last Name</td>
                        <td>${user.lastName}</td>
                    </tr>
                    <tr>
                        <td>Address</td>
                        <td>${user.address}</td>
                    </tr>
                    <tr>
                        <td>Phone</td>
                        <td>${user.phone}</td>
                    </tr>
                    <tr>
                        <td>Social Security Number</td>
                        <td>${user.ssn}</td>
                    </tr>
                    <tr>
                        <td>Location Name</td>
                        <td>${user.locationName}</td>
                    </tr>
                    <tr>
                        <td>Country</td>
                        <td>${user.countryName}</td>
                    </tr>
                    <tr>
                        <td>Active</td>
                    <#if currentUser.roles?seq_contains("ADMIN")>
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
                    <#else>
                        <#if user.active>
                            <td>${user.active?c}</td>
                        <#else>
                            <td>Administrator has not yet approved your registration request</td>
                        </#if>
                    </#if>
                    </tr>
                    <tr>
                        <td>Creation Date</td>
                        <td>${user.creationDate}</td>
                    </tr>
                    <#if currentUser.roles?seq_contains("ADMIN") && user.username != currentUser.username>
                    <tr>
                        <td>
                            <button class="btn btn-primary" id="download-items-button" type="button">
                                <span class="glyphicon glyphicon-download"></span> Download User Items</button>
                        </td>
                    </tr>
                    </#if>
                    </tbody>
                </table>
            </div>
            <div id="userData">
            </div>
        </div> <!-- /container -->
    </div>
</div>
<#include "footer.ftl">
</body>
</html>

