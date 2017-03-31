<#-- @ftlvariable name="items" type="java.util.List<gr.uoa.di.digibid.model.WebItem>" -->
<#-- @ftlvariable name="itemImages" type="java.util.Map<java.lang.Long, java.util.List<gr.uoa.di.digibid.model.WebItemImage>>" -->
<#-- @ftlvariable name="totalPages" type="java.lang.Integer" -->
<#-- @ftlvariable name="totalItems" type="java.lang.Integer" -->
<#-- @ftlvariable name="currentPage" type="java.lang.Integer" -->
<style>
    .carousel-control.left, .carousel-control.right {
        background-image: none;
        color: #23527C;
    }

    .carousel-indicators li {
        background-color: #23527C;
    }

    .carousel-indicators .active {
        background-color: #F5F5F5;
    }

    .nav.filter {
        max-height: 300px;
        min-width: 150px;
        overflow-y: scroll;
    }
</style>
<script>
    $(document).ready(function () {

        if (location.href.indexOf("/user/") != -1) {
            $("#filters").html("");
        }

        $("#paging").bootpag(
                {
                    total: ${totalPages},
                    page: ${currentPage},
                    maxVisible: 5,
                    leaps: false,
                    firstLastUse: true,
                    first: '←',
                    last: '→',
                    wrapClass: 'pagination',
                    activeClass: 'active',
                    disabledClass: 'disabled',
                    nextClass: 'next',
                    prevClass: 'prev',
                    lastClass: 'last',
                    firstClass: 'first'
                }
        ).on("page", function (event, num) {

            var query = $.urlParam("q");
            var categories = $.urlParam("c");
            var locations = $.urlParam("l");
            var prices = $.urlParam("p");

            if ((query == undefined || query == "") &&
                    (categories == undefined || categories == "") &&
                    (locations == undefined || locations == "") &&
                    (prices == undefined || prices == "")) {
                $("#container").load("/items?page=" + (num - 1));
            } else {
                var urlParams = "?page=" + num + "&q=" + (query == undefined ? "" : query) +
                        "&c=" + (categories == undefined ? "" : categories) +
                        "&l=" + (locations == undefined ? "" : locations) +
                        "&p=" + (prices == undefined ? "" : prices);

                $("#container").load("/items/search" + urlParams);
            }
        });

        $(".image").on("click", function () {
            $("#imagepreview").attr("src", $(this).attr("src")); // here asign the image to the modal when the user click the enlarge link
            $("#imagemodal").modal("show"); // imagemodal is the id attribute assigned to the bootstrap modal, then i use the show function
        });

        var query = $.urlParam("q");

        if (query != undefined) {
            $("#query").val(decodeURIComponent(query));
        }

        var categories = $.urlParam("c");

        if (categories != undefined) {
            for (var i = 0; i < categories.length; i++) {
                $(".category:checkbox[value='" + categories[i] + "']").prop("checked", "checked");
            }
        }

        var locations = $.urlParam("l");

        if (locations != undefined) {
            for (var i = 0; i < locations.length; i++) {
                $(".location:checkbox[value='" + decodeURIComponent(locations[i]) + "']").prop("checked", "checked");
            }
        }

        var prices = $.urlParam("p");

        if (prices != undefined) {
            for (var i = 0; i < prices.length; i++) {
                $(".price:checkbox[value='" + prices[i] + "']").prop("checked", "checked");
            }
        }

        $(".category").on("click", function () {
            $.search();
        });

        $(".location").on("click", function () {
            $.search();
        });

        $(".price").on("click", function () {
            $.search();
        });
    });
</script>
<div class="container">
    <div id="filters" class="col-sm-3">
        <form>
            <div class="row">
                <div class="sidebar-offcanvas" id="sidebar" role="navigation" style="float:left">
                    <div class="well sidebar-nav">
                        <ul class="nav filter">
                            <li><strong>Category</strong></li>
                        <#list itemCategories as itemCategory>
                            <li>
                                <div class="checkbox">
                                    <label><input name="c" class="category" type="checkbox"
                                                  value="${itemCategory.id}">${itemCategory.name}
                                    </label>
                                </div>
                            </li>
                        </#list>
                        </ul>
                    </div><!--/.well -->
                </div><!--/span-->
            </div><!--/row-->
            <div class="row">
                <div class="sidebar-offcanvas" id="sidebar" role="navigation" style="float:left">
                    <div class="well sidebar-nav">
                        <ul class="nav filter">
                            <li><strong>Location</strong></li>
                        <#list locations as location>
                            <li>
                                <div class="checkbox">
                                    <label><input name="l" class="location" type="checkbox"
                                                  value="${location?html}">${location?html}
                                    </label>
                                </div>
                            </li>
                        </#list>
                        </ul>
                    </div><!--/.well -->
                </div><!--/span-->
            </div>
            <div class="row">
                <div class="sidebar-offcanvas" id="sidebar" role="navigation" style="float:left">
                    <div class="well sidebar-nav">
                        <ul class="nav filter">
                            <li>
                                <strong>Price range</strong>
                            </li>
                        <#list prices as price>
                            <li>
                                <div class="checkbox">
                                    <label><input name="p" class="price" type="checkbox"
                                                  value="${price}">&euro; ${price}</label>
                                </div>
                            </li>
                        </#list>
                        </ul>
                    </div><!--/.well -->
                </div><!--/span-->
            </div>
        </form>
    </div>

    <div id="totalItemsFound">
        <b style="margin-left: 15px;">Found ${totalItems} item(s).</b>
    </div>
    <div id="paging" class="col-sm-8">
    <#list items as item>
        <div class="row">
            <div class="col-sm-3">
                <#list itemImages as itemId, images>
                    <#if itemId == item.id>
                        <#list images as image>
                            <div style="margin-top: 25px;">
                                <img class="image" src="${image.data}" alt="${image.description}"
                                     class="img-responsive img-rounded" style="width: 100%; min-height: 100%;"/>
                            </div>
                        </#list>
                    </#if>
                </#list>
            </div>
            <div class="col-sm-8">
                <div>
                    <h4><a href="/item/${item.id}">${item.name}</a></h4>
                    <p>${item.description[0..*300]}...</p>
                </div>
            </div>

            <div class="modal fade" id="imagemodal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
                 aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <img src="" id="imagepreview" class="img-responsive img-rounded"
                             style="width: 100%; min-height: 100%;">
                    </div>
                </div>
            </div>
        </div>
    </#list>
    </div>
</div>