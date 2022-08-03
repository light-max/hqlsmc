package com.hql.scm.controller.user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hql.scm.model.data.Pager;
import com.hql.scm.model.data.PagerData;
import com.hql.scm.model.data.Result;
import com.hql.scm.model.entity.Follow;
import com.hql.scm.model.entity.User;
import com.hql.scm.model.result.FollowResult;
import com.hql.scm.model.result.FollowState;
import com.hql.scm.service.FollowService;
import com.hql.scm.service.UserService;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class FollowController {
    @Resource
    FollowService service;

    @Resource
    UserService userService;

    @GetMapping("/user/follow/check")
    @ResponseBody
    public Result<FollowState> check(
            @SessionAttribute("user") User user,
            Integer targetId
    ) {
        return Result.success(service.check(user.getId(), targetId));
    }

    @PostMapping("/user/follow/toggle")
    @ResponseBody
    public Result<FollowState> toggle(
            @SessionAttribute("user") User user,
            Integer targetId
    ) {
        return Result.success(service.toggle(user.getId(), targetId));
    }

    @GetMapping("/user/follow/list/{n}")
    @ResponseBody
    public Result<PagerData> followList(HttpSession session, @PathVariable Integer n) {
        User user = (User) session.getAttribute("user");
        return followList(session, n, user.getId());
    }

    @GetMapping({"/follow/list", "/follow/list/{n}"})
    @ResponseBody
    public Result<PagerData> followList(
            HttpSession session,
            @PathVariable(required = false) Integer n,
            Integer userId
    ) {
        Page<Follow> page = service.followList(n, 10, userId);
        Pager pager = new Pager(page, "/follow/list");
        pager.setTailAppend("?userId=" + userId);
        User currentUser = (User) session.getAttribute("user");
        List<FollowResult> list = getFollowResult(currentUser, page.getRecords());
        return Result.pager(pager, list);
    }

    @GetMapping({"/follower/list", "/follower/list/{n}"})
    @ResponseBody
    public Result<PagerData> followerList(
            HttpSession session,
            @PathVariable(required = false) Integer n,
            Integer userId
    ) {
        Page<Follow> page = service.followerList(n, 10, userId);
        Pager pager = new Pager(page, "/follower/list");
        pager.setTailAppend("?userId=" + userId);
        User currentUser = (User) session.getAttribute("user");
        List<FollowResult> list = getFollowerResult(currentUser, page.getRecords());
        return Result.pager(pager, list);
    }

    private List<FollowResult> getFollowResult(@Nullable User currentUser, List<Follow> follows) {
        List<FollowResult> results = new ArrayList<>();
        for (Follow follow : follows) {
            Integer currentUserId = currentUser != null ? currentUser.getId() : null;
            results.add(getFollowResult(currentUserId, follow));
        }
        return results;
    }

    private FollowResult getFollowResult(@Nullable Integer currentUserId, Follow follow) {
        User targetUser = userService.getById(follow.getTargetId());
        FollowState state = currentUserId == null ? null : service.check(currentUserId, follow.getTargetId());
        return FollowResult.builder()
                .targetId(follow.getTargetId())
                .nickname(targetUser.getNickname())
                .username(targetUser.getUsername())
                .des(targetUser.getDes())
                .state(state)
                .build();
    }

    private List<FollowResult> getFollowerResult(@Nullable User currentUser, List<Follow> follows) {
        List<FollowResult> results = new ArrayList<>();
        for (Follow follow : follows) {
            Integer currentUserId = currentUser != null ? currentUser.getId() : null;
            results.add(getFollowerResult(currentUserId, follow));
        }
        return results;
    }

    private FollowResult getFollowerResult(@Nullable Integer currentUserId, Follow follow) {
        User targetUser = userService.getById(follow.getUserId());
        FollowState state = currentUserId == null ? null : service.check(currentUserId, follow.getUserId());
        return FollowResult.builder()
                .targetId(follow.getUserId())
                .nickname(targetUser.getNickname())
                .username(targetUser.getUsername())
                .des(targetUser.getDes())
                .state(state)
                .build();
    }

}
