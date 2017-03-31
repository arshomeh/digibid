<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
	<meta name="description" content="">
	<meta name="author" content="">

	<title>DigiBid | Contact</title>

	<style>
	.form-contact {
		max-width: 330px;
		padding: 15px;
		margin: 0 auto;
	}

	.form-contact .form-control {
		position: relative;
		height: auto;
		-webkit-box-sizing: border-box;
		-moz-box-sizing: border-box;
		box-sizing: border-box;
		padding: 10px;
		font-size: 16px;
	}

	.form-contact .form-control:focus {
		z-index: 2;
	}

	.form-contact input[type="email"],
	.form-contact input[type="text"],
	.form-contact input[type="password"] {
		margin-bottom: 10px;
		border-top-left-radius: 0;
		border-top-right-radius: 0;
	}
	</style>
</head>
<body>
	<#include "header.ftl">

	<script>
    $(document).ready(function () {
        $("#countryName").easyAutocomplete({
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
        });
    });
	</script>

	<div class="container">
		<form role="form" class="form-contact" action="" method="post" name="form">
			<h2>Contact Us</h2>
			<label for="firstName" class="control-label">Your Name</label>
        	<input type="text" name="firstName" class="form-control" id="firstName" value="" placeholder="First Name" required autofocus/>

			<label for="email" class="control-label">Email address</label>
			<input type="email" name="email" class="form-control" id="email" value="" placeholder="Email address" required/>

			<label for="firstName" class="control-label">Subject</label>
        	<input type="text" name="firstName" class="form-control" id="subject" value="" placeholder="Subject" required />
	
			<label for="txt_msg" class="control-label">Massage</label>
			<textarea class="form-control" name="txt_msg" rows="4" placeholder="Massage" required></textarea>
			<br>
			<input name="submit" type="submit" class="btn btn-primary btn-block"  value="Submit" />
		</form>
	</div>
	<#include "footer.ftl">
</body>
</html>