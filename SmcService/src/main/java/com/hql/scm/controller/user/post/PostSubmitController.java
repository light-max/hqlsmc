package com.hql.scm.controller.user.post;

import com.hql.scm.model.data.Result;
import com.hql.scm.model.entity.Post;
import com.hql.scm.model.entity.Reply;
import com.hql.scm.model.entity.User;
import com.hql.scm.model.result.MainPostResult;
import com.hql.scm.model.result.PostResult;
import com.hql.scm.model.result.ReplyResult;
import com.hql.scm.service.PostService;
import com.hql.scm.util.FileTools;
import com.hql.scm.util.UseDefaultSuccessResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;

@Controller
public class PostSubmitController {
    @Value("${file.images.post}")
    private String postImagePath;

    @Resource
    PostService service;

    @Resource
    PostQueryController postQueryController;

    @GetMapping("/mainpost/details/view/{id}")
    public String sendPost(Model model, HttpSession session, @PathVariable Integer id) {
        Result<MainPostResult> result = postQueryController.getMainDetails(session, id);
        model.addAttribute("mp", result.getData());
        return "/public/post/mainpostdetails";
    }

    @PostMapping("/user/mainpost/reply")
    @ResponseBody
    public Result<PostResult> replyMainPost(
            HttpSession session,
            @SessionAttribute("user") User user,
            Integer mainPostId,
            String content,
            @RequestParam(value = "file", required = false) MultipartFile[] files
    ) throws IOException {
        Post post = service.addPost(user.getId(), mainPostId, content);
        if (files != null) {
            File path = FileTools.getImagePath(postImagePath, post.getId());
            path.mkdirs();
            for (MultipartFile file : files) {
                File f = FileTools.makeImagePath(postImagePath, post.getId());
                FileTools.write(file.getInputStream(), f);
            }
        }
        return postQueryController.getDetails(session, post.getId());
    }

    @PostMapping("/user/post/reply")
    @ResponseBody
    public Result<ReplyResult> replyPost(
            HttpSession session,
            @SessionAttribute("user") User user,
            Integer postId,
            String content
    ) {
        Reply reply = service.addReply(user.getId(), postId, content);
        return postQueryController.getReplyDetails(session, reply.getId());
    }

    @PostMapping("/user/reply/reply")
    @ResponseBody
    public Result<ReplyResult> replyReply(
            HttpSession session,
            @SessionAttribute("user") User user,
            Integer replyId,
            String content
    ) {
        Reply reply = service.addReplyChild(user.getId(), replyId, content);
        return postQueryController.getReplyDetails(session, reply.getId());
    }

    @DeleteMapping("/user/post/delete/{id}")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void deletePost(@SessionAttribute("user") User user, @PathVariable Integer id) {
        service.deletePost(user.getId(), id);
        File path = FileTools.getImagePath(postImagePath, id);
        FileTools.deleteDir(path);
    }

    @DeleteMapping("/user/reply/delete/{id}")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void deleteReply(@SessionAttribute("user") User user, @PathVariable Integer id) {
        service.deleteReply(user.getId(), id);
    }
}
