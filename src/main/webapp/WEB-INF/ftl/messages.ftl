<#-- @ftlvariable name="messages" type="java.util.Map<java.lang.String, java.util.List<gr.uoa.di.digibid.model.WebMessage>>" -->
<style>
    tr.record:hover {
        cursor: pointer;
        color: #fff;
        background-color: #337ab7;
    }

    .record.selected {
        color: #fff;
        background-color: #337ab7;
    }

    .bold {
        font-weight: bold;
    }
</style>
<script>
    $(document).ready(function () {
        $("#userMessages").DataTable(
                {
                    "order": [[3, "desc"]],
                    responsive: true,
                    searching: false
                }
        );

        var messageId;
        var messageSubject;
        var selectedMessage = $("#userMessages tbody").on("click", ".record", function (e) {
            e.stopPropagation();

            messageId = $(this).attr("id");
            messageSubject = $(this).find("#subject")[0].attributes[1].value;

            $("#replyButton").show();
            $("#deleteButton").show();

            if (!$(this).hasClass("selected")) {
                $(".record").removeClass("selected");
                $(this).addClass("selected");
            }

            if ($(this).hasClass("bold")) {
                $(this).removeClass("bold");
                //TODO test this
//                $.get("/api/message/" + $(this).attr("id"));

                $.ajax(
                        {
                            type: "GET",
                            url: "/api/message/" + $(this).attr("id"),
                            success: function (result) {

                            },
                            error: function (XMLHttpRequest, textStatus, errorThrown) {
                                window.location.href = "/error";
                            }
                        }
                );
            }

            $("#messageInfo").html($(this).attr("content"));
            $("#messageInfo").show();
            $("#messageInfo").focus(); //TODO focus doesn't work , also fix focusing on textarea of content
        });

        $("#sendMessage").on("click", function (e) {
            e.preventDefault();

            stompClient.send("/app/message/send", {},
                    JSON.stringify({
                        "toUsername": $("#to").val(),
                        "fromUsername": "${currentUser.username}",
                        "subject": messageSubject,
                        "content": $("#content").val(),
                    })
            );

            $.notify("Message sent", "info");

            $("#reply").modal("hide");
        });

        $("#replyButton").on("click", function () {
            var to = $(selectedMessage).find("#toId")[0];
            var toId = to.attributes[1].value;
            var toName = to.attributes[2].value;

            var modal = $("#reply").modal();
            modal.find(".modal-title").text("Reply to " + toName);
            modal.find("#to").val(toId);
            modal.find("#subject").val(messageSubject);
        });

        $("#reply").on('hidden.bs.modal', function (e) {
            $(".modal-body form").trigger("reset");
        });

        $("#deleteButton").on("click", function () {
            var modal = $("#delete").modal();
            modal.find(".modal-title").text("Deleting message with subject \"" + messageSubject + "\". Are you sure?");
        });

        $("#deleteMessage").on("click", function (e) {
            e.preventDefault();

            $.ajax(
                    {
                        type: "GET",
                        url: "/api/message/" + messageId + "/delete",
                        success: function (result) {
                            if (result == true) {
                                $(selectedMessage).find(".selected")[0].remove();

                                $("#replyButton").hide();
                                $("#deleteButton").hide();

                                $("#messageInfo").html("");
                                $("#messageInfo").hide();

                                $.notify("Message deleted", "info");
                            } else {
                                $.notify("Couldn't delete the message");
                            }
                        },
                        error: function (XMLHttpRequest, textStatus, errorThrown) {
                            window.location.href = "/error";
                        }
                    }
            );

            $("#delete").modal("hide");
        });
    });
</script>
<table id="userMessages" class="table" cellspacing="0" width="100%">
    <thead>
    <tr>
        <th>${messages?keys?first}</th>
        <th>Subject</th>
        <th>Message</th>
        <th>Date</th>
    </tr>
    </thead>
    <tbody>
    <#list messages?values?first as message>
        <#if !message.isRead() && currentUser.username == message.to.username>
        <tr class="record bold" content="${message.content}" id="${message.id}">
        <#else>
        <tr class="record" content="${message.content}" id="${message.id}">
        </#if>
        <#if currentUser.username == message.from.username>
            <td>
                <div id="toId" toUsername="${message.to.username}"
                     toName="${message.to.firstName} ${message.to.lastName}">${message.to.firstName} ${message.to.lastName}</div>
            </td>
        <#else>
            <td>
                <div id="toId" toUsername="${message.from.username}"
                     toName="${message.from.firstName} ${message.from.lastName}">${message.from.firstName} ${message.from.lastName}</div>
            </td>
        </#if>
        <td>
            <div id="subject" subject="${message.subject}">${message.subject}</div>
        </td>
        <td>
            <#if message.content?length <= 40>
            ${message.content}
            <#else>
            ${message.content[0..40]}...
            </#if>
        </td>
        <td>${message.time}</td>
    </tr>
    </#list>
    </tbody>
</table>
<table>
    <tr>
        <td>
            <button type="button" class="btn btn-primary" data-toggle="modal" data-target="reply" id="replyButton"
                    style="display: none;">
                <span class="glyphicon glyphicon-send"></span> Reply
            </button>
        </td>
        <td>
            <button type="button" class="btn btn-primary" data-toggle="modal" data-target="delete" id="deleteButton"
                    style="display: none; margin-left: 15px;">
                <span class="glyphicon glyphicon-remove"></span> Delete
            </button>
        </td>
    </tr>
</table>
<div class="bd-delete">
    <div class="modal fade" id="delete" tabindex="-1" role="dialog" aria-labelledby="deleteModalLabel"
         aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                    <h4 class="modal-title" id="deleteModalLabel" id="deleteTitle"></h4>
                </div>
                <div class="modal-body">
                    <form>
                        <input type="hidden" name="id" class="form-control" id="messageId">
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal" id="closeDeleteMessage">Close
                    </button>
                    <button type="button" class="btn btn-primary" id="deleteMessage">Delete message</button>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="bd-reply">
    <div class="modal fade" id="reply" tabindex="-1" role="dialog" aria-labelledby="replyModalLabel"
         aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                    <h4 class="modal-title" id="replyModalLabel" id="replyTitle"></h4>
                </div>
                <div class="modal-body">
                    <form>
                        <input type="hidden" name="toId" class="form-control" id="to">
                        <input type="hidden" name="fromId" class="form-control" id="from">
                        <input type="hidden" name="subject" class="form-control" id="subject">

                        <div class="form-group">
                            <label for="content" class="form-control-label">Message:</label>
                            <textarea name="content" class="form-control" id="content"
                                      placeholder="Write your message here" style="height: 200px;" required
                                      autofocus></textarea>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal" id="closeSendMessage">Close
                    </button>
                    <button type="button" class="btn btn-primary" id="sendMessage">Send message</button>
                </div>
            </div>
        </div>
    </div>
</div>

<div id="messageInfo" style="display:none; font-size: 20px; margin-top: 15px;"></div>