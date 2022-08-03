package com.hql.scm.controller;

import com.hql.scm.constant.GlobalConstant;
import com.hql.scm.mapper.AdminMapper;
import com.hql.scm.model.entity.Admin;
import com.hql.scm.util.ump.ViewModelParameter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.annotation.Resource;

@Controller
public class PasswordController {
    @Resource
    AdminMapper mapper;

    @GetMapping("/admin/password")
    @ViewModelParameter(key = "view", value = "password")
    public String getView(Model model) {
        return "/admin/password";
    }

    @PostMapping("/admin/password")
    @ViewModelParameter(key = "view", value = "password")
    public String setPwd(
            @SessionAttribute("admin") Admin admin,
            String source, String password,
            Model model
    ) {
        try {
            GlobalConstant.sourcePasswordError.isTrue(admin.getPassword().equals(source));
            Admin.builder().password(password)
                    .build()
                    .check();
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "/admin/password";
        }
        admin.setPassword(password);
        mapper.updateById(admin);
        return "redirect:/admin/logout";
    }
}
