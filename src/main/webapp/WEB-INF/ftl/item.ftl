<#-- @ftlvariable name="item" type="gr.uoa.di.digibid.model.WebItem" -->
<#-- @ftlvariable name="webBids" type="java.util.List<gr.uoa.di.digibid.model.WebBid>" -->
<#-- @ftlvariable name="itemImages" type="java.util.Map<java.lang.Long, java.util.List<gr.uoa.di.digibid.model.WebItemImage>>" -->
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

    <title>DigiBid | Item</title>
    <style>
        #map {
            height: 400px;
            margin-top: 25px;
        }
    </style>

<#include "header.ftl">
    <script>

        $(document).ready(function () {

            $(".image").on("click", function () {
                $("#imagepreview").attr("src", $(this).attr("src")); // here asign the image to the modal when the user click the enlarge link
                $("#imagemodal").modal("show"); // imagemodal is the id attribute assigned to the bootstrap modal, then i use the show function
            });

            $("#delete-button").on("click", function () {
                $.ajax(
                        {
                            type: "DELETE",
                            url: "/api/item/" + $(this).attr("itemId")
                            + "?${_csrf.parameterName}=${_csrf.token}",
                            success: function () {
                                $.notify("Item deleted", "info");

                                window.location.href = "/";
                            },
                            error: function (XMLHttpRequest, textStatus, errorThrown) {
                                $.notify("Couldn't delete the item");
                            }
                        }
                );
            });

            $("#activate-button").on("click", function () {
                $.ajax(
                        {
                            type: "POST",
                            url: "/api/item/" + $(this).attr("itemId") + "/activate"
                            + "?${_csrf.parameterName}=${_csrf.token}",
                            success: function () {
                                $.notify("Item activated", "info");

                                window.location.href = "/";
                            },
                            error: function (XMLHttpRequest, textStatus, errorThrown) {
                                $.notify("Couldn't activate the item");
                            }
                        }
                );
            });

            $("#direct-buy-button").on("click", function () {
                $("#direct-buy").modal();
            });

            $("#buy").on("click", function (e) {
                e.preventDefault();

                stompClient.send("/app/bid/place", {},
                        JSON.stringify({
                            "bidderUsername": $("#currentUserDiv").attr("user"),
                            "webItemId": "${item.id}",
                            "amount": "${item.price}"
                        })
                );

                $.notify("Congratulations item is yours", "info");

                $("#direct-buy").modal("hide");

                window.location.href = "/item/${item.id}";
            });

            $("#bid").on('hidden.bs.modal', function (e) {
                $("#bid-modal-body form").trigger("reset");
            });

            $("#bid-button").on("click", function () {
                $("#bid").modal();
            });

            $("#placeForm").on("submit", function (e) {
                e.preventDefault();

                stompClient.send("/app/bid/place", {},
                        JSON.stringify({
                            "bidderUsername": $("#currentUserDiv").attr("user"),
                            "webItemId": "${item.id}",
                            "amount": $("#bid-amount").val()
                        })
                );

                $.notify("Bid placed", "info");

                $("#bid").modal("hide");

                window.location.href = "/item/${item.id}";
            });

            $("#message").on('hidden.bs.modal', function (e) {
                $("#message-modal-body form").trigger("reset");
            });

            $("#send-message-button").on("click", function () {
                $("#message").modal();
            });

            $("#send").on("click", function (e) {
                e.preventDefault();

                stompClient.send("/app/message/send", {},
                        JSON.stringify({
                            "toUsername": $("#sellerUserDiv").attr("user"),
                            "fromUsername": $("#currentUserDiv").attr("user"),
                            "subject": $("#message-subject").val(),
                            "content": $("#message-content").val()
                        })
                );

                $.notify("Message sent", "info");

                $("#message").modal("hide");
            });

            $("#edit").on('hidden.bs.modal', function (e) {
                $("#edit-modal-body form").trigger("reset");
            });

            $("#edit-button").on("click", function () {
                $.get("/item/${item.id}/edit")
                        .done(function (data) {
                            $("#edit-modal-body").html(data);
                            $("#edit").modal();
                        });
            });
        });
    </script>
</head>
<body>

