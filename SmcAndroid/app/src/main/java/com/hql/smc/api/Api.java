package com.hql.smc.api;

import com.hql.smc.async.Async;
import com.hql.smc.data.PagerData;
import com.hql.smc.data.result.Da;
import com.hql.smc.data.result.Follow;
import com.hql.smc.data.result.FollowState;
import com.hql.smc.data.result.Images;
import com.hql.smc.data.result.LikeState;
import com.hql.smc.data.result.MainPost;
import com.hql.smc.data.result.Notice;
import com.hql.smc.data.result.NoticeDetails;
import com.hql.smc.data.result.Post;
import com.hql.smc.data.result.PostDraft;
import com.hql.smc.data.result.Reply;
import com.hql.smc.data.result.SpaceValues;
import com.hql.smc.data.result.User;

import java.io.File;
import java.util.List;

public class Api {
    public static Async.Builder<User> login(String username, String password) {
        return ExRequestBuilder.post("/user/api/login")
                .form()
                .field("username", username)
                .field("password", password)
                .<ExRequestBuilder>as()
                .async(User.class);
    }

    public static Async.Builder<?> logout() {
        return ExRequestBuilder.post("/user/logout").async();
    }

    public static Async.Builder<User> register(String username, String password) {
        return ExRequestBuilder.post("/user/api/register")
                .form()
                .field("username", username)
                .field("password", password)
                .field("gender", 1)
                .field("birthday", "20000101")
                .field("email", "")
                .<ExRequestBuilder>as()
                .async(User.class);
    }

    public static Async.Builder<User> getUser(int id) {
        return ExRequestBuilder.get("/user/details/{id}")
                .path("id", id)
                .<ExRequestBuilder>as()
                .async(User.class);
    }

    public static Async.Builder<?> setHead(File file) {
        return ExRequestBuilder.post("/user/head")
                .form()
                .field("file", file)
                .async();
    }

    public static Async.Builder<User> setUserInfo(String nickname, int gender, String birthday, String email, String des) {
        return ExRequestBuilder.put("/user/info")
                .form()
                .field("nickname", nickname)
                .field("gender", gender)
                .field("birthday", birthday)
                .field("email", email)
                .field("des", des)
                .<ExRequestBuilder>as()
                .async(User.class);
    }

    public static Async.Builder<?> setUserPassword(String source, String password) {
        return ExRequestBuilder.post("/user/api/password")
                .form()
                .field("source", source)
                .field("password", password)
                .async();
    }

    //=========================== 公告
    public static Async.Builder<PagerData<Notice>> getNoticeList(int n) {
        return ExRequestBuilder.get("/api/notice/list/{n}")
                .path("n", n)
                .<ExRequestBuilder>as()
                .asyncPager(Notice.class);
    }

    public static Async.Builder<NoticeDetails> getNoticeDetails(int id) {
        return ExRequestBuilder.get("/notice/details/{id}")
                .path("id", id)
                .<ExRequestBuilder>as()
                .async(NoticeDetails.class);
    }

    //=========================== 主帖
    public static Async.Builder<PagerData<MainPost>> getNewestMainPost(int page) {
        return ExRequestBuilder.get("/mainpost/newest/list/{n}")
                .path("n", page)
                .<ExRequestBuilder>as()
                .asyncPager(MainPost.class);
    }

    public static Async.Builder<PagerData<MainPost>> getSpaceMainPost(int page) {
        return ExRequestBuilder.get("/user/space/follow/mainpost/list/{n}")
                .path("n", page)
                .<ExRequestBuilder>as()
                .asyncPager(MainPost.class);
    }

    public static Async.Builder<PagerData<MainPost>> getNewestMainPostByUserId(int page, int userId) {
        return ExRequestBuilder.get("/mainpost/list/{n}")
                .path("n", page)
                .param("userId", userId)
                .<ExRequestBuilder>as()
                .asyncPager(MainPost.class);
    }

    public static Async.Builder<MainPost> getMainPost(int id) {
        return ExRequestBuilder.get("/mainpost/details/{id}")
                .path("id", id)
                .<ExRequestBuilder>as()
                .async(MainPost.class);
    }

