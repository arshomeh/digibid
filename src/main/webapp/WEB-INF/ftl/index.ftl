<#-- @ftlvariable name="submitted" type="java.lang.Boolean" -->
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">

    <title>DigiBid | Welcome</title>
</head>
<body>
<#include "header.ftl">

<script>
    $(document).ready(function () {
        if (${submitted?c}) {
            $.notify("Registration request submitted. Our Administrator will approve your request as soon as possible.", "info");
        }

        var query = $.urlParam("q");
        var categories = $.urlParam("c");
        var locations = $.urlParam("l");
        var prices = $.urlParam("p");

        if ((query == undefined || query == "") &&
                (categories == undefined || categories == "") &&
                (locations == undefined || locations == "") &&
                (prices == undefined || prices == "")) {
            $("#container").load("/items");
        } else {
            var urlParams = "?q=" + (query == undefined ? "" : query) +
                    "&c=" + (categories == undefined ? "" : categories) +
                    "&l=" + (locations == undefined ? "" : locations) +
                    "&p=" + (prices == undefined ? "" : prices);

            $("#container").load("/items/search" + urlParams);
        }
    });
</script>
<div class="container" id="container">
</div> <!-- /container -->
<#include "footer.ftl">
</body>
</html>