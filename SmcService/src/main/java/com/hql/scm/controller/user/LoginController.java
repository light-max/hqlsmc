package com.hql.scm.controller.user;

import com.hql.scm.constant.GlobalConstant;
import com.hql.scm.model.data.Result;
import com.hql.scm.model.entity.User;
import com.hql.scm.service.LoginService;
import com.hql.scm.util.UseDefaultSuccessResponse;
import com.hql.scm.util.ump.ViewModelParameter;
import com.hql.scm.util.ump.ViewModelParameters;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller("user.login")
public class LoginController {
    @Resource
    LoginService service;

    @PostMapping("/user/api/login")
    @ResponseBody
    public Result<User> login(HttpSession session, String username, String password) {
        Result<User> result = service.user(username, password);
        if (result.isSuccess()) {
            session.setAttribute("user", result.getData());
        }
        return result;
    }

    @GetMapping(value = "/user/notloggedin", produces = "text/html")
    @ViewModelParameters({
            @ViewModelParameter(key = "username", value = "user0"),
            @ViewModelParameter(key = "password", value = "1234")
    })
    public String notLogin(Model model) {
        model.addAttribute("error", GlobalConstant.noAccess.getMessage());
        return "/user/login";
    }

    @GetMapping("/user/notloggedin")
    @ResponseBody
    public Result<Object> notLogin() {
        return Result.error(GlobalConstant.noAccess.getMessage());
    }

    @PostMapping("/user/logout")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void logout(HttpSession session) {
        session.removeAttribute("user");
    }
}
