package com.hql.scm.controller.user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hql.scm.mapper.UserMapper;
import com.hql.scm.model.data.Pager;
import com.hql.scm.model.data.PagerData;
import com.hql.scm.model.data.Result;
import com.hql.scm.model.entity.Task;
import com.hql.scm.model.entity.User;
import com.hql.scm.model.result.TaskResult;
import com.hql.scm.service.DailyAttendanceService;
import com.hql.scm.util.datetranslate.DateFormatUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Controller
public class DailyAttendanceController {
    @Resource
    DailyAttendanceService service;

    @Resource
    UserMapper userMapper;

    @PostMapping("/user/da/submit")
    @ResponseBody
    public Result<TaskResult> submit(
            @SessionAttribute("user") User user,
            Integer reviewId, String des
    ) {
        Task task = service.submit(user.getId(), reviewId, des);
        return Result.success(get(task));
    }

    @PostMapping("/user/da/review")
    @ResponseBody
    public Result<TaskResult> review(
            @SessionAttribute("user") User user,
            Integer taskId, String des, Boolean flag
    ) {
        Task task = service.review(user.getId(), taskId, des, flag);
        return Result.success(get(task));
    }

    @GetMapping("/user/da/my/{n}")
    @ResponseBody
    public Result<PagerData> getMyList(
            @SessionAttribute("user") User user,
            @PathVariable Integer n,
            @RequestParam(required = false) Integer state
    ) {
        state = state == null ? -1 : state;
        Page<Task> page = service.getListByUserId(n, 8, user.getId(), state);
        Pager pager = new Pager(page, "/user/da/my");
        List<TaskResult> list = getList(page.getRecords());
        return Result.pager(pager, list);
    }

    @GetMapping("/user/da/review/{n}")
    @ResponseBody
    public Result<PagerData> getReviewList(
            @SessionAttribute("user") User user,
            @PathVariable Integer n,
            @RequestParam(required = false) Integer state
    ) {
        state = state == null ? -1 : state;
        Page<Task> page = service.getListByReviewId(n, 8, user.getId(), state);
        Pager pager = new Pager(page, "/user/da/my");
        List<TaskResult> list = getList(page.getRecords());
        return Result.pager(pager, list);
    }

    @GetMapping("/user/da/task/view/{id}")
    public String getTaskDetailsView(@PathVariable Integer id, Model model) {
        model.addAttribute("task", get(service.getById(id)));
        model.addAttribute("date_format", new DateFormatUtil());
        return "/user/da/task_view";
    }

    @GetMapping("/user/da/review/view/{id}")
    public String getReviewDetailsView(@PathVariable Integer id, Model model) {
        model.addAttribute("task", get(service.getById(id)));
        model.addAttribute("date_format", new DateFormatUtil());
        return "/user/da/review_view";
    }

    @GetMapping({"/user/da/view/task/list", "/user/da/view/task/list/{n}"})
    public String getTaskListView(
            @SessionAttribute("user") User user,
            @PathVariable(required = false) Integer n,
            Model model
    ) {
        Page<Task> page = service.getListByUserId(n == null ? 1 : n, 8, user.getId(), -1);
        Pager pager = new Pager(page, "/user/da/view/task/list");
        List<TaskResult> list = getList(page.getRecords());
        model.addAttribute("pager", pager);
        model.addAttribute("list", list);
        model.addAttribute("date_format", new DateFormatUtil());
        return "/user/da/task_list";
    }

    @GetMapping({"/user/da/view/review/list", "/user/da/view/review/list/{n}"})
    public String getReviewListView(
            @SessionAttribute("user") User user,
            @PathVariable(required = false) Integer n,
            Model model
    ) {
        Page<Task> page = service.getListByReviewId(n == null ? 1 : n, 8, user.getId(), -1);
        Pager pager = new Pager(page, "/user/da/view/review/list");
        List<TaskResult> list = getList(page.getRecords());
        model.addAttribute("pager", pager);
        model.addAttribute("list", list);
        model.addAttribute("date_format", new DateFormatUtil());
        return "/user/da/review_list";
    }

    public List<TaskResult> getList(List<Task> tasks) {
        List<TaskResult> results = new ArrayList<>();
        for (Task task : tasks) {
            results.add(get(task));
        }
        return results;
    }

    public TaskResult get(Task task) {
        User reviewUser = userMapper.selectById(task.getReviewId());
        User user = userMapper.selectById(task.getUserId());
        return TaskResult.builder()
                .id(task.getId())
                .userId(task.getUserId())
                .reviewId(task.getReviewId())
                .des(task.getDes())
                .createTime(task.getCreateTime())
                .createDate(task.getCreateDate())
                .reviewTime(task.getReviewTime())
                .state(task.getState())
                .reviewMsg(task.getReviewMsg())
                .reviewDate(task.getReviewTime() != 0 ? DateFormatUtil.format(task.getReviewTime(), "yyyy/MM/dd HH:mm:ss") : "")
                .reviewNickname(reviewUser.getNickname())
                .reviewUsername(reviewUser.getUsername())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .build();
    }
}
