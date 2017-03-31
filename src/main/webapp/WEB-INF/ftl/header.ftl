<#-- @ftlvariable name="itemCategories" type="java.util.List<gr.uoa.di.digibid.model.WebItemCategory>" -->
<#-- @ftlvariable name="currentUser" type="gr.uoa.di.digibid.model.CurrentUser" -->
<#-- @ftlvariable name="c" type="java.util.List<java.lang.Long>" -->
<#-- @ftlvariable name="q" type="java.lang.String" -->
<#-- @ftlvariable name="_csrf" type="org.springframework.security.web.csrf.CsrfToken" -->
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">

    <!-- Latest compiled and minified JavaScript -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"
            integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
            crossorigin="anonymous"></script>
    <!-- Latest compiled and minified JavaScript -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.11.0/js/bootstrap-select.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/easy-autocomplete/1.3.5/jquery.easy-autocomplete.min.js"
            type="text/javascript"></script>

    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.11.0/css/bootstrap-select.min.css">
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/easy-autocomplete/1.3.5/easy-autocomplete.min.css">

    <script src="https://cdn.datatables.net/1.10.12/js/jquery.dataTables.min.js" type="text/javascript"></script>
    <script src="https://cdn.datatables.net/1.10.12/js/dataTables.bootstrap.min.js" type="text/javascript"></script>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootpag/1.0.7/jquery.bootpag.min.js"
            type="text/javascript"></script>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.1.1/sockjs.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/notify/0.4.2/notify.min.js"></script>

    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.17.42/css/bootstrap-datetimepicker.min.css">

    <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.15.0/moment.min.js" type="text/javascript"></script>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.17.42/js/bootstrap-datetimepicker.min.js"
            type="text/javascript"></script>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/datejs/1.0/date.min.js" type="text/javascript"></script>

    <link rel="stylesheet"
          href=" https://cdnjs.cloudflare.com/ajax/libs/bootstrap-fileinput/4.3.4/css/fileinput.min.css">

    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-fileinput/4.3.4/js/fileinput.min.js"
            type="text/javascript"></script>

    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyACIqPZEON3aieBSGAVaqVGiCnz_TZZVxc"
            type="text/javascript"></script>

    <style>
        .form-digibid {
            max-width: 330px;
            padding: 15px;
            margin: 0 auto;
        }

        .form-digibid .checkbox {
            margin-bottom: 10px;
        }

        .form-digibid .checkbox {
            font-weight: normal;
        }

        .form-digibid .form-control {
            position: relative;
            height: auto;
            -webkit-box-sizing: border-box;
            -moz-box-sizing: border-box;
            box-sizing: border-box;
            padding: 10px;
            font-size: 16px;
        }

        .form-digibid .form-control:focus {
            z-index: 2;
        }

        .form-digibid .bootstrap-select,
        .form-digibid .file-input,
        .form-digibid textarea,
        .form-digibid input {
            margin-bottom: 10px;
            border-top-left-radius: 0;
            border-top-right-radius: 0;
        }
    </style>
