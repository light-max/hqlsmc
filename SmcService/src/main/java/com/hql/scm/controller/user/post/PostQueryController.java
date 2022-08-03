package com.hql.scm.controller.user.post;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hql.scm.controller.ResourceController;
import com.hql.scm.mapper.MainPostMapper;
import com.hql.scm.mapper.PostMapper;
import com.hql.scm.mapper.ReplyMapper;
import com.hql.scm.model.data.Pager;
import com.hql.scm.model.data.PagerData;
import com.hql.scm.model.data.Result;
import com.hql.scm.model.entity.MainPost;
import com.hql.scm.model.entity.Post;
import com.hql.scm.model.entity.Reply;
import com.hql.scm.model.entity.User;
import com.hql.scm.model.result.Images;
import com.hql.scm.model.result.MainPostResult;
import com.hql.scm.model.result.PostResult;
import com.hql.scm.model.result.ReplyResult;
import com.hql.scm.service.LikeService;
import com.hql.scm.service.PostService;
import com.hql.scm.service.UserService;
import com.hql.scm.util.datetranslate.DateFormatUtil;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class PostQueryController {
    @Resource
    PostService service;

    @Resource
    ResourceController resourceController;

    @Resource
    UserService userService;

    @Resource
    LikeService likeService;

    @Resource
    MainPostMapper mainPostMapper;

    @Resource
    PostMapper postMapper;

    @Resource
    ReplyMapper replyMapper;

    @GetMapping("/mainpost/newest/list/{n}")
    @ResponseBody
    public Result<PagerData> getMainpostNewestList(HttpSession session, @PathVariable Integer n) {
        Page<MainPost> page = service.getMainPostNewestList(n, 8);
        Pager pager = new Pager(page, "/mainpost/newest/list");
        User user = (User) session.getAttribute("user");
        List<MainPostResult> list = getMainList(user, page.getRecords());
        return Result.pager(pager, list);
    }

    @GetMapping({"/mainpost/list/{n}"})
    @ResponseBody
    public Result<PagerData> getMainpostList(HttpSession session, @PathVariable Integer n, Integer userId) {
        Page<MainPost> page = service.getMainPostList(n, 8, userId);
        Pager pager = new Pager(page, "/mainpost/list");
        pager.setTailAppend("?userId=" + userId);
        User user = (User) session.getAttribute("user");
        List<MainPostResult> list = getMainList(user, page.getRecords());
        return Result.pager(pager, list);
    }

    @GetMapping("/post/list/{n}")
    @ResponseBody
    public Result<PagerData> getPostList(HttpSession session, @PathVariable Integer n, Integer mainPostId) {
        Page<Post> page = service.getPostListByMainPostId(n, 8, mainPostId);
        Pager pager = new Pager(page, "/post/list");
        pager.setTailAppend("?mainPostId=" + mainPostId);
        User user = (User) session.getAttribute("user");
        List<PostResult> list = getList(user, page.getRecords());
        return Result.pager(pager, list);
    }

    @GetMapping("/reply/list/{n}")
    @ResponseBody
    public Result<PagerData> getReplyList(HttpSession session, @PathVariable Integer n, Integer postId) {
        Page<Reply> page = service.getReplyListByPostId(n, 8, postId);
        Pager pager = new Pager(page, "/reply/list");
        pager.setTailAppend("?postId=" + postId);
        User user = (User) session.getAttribute("user");
        List<ReplyResult> list = getReplyList(user, page.getRecords());
        return Result.pager(pager, list);
    }

    @GetMapping("/user/space/follow/mainpost/list/{n}")
    @ResponseBody
    public Result<PagerData> getFollowMainpost(@PathVariable Integer n, @SessionAttribute("user") User user) {
        Page<MainPost> page = service.getMainPostByFollowerId(n, 8, user.getId());
        Pager pager = new Pager(page, "/user/sapce/follow/mainpost/list");
        List<MainPostResult> list = getMainList(user, page.getRecords());
        return Result.pager(pager, list);
    }

    @GetMapping("/mainpost/details/{id}")
    @ResponseBody
    public Result<MainPostResult> getMainDetails(HttpSession session, @PathVariable Integer id) {
        MainPost mainPost = mainPostMapper.selectById(id);
        User user = (User) session.getAttribute("user");
        Integer currentUserId = user != null ? user.getId() : null;
        return Result.success(getMain(currentUserId, mainPost));
    }

    @GetMapping("/post/details/{id}")
    @ResponseBody
    public Result<PostResult> getDetails(HttpSession session, @PathVariable Integer id) {
        Post post = postMapper.selectById(id);
        User user = (User) session.getAttribute("user");
        Integer currentUserId = user != null ? user.getId() : null;
        return Result.success(get(currentUserId, post));
    }

    @GetMapping("/reply/details/{id}")
    @ResponseBody
    public Result<ReplyResult> getReplyDetails(HttpSession session, @PathVariable Integer id) {
        Reply reply = replyMapper.selectById(id);
        User user = (User) session.getAttribute("user");
        Integer currentUserId = user != null ? user.getId() : null;
        return Result.success(getReply(currentUserId, reply));
    }

    @GetMapping("/query/post")
    @ResponseBody
    public Result<List<MainPostResult>> queryMainPost(String w) {
        List<MainPost> posts = service.queryMainPost(w);
        List<MainPostResult> results = getMainList(null, posts);
        return Result.success(results);
    }

    /**
     * @param user 当前登录的用户
     */
    public List<MainPostResult> getMainList(@Nullable User user, List<MainPost> posts) {
        List<MainPostResult> results = new ArrayList<>();
        for (MainPost post : posts) {
            Integer currentUserId = user != null ? user.getId() : null;
            results.add(getMain(currentUserId, post));
        }
        return results;
    }

    public MainPostResult getMain(Integer currentUserId, MainPost post) {
        User user = userService.getById(post.getUserId());
        user = user == null ? new User() : user;
        Result<Images> images = resourceController.getImages("mainpost", post.getId());
        boolean isLike = currentUserId != null && likeService.isLike(0, post.getId(), currentUserId);
        return MainPostResult.builder()
                .id(post.getId())
                .userId(post.getUserId())
                .title(post.getTitle())
                .content(post.getContent())
                .createTime(post.getCreateTime())
                .time(DateFormatUtil.format(post.getCreateTime(), "yyyy/MM/dd HH:mm:ss"))
                .username(user.getUsername())
                .nickname(user.getNickname())
                .likeCount(likeService.getMainPostCount(post.getId()))
                .replyCount(service.getPostCount(post.getId()))
                .images(images.getData())
                .like(isLike)
                .build();
    }

    private List<PostResult> getList(@Nullable User user, List<Post> posts) {
        List<PostResult> results = new ArrayList<>();
        for (Post post : posts) {
            Integer currentUserId = user != null ? user.getId() : null;
            results.add(get(currentUserId, post));
        }
        return results;
    }

    private PostResult get(Integer currentUserId, Post post) {
        User user = userService.getById(post.getUserId());
        user = user == null ? new User() : user;
        Result<Images> images = resourceController.getImages("post", post.getId());
        boolean isLike = currentUserId != null && likeService.isLike(1, post.getId(), currentUserId);
        boolean isMe = currentUserId != null && currentUserId.equals(post.getUserId());
        return PostResult.builder()
                .id(post.getId())
                .userId(post.getUserId())
                .mainId(post.getMainId())
                .content(post.getContent())
                .createTime(post.getCreateTime())
                .time(DateFormatUtil.format(post.getCreateTime(), "yyyy/MM/dd HH:mm:ss"))
                .username(user.getUsername())
                .nickname(user.getNickname())
                .likeCount(likeService.getPostCount(post.getId()))
                .replyCount(service.getReplyCount(post.getId()))
                .images(images.getData())
                .like(isLike)
                .me(isMe)
                .build();
    }

    private List<ReplyResult> getReplyList(@Nullable User user, List<Reply> replies) {
        List<ReplyResult> results = new ArrayList<>();
        for (Reply reply : replies) {
            Integer currentUserId = user != null ? user.getId() : null;
            results.add(getReply(currentUserId, reply));
        }
        return results;
    }

    private ReplyResult getReply(Integer currentUserId, Reply reply) {
        User user = userService.getById(reply.getUserId());
        user = user == null ? new User() : user;
        User targetUser = userService.getById(reply.getTargetId() == null ? -1 : reply.getTargetId());
        targetUser = targetUser == null ? new User() : targetUser;
        Post post = postMapper.selectById(reply.getPostId());
        boolean isMe = currentUserId != null && currentUserId.equals(post.getUserId());
        return ReplyResult.builder()
                .id(reply.getId())
                .userId(reply.getUserId())
                .postId(reply.getPostId())
                .targetId(reply.getTargetId())
                .content(reply.getContent())
                .createTime(reply.getCreateTime())
                .mainPostId(post.getMainId())
                .time(DateFormatUtil.format(reply.getCreateTime(), "yyyy/MM/dd HH:mm:ss"))
                .username(user.getUsername())
                .nickname(user.getNickname())
                .targetUsername(targetUser.getUsername())
                .targetNickname(targetUser.getNickname())
                .me(isMe)
                .build();
    }

}
