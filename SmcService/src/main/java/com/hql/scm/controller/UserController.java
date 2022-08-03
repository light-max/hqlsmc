package com.hql.scm.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hql.scm.constant.GlobalConstant;
import com.hql.scm.model.data.Pager;
import com.hql.scm.model.data.PagerData;
import com.hql.scm.model.data.Result;
import com.hql.scm.model.entity.User;
import com.hql.scm.service.UserService;
import com.hql.scm.util.ump.ViewModelParameter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class UserController {
    @Resource
    UserService service;

    @GetMapping({"/admin/user", "/admin/user/list", "/admin/user/list/{n}"})
    @ViewModelParameter(key = "view", value = "user")
    public String list(
            @PathVariable(required = false) Integer n,
            @RequestParam(required = false) String w,
            @RequestParam(required = false) Boolean username,
            @RequestParam(required = false) Boolean nickname,
            Model model
    ) {
        String tail = "";
        if (w != null && username != null && nickname != null) {
            tail = "?w=" + w + "&username=" + username + "&nickname=" + nickname;
        } else {
            username = nickname = false;
        }
        Page<User> page = service.list(n, w, username, nickname);
        Pager pager = new Pager(page, "/admin/user/list");
        pager.setTailAppend(tail);
        List<User> list = page.getRecords();
        model.addAttribute("list", list);
        model.addAttribute("pager", pager);
        model.addAttribute("w", w);
        model.addAttribute("username", username);
        model.addAttribute("nickname", nickname);
        model.addAttribute("genderArray", new String[]{"", "男", "女"});
        return "/admin/user/list";
    }

    @GetMapping("/admin/user/get/{id}")
    @ResponseBody
    public Result<User> getUser(@PathVariable Integer id) {
        User user = service.getById(id);
        GlobalConstant.dataNotExists.notNull(user);
        return Result.success(user);
    }

    @PostMapping("/admin/user/update")
    @ResponseBody
    public Result<User> updateById(
            Integer id, String username, String nickname, Integer gender,
            String birthday, String email, Boolean enable, String des
    ) {
        User user = service.update(User.builder()
                .id(id)
                .username(username)
                .nickname(nickname)
                .gender(gender)
                .birthday(birthday)
                .email(email)
                .enable(enable)
                .des(des)
                .build());
        return Result.success(service.update(user));
    }

    @GetMapping("/admin/api/user/list/{n}")
    @ResponseBody
    public Result<PagerData> list(@PathVariable Integer n, String w) {
        LambdaQueryWrapper<User> wrapper = new QueryWrapper<User>()
                .lambda();
        if (w != null && w.trim().length() > 0) {
            wrapper.like(User::getUsername, w)
                    .or()
                    .like(User::getNickname, w);
        }
        Page<User> page = service.page(new Page<>(n, 8), wrapper);
        Pager pager = new Pager(page, "/user/list");
        if (w != null && w.trim().length() > 0) {
            pager.setTailAppend("?w=" + w);
        }
        return Result.pager(pager, page.getRecords());
    }

    @GetMapping("/query/user")
    @ResponseBody
    public Result<List<User>> query(String w) {
        return Result.success(service.query(w));
    }
}
