var nextPage = 1

function clickLike(obj, postId) {
    net.post({
        url: "/user/like/mainpost?targetId=" + postId,
        toast: true,
        success: function (state) {
            if (state['like']) {
                $(obj).addClass('text-primary')
                $(obj).html(`已点赞 ${state['count']}`)
            } else {
                $(obj).removeClass('text-primary')
                $(obj).html(`点赞 ${state['count']}`)
            }
        }
    })
}

function clickPostImage(img) {
    $(img).toggleClass("active");
}

function drawMainPostItem(item) {
    function getImagesDiv(images) {
        if (images == null || images.urls.length == 0) {
            return ''
        } else {
            var html = "";
            images.urls.forEach(url => {
                html += `<img src=${url} onclick="clickPostImage(this)"/>`
            })
            return `<div class="post-item-images">${html}</div>`
        }
    }

    $(".post-list-layout").append(`<div class="post-item">
    <img class="post-item-head" src="/head/${item['userId']}"/>
    <div class="post-item-right">
        <div class="post-item-nickname">
            <a href="/space/posts/${item['userId']}" target="_blank">${item['nickname']}</a>
        </div>
        <div class="post-item-time">${item['time']}</div>
        <div class="post-item-title">
            <a href="/mainpost/details/view/${item['id']}" target="_blank">${item['title']}</a>
        </div>
        <div class="post-item-content">${item['content']}</div>
        ${getImagesDiv(item['images'])}
        <div class="post-item-option">
            <div class="post-item-op-like ${item['like'] ? 'text-primary' : ''}"
                onclick="clickLike(this,${item['id']})">
                ${item['like'] ? '已点赞' : '点赞'}&nbsp;${item['likeCount']}
            </div>
            <div><a href="/mainpost/details/view/${item['id']}" target="_blank">
                ${item['replyCount']}条回帖
            </a></div>
        </div>
    </div>
</div>`)
}

function drawMainPostList(p) {
    var page = p['pager'];
    if (page['size'] == 0) {
        $(".post-list-more").html('没有更多内容了')
    } else {
        if (page['currentPage'] >= page['pageCount']) {
            $(".post-list-more").html('没有更多内容了')
        } else {
            $(".post-list-more").html('加载更多')
        }
        nextPage = page['currentPage'] + 1
        p['data'].forEach(item => {
            drawMainPostItem(item)
        })
    }
}