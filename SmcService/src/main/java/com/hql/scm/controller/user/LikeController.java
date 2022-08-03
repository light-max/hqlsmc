package com.hql.scm.controller.user;

import com.hql.scm.model.data.Result;
import com.hql.scm.model.entity.User;
import com.hql.scm.model.result.LikeState;
import com.hql.scm.service.LikeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.annotation.Resource;

@Controller
public class LikeController {
    @Resource
    LikeService service;

    @GetMapping("/user/like/mainpost")
    @ResponseBody
    public Result<LikeState> check(
            @SessionAttribute("user")User user,
            Integer targetId
    ){
        boolean checked = service.isLike(0, targetId, user.getId());
        int mainPostCount = service.getMainPostCount(targetId);
        LikeState state = LikeState.builder()
                .type(0)
                .userId(user.getId())
                .targetId(targetId)
                .like(checked)
                .count(mainPostCount)
                .build();
        return Result.success(state);
    }

    @PostMapping("/user/like/mainpost")
    @ResponseBody
    public Result<LikeState> toggle(
            @SessionAttribute("user")User user,
            Integer targetId
    ){
        boolean checked = service.toggle(0, targetId, user.getId());
        int mainPostCount = service.getMainPostCount(targetId);
        LikeState state = LikeState.builder()
                .type(0)
                .userId(user.getId())
                .targetId(targetId)
                .like(checked)
                .count(mainPostCount)
                .build();
        return Result.success(state);
    }

    @GetMapping("/user/like/post")
    @ResponseBody
    public Result<LikeState> check1(
            @SessionAttribute("user")User user,
            Integer targetId
    ){
        boolean checked = service.isLike(1, targetId, user.getId());
        int postCount = service.getPostCount(targetId);
        LikeState state = LikeState.builder()
                .type(0)
                .userId(user.getId())
                .targetId(targetId)
                .like(checked)
                .count(postCount)
                .build();
        return Result.success(state);
    }

    @PostMapping("/user/like/post")
    @ResponseBody
    public Result<LikeState> toggle1(
            @SessionAttribute("user")User user,
            Integer targetId
    ){
        boolean checked = service.toggle(1, targetId, user.getId());
        int postCount = service.getPostCount(targetId);
        LikeState state = LikeState.builder()
                .type(0)
                .userId(user.getId())
                .targetId(targetId)
                .like(checked)
                .count(postCount)
                .build();
        return Result.success(state);
    }
}
