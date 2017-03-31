<#-- @ftlvariable name="itemCategories" type="java.util.List<gr.uoa.di.digibid.model.WebItemCategory>" -->
<#-- @ftlvariable name="webItem" type="gr.uoa.di.digibid.model.WebItem" -->
<#-- @ftlvariable name="_csrf" type="org.springframework.security.web.csrf.CsrfToken" -->

<#import "/spring.ftl" as spring>
<style>
    .btn-file {
        height: 47px;
    }

    .glyphicon-folder-open {
        margin-top: 8px;
    }
</style>
<script>
    $(document).ready(function () {

        var categories = "${webItem.itemCategories?join(",", "")}";
        if (categories == "") {
            $("#itemCategories").selectpicker();
        } else {
            var spl = categories.split(",");
            var c = [];
            for(var i = 0; i < spl.length; i++ ){
                c.push(spl[i]);
            }

            $("#itemCategories").val(c);
            $("#itemCategories").selectpicker("render");
        }

        $("#webItem .btn.dropdown-toggle.btn-default").css(
                {
                    width: "300px"
                }
        );

        $("#ends").datetimepicker(
                {
                    sideBySide: false,
                    useCurrent: false,
                    minDate: Date.today().add(1).days(),
                    format: "DD-MM-YYYY HH:mm"
                }
        );

        $("#ends").val("${webItem.ends}");

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

        $("#webItem").on("submit", function (e) {
            e.preventDefault();

            $.ajax(
                    {
                        type: "POST",
                        url: "/item/${webItem.id}/edit",
                        data: new FormData($("#webItem")[0]),
                        enctype: "multipart/form-data",
                        processData: false,
                        contentType: false,
                        cache: false,
                        success: function (result) {
                            $.notify("The item edit successfully.", "info");
                            $("#webItem")[0].reset();
                            window.location.href = "/item/${webItem.id}";
                        },
                        error: function (data, textStatus, errorThrown) {
                            console.log(textStatus, errorThrown);
                            $.notify("Invalid data. The item couldn't be added.");
                        }
                    }
            );
        });
    });
</script>
<form id="webItem" name="webItem" action="" method="POST"
      enctype="multipart/form-data"
      class="form-digibid">

    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

    <label for="name" class="sr-only">Name</label>
    <input type="text" name="name" class="form-control" id="name" placeholder="Name" value="${webItem.name}" required
           autofocus>

    <label for="itemCategories" class="sr-only">Categories</label>
    <select id="itemCategories" name="itemCategories" class="selectpicker" data-none-selected-text="Select Category"
            data-selected-text-format="count > 3" multiple required>
    <#list itemCategories as itemCategory>
    <#--TODO handle categories pre load-->
        <option value="${itemCategory.id}">${itemCategory.name}</option>
    </#list>
    </select>

    <label for="firstBid" class="sr-only">First Bid</label>
    <input type="number" min="0" name="firstBid" class="form-control" id="firstBid" placeholder="First Bid"
           value="${webItem.firstBid}" required>

    <label for="price" class="sr-only">Price</label>
    <input type="number" min="0" name="price" class="form-control" id="price" value="${webItem.price}"
           placeholder="Price">

    <label for="locationName" class="sr-only">Location</label>
    <input type="text" name="locationName" class="form-control" id="locationName" placeholder="Location"
           value="${webItem.locationName}" required>

    <label for="longitude" class="sr-only">Longitude</label>
    <input type="number" name="longitude" class="form-control" id="longitude" placeholder="Longitude" min="180,0000000"
           step="0.00000000000001" max="-180,0000000"
           value="${webItem.longitude}">

    <label for="latitude" class="sr-only">Latitude</label>
    <input type="number" name="latitude" class="form-control" id="latitude" placeholder="Latitude" min="90,0000000" step="0.00000000000001"
           max="-90,0000000"
           value="${webItem.latitude}">

    <label for="countryName" class="sr-only">Country</label>
    <input type="text" name="countryName" class="form-control" id="countryName" placeholder="Country"
           value="${webItem.countryName}" required>

    <label for="ends" class="sr-only">End Date</label>
    <input type="text" name="ends" class="form-control" id="ends" placeholder="End Date" data-provide="datepicker"
           value="${webItem.ends}"
           required/>

    <label for="description" class="sr-only">Description:</label>
    <textarea name="description" class="form-control" id="description" rows="10" cols="30"
              required>${webItem.description}</textarea>

    <button class="btn btn-primary btn-block" id="editItemButton" type="submit">Save</button>
<@spring.bind "webItem" />

<#if spring.status.error>
    <ul style="color:red; margin-top: 10px;">
        <#list spring.status.errorMessages as error>
            <li>${error}</li>
        </#list>
    </ul>
</#if>
</form>