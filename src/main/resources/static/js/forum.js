/**
 * 提交回复
 */
function post() {
    var postId = $("#post_id").val();
    var content = $("#comment_content").val();

    comment2target(postId, 1, content);
}

function comment2target(targetId, type, content) {

    if (!content) {
        alert("回复内容不能为空");
        return;
    }

    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: "application/json",
        data: JSON.stringify({
            "parentId": targetId,
            "content": content,
            "type": type
        }),
        success: function (response) {
            if (response.code == 200) {
                window.location.reload();
            } else {
                if (response.code == 2003) {
                    var isAccepted = confirm(response.message);
                    if (isAccepted) {
                        window.open("https://github.com/login/oauth/authorize?client_id=73f8ee46b1910380454e&redirect_uri=http://localhost:1310/callback&scpoe=user&state=1")
                        window.localStorage.setItem("closable", true);
                    }

                } else {

                    alert(response.message);
                }
            }
        },
        dataType: "json"
    })
}

function comment(e) {
    var commentId = e.getAttribute("data-id");
    var content = $("#input-" + commentId).val();
    comment2target(commentId, 2, content);
}

/**
 *展开或折叠二级评论
 */
function collapseComments(e) {
    var id = e.getAttribute("data-id");
    var comment = $("#comment-" + id);
    if (comment.hasClass("in")) {

    } else {
        var subCommentContainer = $("#comment-" + id);

        if (subCommentContainer.children().length == 1) {
            $.getJSON("/comment/" + id, function (data) {
                $.each(data.data, function (index, comment) {

                    var mediaLeftElement = $("<div/>", {
                        "class": "media-left"
                    }).append($("<img/>", {
                        "class": "media-object img-thumbnail",
                        "src": comment.user.avatarUrl
                    }));

                    var mediaBodyElement = $("<div/>", {
                        "class": "media-body"
                    }).append($("<h5/>", {
                        "class": "media-heading",
                        "html": comment.user.name
                    })).append($("<div/>", {
                        "html": comment.content
                    })).append($("<div/>", {
                        "class": "comment-menu"
                    }).append($("<span/>", {
                        "class": "pull-right",
                        "html": moment(comment.gmtCreate).format('YYYY-MM-DD')
                    })));

                    var mediaElement = $("<div/>", {
                        "class": "media"
                    }).append(mediaLeftElement)
                        .append(mediaBodyElement);

                    var commentElement = $("<div/>", {
                        "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comment",
                    }).append(mediaElement);
                    subCommentContainer.prepend(commentElement);
                });
            });
        }
    }
    e.classList.toggle("active");
    comment.toggleClass("in");
}

function selectTag(e) {
    var value = e.getAttribute("data-tag");
    var previous = $("#tag").val();
    if (previous.indexOf(value) == -1) {
        if (previous) {
            $("#tag").val(previous + ',' + value);
        } else {
            $("#tag").val(value);
        }
    }
}

function showSelectTag(){
    $("#select-tag").show();
}