package com.hql.scm.controller.user;

import com.hql.scm.model.data.Result;
import com.hql.scm.model.entity.User;
import com.hql.scm.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
public class UserInfoController {
    @Resource
    UserService service;

    @GetMapping("/user/info")
    public String info(Model model, @SessionAttribute("user") User user) {
        model.addAttribute("user", user);
        return "/user/info";
    }

    @PutMapping("/user/info")
    @ResponseBody
    public Result<User> setInfo(
            HttpSession session,
            @RequestParam(required = false) String nickname,
            @RequestParam(required = false) Integer gender,
            @RequestParam(required = false) String birthday,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String des
    ) {
        User user = service.update(User.builder()
                .id(((User) session.getAttribute("user")).getId())
                .nickname(nickname)
                .gender(gender)
                .birthday(birthday)
                .email(email)
                .des(des)
                .build());
        session.setAttribute("user", user);
        return Result.success(user);
    }
}