<#if item.longitude?has_content && item.latitude?has_content>
<script>
    function initMap() {

        var lat =  ${item.latitude};
        var lng =  ${item.longitude};
        if (lat != "" && lng != "") {
            var mapProp = {
                center: new google.maps.LatLng(lat, lng),
                zoom: 16,
                mapTypeId: google.maps.MapTypeId.ROADMAP
            };
            var map = new google.maps.Map(document.getElementById("map"), mapProp);
            var marker = new google.maps.Marker({
                position: new google.maps.LatLng(lat, lng),
            });
            marker.setMap(map);
        }
    }
</script>

<script async defer
        src="https://maps.googleapis.com/maps/api/js?key=AIzaSyACIqPZEON3aieBSGAVaqVGiCnz_TZZVxc&callback=initMap">
</script>
</#if>
<div class="container" id="container">
    <div class="row">
        <div class="col-sm-4">
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
                <h3><a href="/item/${item.id}">${item.name}</a></h3>
                <p>${item.description}</p>
                <ul>
                <#if (item.currently?number >= item.price?number || item.endsFormatted == "0d 0h 0m")>
                    <li><b>Ended</b></li>
                <#else>
                    <li><b>Ends in </b>${item.endsFormatted} (${item.ends})</li>
                </#if>
                    <li><b>${item.locationName}</b></li>
                    <li><b>${item.countryName}</b></li>
                    <li><b>Current bid is at </b>&euro;${item.currently}</li>
                    <li><b>Starting bid is at </b>&euro;${item.firstBid}</li>
                <#if (item.price?number > 0)>
                    <li><b>Buying price is </b>&euro;${item.price}</li>
                </#if>
                    <li><b>Total bids are </b>${webBids?size}</li>
                    <li><b>The item is being auctioned by ${item.seller.username} with
                        rating ${item.seller.sellerRating}</b></li>
                </ul>
            </div>
            <!-- TODO REMOVE CLASS btn-primary ADD SUCCESS AND DISABLE THE REST BUTTONS -->
        <#if currentUser?? && !currentUser.roles?seq_contains("ADMIN")>
            <#if currentUser.username == item.sellerUsername>
                <#if !item.started?has_content>
                    <button itemId="${item.id}" class="btn btn-primary" id="activate-button" type="button">
                        <span class="glyphicon glyphicon-ok"></span> Activate
                    </button>
                </#if>
                <#if currentUser.username == item.sellerUsername && webBids?size == 0>
                    <button itemId="${item.id}" class="btn btn-primary" id="delete-button" type="button">
                        <span class="glyphicon glyphicon-remove"></span> Delete
                    </button>
                    <button itemId="${item.id}" class="btn btn-primary" id="edit-button" type="button">
                        <span class="glyphicon glyphicon-edit"></span> Edit
                    </button>
                </#if>

                <table class="table">
                    <#list webBids as bid>
                        <tr>
                            <td>
                                Bidder:<b> ${bid.bidderUsername}</b>
                            </td>
                            <td>
                                Amount:<b> &euro;${bid.amount}</b>
                            </td>
                            <td>
                                Date:<b> ${bid.time}</b>
                            </td>
                        </tr>
                    </#list>
                </table>
            <#else>
                <div id="sellerUserDiv" user="${item.sellerUsername}"></div>
                <div id="currentUserDiv" user="${currentUser.username}"></div>
                <#if item.price?? && (item.price?number > 0)>
                    <#if (item.currently?number >= item.price?number || item.endsFormatted == "0d 0h 0m")>
                        <button class="btn btn-danger" id="direct-buy-button" disabled>
                            <span class="glyphicon glyphicon-lock"></span> Ended
                        </button>
                        <button class="btn btn-primary" id="bid-button" disabled>
                            <span class="glyphicon glyphicon-plus"></span> Place Bid
                        </button>
                    <#else>
                        <button class="btn btn-success" id="direct-buy-button">
                            <span class="glyphicon glyphicon-shopping-cart"></span> Direct Buy
                            for &euro;${item.price}</button>
                        <button class="btn btn-primary" id="bid-button">
                            <span class="glyphicon glyphicon-plus"></span> Place Bid
                        </button>
                    </#if>
                    <#if (item.currently?number >= item.price?number || item.endsFormatted == "0d 0m 0h") && currentUser.username == item.currentBidUsername>
                        <button class="btn btn-primary" id="send-message-button" data-toggle="modal"
                                data-target="message">
                            <span class="glyphicon glyphicon-send"></span> Send Message to ${item.seller.username}
                        </button>
                    <#else>
                        <button class="btn btn-primary" id="send-message-button" data-toggle="modal"
                                data-target="message"
                                disabled>
                            <span class="glyphicon glyphicon-send"></span> Send Message to ${item.seller.username}
                        </button>
                    </#if>
                <#else>
                    <button class="btn btn-primary" id="bid-button">
                        <span class="glyphicon glyphicon-plus"></span> Place Bid
                    </button>
                    <#if item.endsFormatted == "0d 0m 0h" && currentUser.username == item.currentBidUsername>
                    <button class="btn btn-primary" id="send-message-button" data-toggle="modal" data-target="message">
                        <span class="glyphicon glyphicon-send"></span> Send Message to ${item.seller.username}</button>
                <#else>
                    <button class="btn btn-primary" id="send-message-button" data-toggle="modal" data-target="message"
                            disabled>
                        <span class="glyphicon glyphicon-send"></span> Send Message to ${item.seller.username}</button>
                </#if>
            </#if>
        </#if>
        </#if>
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
</div> <!-- /container -->

