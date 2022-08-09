package com.hql.scm.controller.user;

import com.hql.scm.controller.UserController;
import com.hql.scm.controller.user.post.PostQueryController;
import com.hql.scm.model.entity.User;
import com.hql.scm.model.result.MainPostResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class SearchController {
    @Resource
    UserController userController;

    @Resource
    PostQueryController postQueryController;

    @GetMapping("/search")
    public String search(
            @RequestParam(required = false) String w,
            @RequestParam(required = false, defaultValue = "post") String type,
            Model model
    ) {
        if ("post".equals(type)) {
            List<MainPostResult> list = postQueryController.queryMainPost(w).getData();
            model.addAttribute("posts", list);
        }
        if ("user".equals(type)) {
            List<User> list = userController.query(w).getData();
            model.addAttribute("users", list);
        }
        model.addAttribute("w", w);
        model.addAttribute("type", type);
        return "/user/search";
    }
}
