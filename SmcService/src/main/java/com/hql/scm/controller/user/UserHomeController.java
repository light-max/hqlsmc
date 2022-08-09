package com.hql.scm.controller.user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hql.scm.model.data.PagerData;
import com.hql.scm.model.data.Result;
import com.hql.scm.model.entity.Notice;
import com.hql.scm.model.entity.User;
import com.hql.scm.model.result.SpaceValueNumbers;
import com.hql.scm.model.td.HomeNoticeTD;
import com.hql.scm.service.FollowService;
import com.hql.scm.service.NoticeService;
import com.hql.scm.service.PostService;
import com.hql.scm.service.UserService;
import com.hql.scm.util.datetranslate.DateFormatUtil;
import com.hql.scm.util.ump.ViewModelParameter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
public class UserHomeController {
    @Resource
    UserService userService;

    @Resource
    NoticeService noticeService;

    @Resource
    PostService postService;

    @Resource
    FollowService followService;

    @Resource
    DailyAttendanceController attendanceController;

    @GetMapping({"/user/home", "/user"})
    @ViewModelParameter(key = "route", value = "home")
    public String home(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        Page<Notice> topNotice = noticeService.list(null, 10, "top");
        Page<Notice> notopNotice = noticeService.list(null, 10, "notop");
        model.addAttribute("user", user);
        model.addAttribute("top_notices", HomeNoticeTD.list(topNotice.getRecords()));
        model.addAttribute("notop_notices", HomeNoticeTD.list(notopNotice.getRecords()));
        getPostFollowValue(model, user);
        return "/user/home";
    }

    @GetMapping("/user/space/posts-followed")
    @ViewModelParameter(key = "route", value = "space")
    public String space(@SessionAttribute("user") User user, Model model) {
        PagerData data1 = attendanceController.getMyList(user, 1, null).getData();
        PagerData data2 = attendanceController.getReviewList(user, 1, 0).getData();
        model.addAttribute("user", user);
        model.addAttribute("my_task_list", data1.getData());
        model.addAttribute("review_task_list", data2.getData());
        model.addAttribute("date_format", new DateFormatUtil());
        getPostFollowValue(model, user);
        return "/user/space";
    }

    @GetMapping({"/space/follow/{id}", "/user/space/follow"})
    @ViewModelParameter(key = "view", value = "follow")
    public String follow(
            Model model, HttpSession session,
            @PathVariable(required = false) Integer id
    ) {
        getUInfoData(model, session, id);
        return "/public/space/follow";
    }

    @GetMapping({"/space/follower/{id}", "/user/space/follower"})
    @ViewModelParameter(key = "view", value = "follower")
    public String follower(
            Model model, HttpSession session,
            @PathVariable(required = false) Integer id
    ) {
        getUInfoData(model, session, id);
        return "/public/space/follower";
    }

    @GetMapping({"/space/posts/{id}", "/user/space/posts"})
    @ViewModelParameter(key = "view", value = "posts")
    public String post(
            Model model, HttpSession session,
            @PathVariable(required = false) Integer id
    ) {
        getUInfoData(model, session, id);
        return "/public/space/posts";
    }

    @GetMapping("/space/value/numbers")
    @ResponseBody
    public Result<SpaceValueNumbers> getValueNumbers(Integer userId){
        int postCount = postService.getMainPostCount(userId);
        int followCount = followService.getFollowCount(userId);
        int followerCount = followService.getFollowerCount(userId);
        return Result.success(SpaceValueNumbers.builder()
                .postCount(postCount)
                .followCount(followCount)
                .followerCount(followerCount)
                .build());
    }

    private void getUInfoData(Model model, HttpSession session, @Nullable Integer id) {
        boolean isMe;
        User user;
        if (id != null) {
            user = userService.getById(id);
            model.addAttribute("posts_href", "/space/posts/" + id);
            model.addAttribute("follow_href", "/space/follow/" + id);
            model.addAttribute("follower_href", "/space/follower/" + id);
            User t = (User) session.getAttribute("user");
            if (t != null && t.getId().equals(id)) {
                isMe = true;
            } else {
                isMe = false;
            }
        } else {
            user = (User) session.getAttribute("user");
            model.addAttribute("posts_href", "/user/space/posts");
            model.addAttribute("follow_href", "/user/space/follow");
            model.addAttribute("follower_href", "/user/space/follower");
            if (user != null) {
                isMe = true;
            } else {
                isMe = false;
            }
        }
        if (user != null) {
            model.addAttribute("user", user);
        }
        getPostFollowValue(model, user);
        model.addAttribute("isMe", isMe);
    }

    private void getPostFollowValue(Model model, User user) {
        if (user != null) {
            int postCount = postService.getMainPostCount(user.getId());
            int followCount = followService.getFollowCount(user.getId());
            int followerCount = followService.getFollowerCount(user.getId());
            model.addAttribute("post_count", postCount);
            model.addAttribute("follow_count", followCount);
            model.addAttribute("follower_count", followerCount);
        }
    }
}
