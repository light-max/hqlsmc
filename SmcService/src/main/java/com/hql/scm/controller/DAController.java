package com.hql.scm.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hql.scm.controller.user.DailyAttendanceController;
import com.hql.scm.mapper.UserMapper;
import com.hql.scm.model.data.Pager;
import com.hql.scm.model.entity.Task;
import com.hql.scm.model.entity.User;
import com.hql.scm.service.DailyAttendanceService;
import com.hql.scm.util.UseDefaultSuccessResponse;
import com.hql.scm.util.datetranslate.DateFormatUtil;
import com.hql.scm.util.ump.ViewModelParameter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Controller
public class DAController {
    @Resource
    DailyAttendanceService service;

    @Resource
    DailyAttendanceController controller;

    @Resource
    UserMapper userMapper;

    @GetMapping({"/admin/da", "/admin/da/list", "/admin/da/list/{n}"})
    @ViewModelParameter(key = "view", value = "da")
    public String list(
            @PathVariable(required = false) Integer n,
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) Integer reviewId,
            @RequestParam(required = false) String sort,
            Model model
    ) {
        Page<Task> page = service.queryList(n, userId, reviewId, sort);
        Pager pager = new Pager(page, "/admin/da/list");
        if (userId != null || reviewId != null || sort != null) {
            String t = "?";
            if (userId != null) {
                t += "userId=" + userId + "&";
            }
            if (reviewId != null) {
                t += "reviewId=" + reviewId + "&";
            }
            if (sort != null) {
                t += "sort=" + sort + "&";
            }
            pager.setTailAppend(t.substring(0, t.length() - 1));
        }
        if (userId != null) {
            User user = userMapper.selectById(userId);
            if (user != null) {
                model.addAttribute("username", user.getNickname());
            }
        }
        model.addAttribute("userId", userId);
        model.addAttribute("reviewId", reviewId);
        model.addAttribute("sort", sort);
        model.addAttribute("pager", pager);
        model.addAttribute("list", controller.getList(page.getRecords()));
        model.addAttribute("date_format", new DateFormatUtil());
        return "/admin/da/list";
    }

    @DeleteMapping("/admin/da/delete/{id}")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void delete(@PathVariable Integer id) {
        service.removeById(id);
    }
}
