package com.hql.scm.controller.user.post;

import com.hql.scm.model.data.Result;
import com.hql.scm.model.entity.MainPost;
import com.hql.scm.model.entity.PostDraft;
import com.hql.scm.model.entity.User;
import com.hql.scm.model.result.Images;
import com.hql.scm.service.PostService;
import com.hql.scm.util.FileTools;
import com.hql.scm.util.UseDefaultSuccessResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 主帖controller
 */
@Controller
public class MainPostSubmitController {
    @Value("${file.images.draft}")
    private String draftImagesPath;

    @Value("${file.images.mainpost}")
    private String mainPostImagesPath;

    @Resource
    PostService service;

    @GetMapping("/user/post")
    public String post(Model model, @SessionAttribute("user") User user) {
        Result<Images> images = getImages(user);
        Result<PostDraft> draft = postDraft(user);
        model.addAttribute("images", images.getData());
        model.addAttribute("draft", draft.getData());
        return "/user/post/send";
    }

    /**
     * 发帖，发帖之前需要保存内容到草稿，将草稿发布成帖子
     */
    @PostMapping("/user/post/draft/submit")
    @ResponseBody
    public Result<MainPost> post(@SessionAttribute("user") User user) throws IOException {
        MainPost post = service.addMainPost(user.getId());
        File postPath = FileTools.getImagePath(mainPostImagesPath, post.getId());
        File draftPath = FileTools.getImagePath(draftImagesPath, user.getId());
        File[] files = draftPath.listFiles();
        if (files != null) {
            postPath.mkdirs();
            for (File source : files) {
                File target = new File(postPath, source.getName());
                FileTools.write(new FileInputStream(source), target);
            }
        }
        deleteDraft(user);
        return Result.success(post);
    }

    /**
     * 删除主贴，会把子帖和评论一起删除
     */
    @DeleteMapping("/user/mainpost/{id}")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void delete(@SessionAttribute("user") User user, @PathVariable Integer id) {
        service.deleteMainPost(user.getId(), id);
        File path = FileTools.getImagePath(mainPostImagesPath, id);
        FileTools.deleteDir(path);
    }

    @PostMapping("/user/post/draft")
    @ResponseBody
    public Result<PostDraft> postDraft(
            @SessionAttribute("user") User user,
            String title, String content
    ) {
        return Result.success(service.saveDraft(user.getId(), title, content));
    }

    @GetMapping("/user/post/draft")
    @ResponseBody
    public Result<PostDraft> postDraft(@SessionAttribute("user") User user) {
        return Result.success(service.getDraftNotNull(user.getId()));
    }

    @DeleteMapping("/user/post/draft")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void deleteDraft(@SessionAttribute("user") User user) {
        service.deleteDraft(user.getId());
        File path = FileTools.getImagePath(draftImagesPath, user.getId());
        FileTools.deleteDir(path);
    }

    @PostMapping("/user/post/draft/image")
    @ResponseBody
    public Result<String> addDraft(
            @SessionAttribute("user") User user,
            MultipartFile file
    ) throws IOException {
        File path = FileTools.makeImagePath(draftImagesPath, user.getId());
        path.getParentFile().mkdirs();
        FileTools.write(file.getInputStream(), path);
        return Result.success("/user/post/draft/image/" + path.getName());
    }

    @GetMapping(value = "/user/post/draft/image/{index}", produces = "image/jpeg")
    @ResponseBody
    public ResponseEntity<FileSystemResource> getImage(
            @SessionAttribute("user") User user,
            @PathVariable Integer index
    ) {
        File file = FileTools.getImagePath(draftImagesPath, user.getId(), index);
        return ResponseEntity.ok(new FileSystemResource(file));
    }

    @DeleteMapping("/user/post/draft/image/{index}")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void deleteImage(
            @SessionAttribute("user") User user,
            @PathVariable Integer index
    ) {
        File file = FileTools.getImagePath(draftImagesPath, user.getId(), index);
        file.delete();
    }

    @GetMapping("/user/post/draft/images")
    @ResponseBody
    public Result<Images> getImages(@SessionAttribute("user") User user) {
        File path = FileTools.getImagePath(draftImagesPath, user.getId());
        File[] files = path.listFiles();
        if (files == null) {
            return Result.success(Images.builder()
                    .id(user.getId())
                    .urls(new ArrayList<>())
                    .build());
        }
        List<String> urls = new ArrayList<>();
        for (File file : files) {
            urls.add("/user/post/draft/image/" + file.getName());
        }
        return Result.success(Images.builder()
                .id(user.getId())
                .urls(urls)
                .build());
    }
}
