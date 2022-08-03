package com.hql.scm.controller.user;

import com.hql.scm.constant.GlobalConstant;
import com.hql.scm.model.data.Result;
import com.hql.scm.model.entity.User;
import com.hql.scm.service.UserService;
import com.hql.scm.util.UseDefaultSuccessResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.annotation.Resource;

@Controller
public class RegisterController {
    @Resource
    UserService service;

    @PostMapping("/user/api/register")
    @ResponseBody
    public Result<User> register(
            String username, String password,
            Integer gender, String birthday, String email
    ) {
        User data = service.addNew(User.builder()
                .username(username)
                .password(password)
                .gender(gender)
                .birthday(birthday)
                .email(email)
                .build());
        return Result.success(data);
    }

    @PostMapping("/user/api/password")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void setPwd(
            @SessionAttribute("user") User user,
            String source, String password
    ) {
        GlobalConstant.sourcePasswordError.isTrue(user.getPassword().equals(source));
        User.builder().password(password)
                .build()
                .check();
        user.setPassword(password);
        service.updateById(user);
    }
}
