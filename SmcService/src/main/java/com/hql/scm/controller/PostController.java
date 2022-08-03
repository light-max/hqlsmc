package com.hql.scm.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hql.scm.controller.user.post.MainPostSubmitController;
import com.hql.scm.controller.user.post.PostQueryController;
import com.hql.scm.mapper.MainPostMapper;
import com.hql.scm.mapper.UserMapper;
import com.hql.scm.model.data.Pager;
import com.hql.scm.model.data.Result;
import com.hql.scm.model.entity.MainPost;
import com.hql.scm.model.entity.User;
import com.hql.scm.model.result.MainPostResult;
import com.hql.scm.service.PostService;
import com.hql.scm.util.UseDefaultSuccessResponse;
import com.hql.scm.util.ump.ViewModelParameter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class PostController {
    @Resource
    PostService service;

    @Resource
    PostQueryController controller;

    @Resource
    MainPostMapper mapper;

    @Resource
    MainPostSubmitController mainPostSubmitController;

    @Resource
    UserMapper userMapper;

    @GetMapping({"/admin/post", "/admin/post/list", "/admin/post/list/{n}"})
    @ViewModelParameter(key = "view", value = "post")
    public String list(
            @PathVariable(required = false) Integer n,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) String sort,
            Model model
    ) {
        Page<MainPost> page = service.queryMainPost(n, title, userId, sort);
        Pager pager = new Pager(page, "/admin/post/list");
        if (title != null || userId != null || sort != null) {
            String t = "?";
            if (title != null) {
                t += "title=" + title + "&";
            }
            if (userId != null) {
                t += "userId=" + userId + "&";
            }
            if (sort != null) {
                t += "sort=" + sort + "&";
            }
            pager.setTailAppend(t.substring(0, t.length() - 1));
        }
        List<MainPostResult> list = controller.getMainList(null, page.getRecords());
        model.addAttribute("pager", pager);
        model.addAttribute("list", list);
        model.addAttribute("title", title);
        model.addAttribute("userId", userId);
        model.addAttribute("sort", sort);
        return "/admin/post/list";
    }

    @GetMapping("/admin/post/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        MainPost post = mapper.selectById(id);
        MainPostResult result = controller.getMain(null, post);
        model.addAttribute("post", result);
        return "/admin/post/edit";
    }

    @PostMapping("/admin/post/edit/{id}")
    @ResponseBody
    public Result<MainPostResult> edit(@PathVariable Integer id, String title, String content) {
        MainPost post = mapper.selectById(id);
        post.setTitle(title);
        post.setContent(content);
        post.check();
        mapper.updateById(post);
        return Result.success(controller.getMain(null, post));
    }

    @DeleteMapping("/admin/post/delete/{id}")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void delete(@PathVariable Integer id) {
        MainPost post = mapper.selectById(id);
        User user = userMapper.selectById(post.getUserId());
        mainPostSubmitController.delete(user, post.getId());
    }
}
