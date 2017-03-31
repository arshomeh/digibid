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

        var mapOptions = {
            center: new google.maps.LatLng(41.954663, 13.579102),
            zoom: 3,
            mapTypeId: google.maps.MapTypeId.ROADMAP
        };
        var infoWindow = new google.maps.InfoWindow();
        var latlngbounds = new google.maps.LatLngBounds();
        var map = new google.maps.Map(document.getElementById("dvMap"), mapOptions);
        google.maps.event.addListener(map, 'click', function (e) {
            $("#latitude").val(e.latLng.lat());
            $("#longitude").val(e.latLng.lng());
        });

        $("#itemCategories").selectpicker();

        $("#webItem .btn.dropdown-toggle.btn-default").css(
                {
                    width: "300px"
                }
        );

        $("#imageFile").fileinput(
                {
                    showPreview: false,
                    showUpload: false,
                    showRemove: false,
                    allowedFileExtensions: ["jpg", "png", "jpeg"],
                    maxImageWidth: 500,
                    maxImageHeight: 500,
                    allowedFileTypes: [
                        "image"
                    ]
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
                        url: "/items/user/${currentUser.username}/new",
                        data: new FormData($("#webItem")[0]),
                        enctype: "multipart/form-data",
                        processData: false,
                        contentType: false,
                        cache: false,
                        success: function (result) {
                            $.notify("The item added successfully.", "info");
                            $("#webItem")[0].reset();
                            window.location.href = "/user/${currentUser.username}";
                        },
                        error: function (data, textStatus, errorThrown) {
                            $.notify("Invalid data. The item couldn't be added.");
                        }
                    }
            );
        });
    });
</script>
<form id="webItem" name="webItem" action="/items/user/${currentUser.username}/new" method="POST" enctype="multipart/form-data"
      class="form-digibid">

    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

    <label for="name" class="sr-only">Name</label>
    <input type="text" name="name" class="form-control" id="name" placeholder="Name" required autofocus>

    <label for="itemCategories" class="sr-only">Categories</label>
    <select id="itemCategories" name="itemCategories" class="selectpicker" data-none-selected-text="Select Category"
            data-selected-text-format="count > 3" multiple required>
    <#list itemCategories as itemCategory>
        <option value="${itemCategory.id}">${itemCategory.name}</option>
    </#list>
    </select>

    <label for="firstBid" class="sr-only">First Bid</label>
    <input type="number" min="0" name="firstBid" class="form-control" id="firstBid" placeholder="First Bid" required>

    <label for="price" class="sr-only">Price</label>
    <input type="number" min="0" name="price" class="form-control" id="price" placeholder="Price">

    <label for="locationName" class="sr-only">Location</label>
    <input type="text" name="locationName" class="form-control" id="locationName" placeholder="Location" required>

    <label for="longitude" class="sr-only">Longitude</label>
    <input type="number" name="longitude" class="form-control" id="longitude" min="180,0000000" step="0.00000000000001" max="-180,0000000"
           placeholder="Longitude">

    <label for="latitude" class="sr-only">Latitude</label>
    <input type="number" name="latitude" class="form-control" id="latitude" min="90,0000000" step="0.00000000000001" max="-90,0000000"
           placeholder="Latitude">

    <div id="dvMap" style="margin-bottom: 10px; width: auto; height: 300px">
    </div>
    
    <label for="countryName" class="sr-only">Country</label>
    <input type="text" name="countryName" class="form-control" id="countryName" placeholder="Country" required>

    <label for="ends" class="sr-only">End Date</label>
    <input type="text" name="ends" class="form-control" id="ends" placeholder="End Date" data-provide="datepicker"
           required/>

    <label for="image" class="sr-only">Images</label>
    <input type="file" name="imageFile" class="form-control file" id="imageFile" data-show-preview="true"
           placeholder="Images"
           multiple>

    <label for="description" class="sr-only">Description:</label>
    <textarea name="description" class="form-control" id="description" rows="10" cols="30" required></textarea>

    <button class="btn btn-primary btn-block" id="newItemButton" type="submit">Submit</button>
<@spring.bind "webItem" />

<#if spring.status.error>
    <ul style="color:red; margin-top: 10px;">
        <#list spring.status.errorMessages as error>
            <li>${error}</li>
        </#list>
    </ul>
</#if>
</form>