</head>
<body>
<script>
    var stompClient;

    $.search = function () {
        var q = $("#query").val();
        var c = $(".category:checkbox[name='c']:checked").serialize();
        var l = $(".location:checkbox[name='l']:checked").serialize();
        var p = $(".price:checkbox[name='p']:checked").serialize();

        window.location.href = "/?q=" + q +
                               "&" + (c == "" ? "c=" : c) +
                               "&" + (l == "" ? "l=" : l) +
                               "&" + (p == "" ? "p=" : p);
    };

    $.urlParam = function (name) {

        var values = [];

        if (location.search != "") {
            var params = location.search.split("?")[1];
            if (params != undefined) {
                var paramParts = params.split("&");
                for (var i = 0; i < paramParts.length; i++) {
                    var paramNameAndValue = paramParts[i].split("=");
                    if (paramNameAndValue != undefined && paramNameAndValue.length == 2) {
                        if (paramNameAndValue[0] == name) {
                            if (values[paramNameAndValue[0]] == undefined) {
                                values[paramNameAndValue[0]] = [];
                            }
                            values[paramNameAndValue[0]].push(paramNameAndValue[1]);
                        }
                    }
                }
            }
        }

        return values[name];
    };

    $(document).ready(function () {


        $("#query").easyAutocomplete(
                {
                    url: function (query) {
                        return "/api/items/search?q=" + query;
                    },

                    getValue: function (element) {
                        $("#itemIdFromSearch").val(element.id);
                        return element.name;
                    },

                    list: {
                        onClickEvent: function() {
                            window.location.href = "/item/" + $("#itemIdFromSearch").val();
                        },
                        sort: {
                            enabled: true
                        }
                    }
                }
        );

        $("#searchForm .easy-autocomplete").css(
                {
                    float: "left"
                }
        );

        var currentUserName = $(".currentUserName").attr("username");
        var isAdmin = $(".currentUserName").attr("admin");
        if (currentUserName != "" && isAdmin == "false") {

            if (stompClient == null || stompClient == undefined) {
                var socket = new SockJS("/message/send");
                stompClient = Stomp.over(socket);
                stompClient.connect({}, function () {
                    stompClient.subscribe("/user/message/receive", function (message) {
                        $.notify("You've got 1 new message");
                    });

                    stompClient.subscribe("/user/bid/receive", function (bid) {
                        $.notify("New bid has been placed");
                    });
                });
            }

            $.ajax(
                    {
                        type: "GET",
                        url: "/api/user/" + currentUserName + "/messages/unread",
                        success: function (result) {
                            console.log("Got " + result + " unread messages");
                            if (result > 0) {
                                var currentUserFullName = $(".currentUserFullName").attr("id");
                                $.notify("Hello " + currentUserFullName + ". You have " + result + " unread " + (result == 1
                                                 ? "message"
                                                 : "messages") + ".", "info");
                                $(".badge").html(result);
                            }
                        },
                        error: function (XMLHttpRequest, textStatus, errorThrown) {
                            console.log(XMLHttpRequest);
                            console.log(textStatus);
                            console.log(errorThrown);
                        }
                    }
            );
        }

        $("#logout").on("click", function (e) {
            e.preventDefault();

            if (stompClient != null) {
                stompClient.disconnect();
            }

            $(this).unbind("click").click();
        });

        $("#searchForm").on("submit", function (e) {
            e.preventDefault();

            $.search();
        })
    });
</script>

<!-- Fixed navbar -->
<nav class="navbar navbar-default navbar-static-top">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar"
                    aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="/">DigiBid</a>
        </div>
        <div id="navbar" class="collapse navbar-collapse">
            <ul class="nav navbar-nav navbar-right">
                <li>
                    <form id="searchForm" class="navbar-form navbar-right" action="" style="margin-right: auto;">
                        <input id="query" name="q" type="text" class="form-control" placeholder="Search..."
                               style="width: auto; min-width: 450px; margin-right: 5px;" required>
                        <button id="searchButton" class="btn btn-primary" style="float: left; margin-left: 15px;">
                            <span class="glyphicon glyphicon-search"></span> Search
                        </button>
                    </form>
                </li>
            <#if !currentUser??>
                <div class="currentUserName" style="display: none;" username="" admin="false"></div>
                <li><a href="/login"><span class="glyphicon glyphicon-log-in"></span> Log in</a></li>
                <li><a href="/user/register">
                    <span class="glyphicon glyphicon-pencil"></span> Register</a></li>
            </#if>
            <#if currentUser??>
                <div class="currentUserName" style="display: none;" username="${currentUser.username}"
                     admin="${currentUser.roles?seq_contains("ADMIN")?c}"></div>
                <div class="currentUserFullName" style="display: none;" id="${currentUser.fullName}"></div>
                <li>
                    <form class="navbar-form navbar-right" action="/logout?${_csrf.parameterName}=${_csrf.token}"
                          method="post">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <button id="logout" class="btn btn-link" type="submit"><span
                                class="glyphicon glyphicon-log-out"></span> Log
                            out
                        </button>
                    </form>
                </li>
                <li><a href="/user/${currentUser.username}"><span class="glyphicon glyphicon-user"></span>
                ${currentUser.username}<span class="badge pull-right" style="margin-left: 2px;"></span></a>
                </li>
            </#if>
            <#if currentUser?? && currentUser.roles?seq_contains("ADMIN")>
                <li><a href="/users"><span class="glyphicon glyphicon-cog"></span> Manage</a></li>
            </#if>
                <li><a href="/help"><span class="glyphicon glyphicon-question-sign"></span> Help</a></li>
            </ul>
        </div><!--/.nav-collapse -->
    </div>
    <input id="itemIdFromSearch" type="text" hidden />
</nav>
</body>
</html>