    public static Async.Builder<?> deleteMainPost(int id) {
        return ExRequestBuilder.delete("/user/mainpost/{id}")
                .path("id", id)
                .async();
    }

    //========================== 主帖草稿
    public static Async.Builder<PostDraft> getDraft() {
        return ExRequestBuilder.get("/user/post/draft")
                .<ExRequestBuilder>as()
                .async(PostDraft.class);
    }

    public static Async.Builder<PostDraft> setDraft(String title, String content) {
        return ExRequestBuilder.post("/user/post/draft")
                .form()
                .field("title", title)
                .field("content", content)
                .<ExRequestBuilder>as()
                .async(PostDraft.class);
    }

    public static Async.Builder<MainPost> submitDraft() {
        return ExRequestBuilder.post("/user/post/draft/submit")
                .<ExRequestBuilder>as()
                .async(MainPost.class);
    }

    public static Async.Builder<String> addDraftImage(File file) {
        return ExRequestBuilder.post("/user/post/draft/image")
                .form()
                .field("file", file)
                .<ExRequestBuilder>as()
                .async(String.class);
    }

    public static Async.Builder<?> deleteDraftImage(String url) {
        return ExRequestBuilder.delete(url)
                .async();
    }

    public static Async.Builder<Images> getDraftImages() {
        return ExRequestBuilder.get("/user/post/draft/images")
                .async(Images.class);
    }

    //========================== 回复贴
    public static Async.Builder<PagerData<Post>> getReplyPost(int page, int mainPostId) {
        return ExRequestBuilder.get("/post/list/{n}")
                .path("n", page)
                .param("mainPostId", mainPostId)
                .<ExRequestBuilder>as()
                .asyncPager(Post.class);
    }

    public static Async.Builder<Post> getPost(int id) {
        return ExRequestBuilder.get("/post/details/{id}")
                .path("id", id)
                .<ExRequestBuilder>as()
                .async(Post.class);
    }

    public static Async.Builder<?> deletePost(int id) {
        return ExRequestBuilder.delete("/user/post/delete/{id}")
                .path("id", id)
                .async();
    }

    public static Async.Builder<Post> sendPost(int mainPostId, String content, List<File> images) {
        return ExRequestBuilder.post("/user/mainpost/reply")
                .form()
                .field("mainPostId", mainPostId)
                .field("content", content)
                .field("file", images)
                .<ExRequestBuilder>as()
                .async(Post.class);
    }

    //========================== 回复
    public static Async.Builder<PagerData<Reply>> getReply(int postId, int page) {
        return ExRequestBuilder.get("/reply/list/{n}")
                .path("n", page)
                .param("postId", postId)
                .<ExRequestBuilder>as()
                .asyncPager(Reply.class);
    }

    public static Async.Builder<Reply> sendPostReply(int postId, String content) {
        return ExRequestBuilder.post("/user/post/reply")
                .form()
                .field("postId", postId)
                .field("content", content)
                .<ExRequestBuilder>as()
                .async(Reply.class);
    }

    public static Async.Builder<Reply> sendReplyReply(int replyId, String content) {
        return ExRequestBuilder.post("/user/reply/reply")
                .form()
                .field("replyId", replyId)
                .field("content", content)
                .<ExRequestBuilder>as()
                .async(Reply.class);
    }

    public static Async.Builder<?> deleteReply(int replyId) {
        return ExRequestBuilder.delete("/user/reply/delete/{id}")
                .path("id", replyId)
                .async();
    }

    //========================== 点赞
    public static Async.Builder<LikeState> likeCheckMainPost(int targetId) {
        return ExRequestBuilder.get("/user/like/mainpost")
                .param("targetId", targetId)
                .<ExRequestBuilder>as()
                .async(LikeState.class);
    }

    public static Async.Builder<LikeState> likeToggleMainPost(int targetId) {
        return ExRequestBuilder.post("/user/like/mainpost")
                .form()
                .field("targetId", targetId)
                .<ExRequestBuilder>as()
                .async(LikeState.class);
    }

