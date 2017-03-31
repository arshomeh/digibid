<#-- @ftlvariable name="form" type="gr.uoa.di.digibid.model.WebUser" -->
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

    <title>DigiBid | Registration</title>
</head>
<body>
<#include "header.ftl">
<script>
    $(document).ready(function () {
        $("#countryName").easyAutocomplete(
                {
                    url: function (query) {
                        return "/api/countries/search?q=" + query;
                    },

                    getValue: function (element) {
                        return element.name;
                    },

                    list: {
                        sort: {
                            enabled: true
                        }
                    }
                }
        );
    });
</script>
<div class="container" id="container">

    <form role="form" name="form" action="" method="post" class="form-digibid">
        <h2>Register</h2>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

        <label for="email" class="sr-only">Email address</label>
        <input type="email" name="email" class="form-control" id="email" value="${form.email}"
               placeholder="Email address" required autofocus>

        <label for="password" class="sr-only">Password</label>
        <input type="password" name="password" id="password" class="form-control" placeholder="Password" required/>

        <label for="passwordRepeated" class="sr-only">Repeat Password</label>
        <input type="password" name="passwordRepeated" id="passwordRepeated" class="form-control"
               placeholder="Repeat Password" required/>

        <label for="username" class="sr-only">Username</label>
        <input type="text" name="username" class="form-control" id="username" value="${form.username}"
               placeholder="Username" required style="margin-top: 10px;"/>

        <label for="firstName" class="sr-only">First Name</label>
        <input type="text" name="firstName" class="form-control" id="firstName" value="${form.firstName}"
               placeholder="First Name" required/>

        <label for="lastName" class="sr-only">Last Name</label>
        <input type="text" name="lastName" class="form-control" id="lastName" value="${form.lastName}"
               placeholder="Last Name" required/>

        <label for="address" class="sr-only">Address</label>
        <input type="text" name="address" class="form-control" id="address" value="${form.address}"
               placeholder="Address" required/>

        <label for="locationName" class="sr-only">Location Name</label>
        <input type="text" name="locationName" class="form-control" id="locationName" value="${form.locationName}"
               placeholder="Location Name" required/>

        <label for="countryName" class="sr-only">Country</label>
        <input type="text" name="countryName" class="form-control" id="countryName" value="${form.countryName}"
               placeholder="Country" required/>

        <label for="phone" class="sr-only">Phone</label>
        <input type="number" min="0" name="phone" class="form-control" id="phone" value="${form.phone}" placeholder="Phone"
               required/>

        <label for="ssn" class="sr-only">Social Security Number</label>
        <input type="number" min="0" name="ssn" class="form-control" id="ssn" value="${form.ssn}"
               placeholder="Social Security Number" required/>

        <button class="btn btn-primary btn-block" type="submit">Submit</button>
    <@spring.bind "form" />

    <#if spring.status.error>
        <ul style="color:red; margin-top: 10px;">
            <#list spring.status.errorMessages as error>
                <li>${error}</li>
            </#list>
        </ul>
    </#if>
    </form>
</div> <!-- /container -->

<#include "footer.ftl">
</body>
</html>