<div class="bd-message">
    <div class="modal fade" id="message" tabindex="-1" role="dialog" aria-labelledby="messageModalLabel"
         aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                    <h4 class="modal-title" id="messageModalLabel" id="messageTitle">Send message
                        to ${item.sellerUsername}</h4>
                </div>
                <div class="modal-body" id="message-modal-body">
                    <form>
                        <div class="form-group">
                            <label for="message-subject" class="form-control-label">Subject:</label>
                            <input type="text" name="subject" class="form-control" placeholder="Subject"
                                   id="message-subject" value="${item.name}-${item.id}" required>

                            <label for="message-text" class="form-control-label">Message:</label>
                            <textarea name="message-content" class="form-control" id="message-content"
                                      placeholder="Write your message here" style="height: 200px;" required
                                      autofocus></textarea>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal" id="close">Close
                    </button>
                    <button type="button" class="btn btn-primary" id="send">Send</button>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="bd-bid">
    <div class="modal fade" id="bid" tabindex="-1" role="dialog" aria-labelledby="messageModalLabel"
         aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                    <h4 class="modal-title" id="bidModalLabel" id="bidTitle">Place your bid for ${item.name}</h4>
                </div>
                <div class="modal-body" id="bid-modal-body">
                    <form id="placeForm">
                        <div class="form-group">
                            <input type="number" name="amount" class="form-control" placeholder="Place your bid amount"
                                   id="bid-amount"
                                   min="${(item.currently?number == 0)?then(item.firstBid?number + 1,item.currently?number + 1)}"
                                   required>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal" id="close">Close
                            </button>
                            <button type="submit" class="btn btn-primary" id="place">Place</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="bd-direct-buy">
    <div class="modal fade" id="direct-buy" tabindex="-1" role="dialog" aria-labelledby="messageModalLabel"
         aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                    <h4 class="modal-title" id="directBuyModalLabel" id="directBuyTitle">You are going to
                        buy ${item.name}
                        for &euro;${item.price}. Are you sure?</h4>
                </div>
                <div class="modal-body" id="direct-buy-modal-body">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal" id="close">No
                    </button>
                    <button type="button" class="btn btn-primary" id="buy">Yes</button>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="bd-edit">
    <div class="modal fade" id="edit" tabindex="-1" role="dialog" aria-labelledby="editModalLabel"
         aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                    <h4 class="modal-title" id="editModalLabel" id="editTitle">Edit the item ${item.name}</h4>
                </div>
                <div class="modal-body" id="edit-modal-body">

                </div>
            </div>
        </div>
    </div>
</div>

<#if item.longitude?has_content && item.latitude?has_content>
<div class="col-md-8" style="width: 100%">
    <div id="map"></div>
</div>
</#if>
<#include "footer.ftl">
</body>
</html>