    public static Async.Builder<LikeState> likeCheckPost(int targetId) {
        return ExRequestBuilder.get("/user/like/post")
                .param("targetId", targetId)
                .<ExRequestBuilder>as()
                .async(LikeState.class);
    }

    public static Async.Builder<LikeState> likeTogglePost(int targetId) {
        return ExRequestBuilder.post("/user/like/post")
                .form()
                .field("targetId", targetId)
                .<ExRequestBuilder>as()
                .async(LikeState.class);
    }

    //=========================== 关注
    public static Async.Builder<PagerData<Follow>> getFollowList(int page, int userId) {
        return ExRequestBuilder.get("/follow/list/{n}")
                .path("n", page)
                .param("userId", userId)
                .<ExRequestBuilder>as()
                .asyncPager(Follow.class);
    }

    public static Async.Builder<PagerData<Follow>> getFollowerList(int page, int userId) {
        return ExRequestBuilder.get("/follower/list/{n}")
                .path("n", page)
                .param("userId", userId)
                .<ExRequestBuilder>as()
                .asyncPager(Follow.class);
    }

    public static Async.Builder<PagerData<Follow>> getFollowList(int page) {
        return ExRequestBuilder.get("/user/follow/list/{n}")
                .path("n", page)
                .<ExRequestBuilder>as()
                .asyncPager(Follow.class);
    }

    public static Async.Builder<FollowState> followCheck(int targetId) {
        return ExRequestBuilder.get("/user/follow/check")
                .param("targetId", targetId)
                .<ExRequestBuilder>as()
                .async(FollowState.class);
    }

    public static Async.Builder<FollowState> followToggle(int targetId) {
        return ExRequestBuilder.post("/user/follow/toggle")
                .form()
                .field("targetId", targetId)
                .<ExRequestBuilder>as()
                .async(FollowState.class);
    }

    //========================== 帖子、关注、粉丝的数量
    public static Async.Builder<SpaceValues> getSpaceValues(int userId) {
        return ExRequestBuilder.get("/space/value/numbers")
                .param("userId", userId)
                .<ExRequestBuilder>as()
                .async(SpaceValues.class);
    }

    //========================== 搜索
    public static Async.Builder<List<MainPost>> searchMainPost(String w) {
        return ExRequestBuilder.get("/query/post")
                .param("w", w)
                .<ExRequestBuilder>as()
                .asyncList(MainPost.class);
    }

    public static Async.Builder<List<User>> searchUser(String w) {
        return ExRequestBuilder.get("/query/user")
                .param("w", w)
                .<ExRequestBuilder>as()
                .asyncList(User.class);
    }

    //========================= 打卡
    public static Async.Builder<Da> submitDa(int reviewId, String des) {
        return ExRequestBuilder.post("/user/da/submit")
                .form()
                .param("reviewId", reviewId)
                .param("des", des)
                .<ExRequestBuilder>as()
                .async(Da.class);
    }

    public static Async.Builder<Da> reviewDa(int taskId, String des, boolean flag) {
        return ExRequestBuilder.post("/user/da/review")
                .form()
                .param("taskId", taskId)
                .param("des", des)
                .param("flag", flag)
                .<ExRequestBuilder>as()
                .async(Da.class);
    }

    public static Async.Builder<PagerData<Da>> getMyDaList(int page) {
        return ExRequestBuilder.get("/user/da/my/{n}")
                .path("n", page)
                .<ExRequestBuilder>as()
                .asyncPager(Da.class);
    }

    public static Async.Builder<PagerData<Da>> getReviewDaList(int page) {
        return ExRequestBuilder.get("/user/da/review/{n}")
                .path("n", page)
                .<ExRequestBuilder>as()
                .asyncPager(Da.class);
    }

    public static Async.Builder<Da> getDaDetails(int id) {
        return ExRequestBuilder.get("/da/task/details/{id}")
                .path("id", id)
                .<ExRequestBuilder>as()
                .async(Da.class);
    }
}
