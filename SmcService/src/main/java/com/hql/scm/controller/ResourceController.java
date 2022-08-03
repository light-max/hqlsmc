package com.hql.scm.controller;

import com.hql.scm.constant.AssertException;
import com.hql.scm.constant.GlobalConstant;
import com.hql.scm.model.data.Result;
import com.hql.scm.model.entity.User;
import com.hql.scm.model.result.Images;
import com.hql.scm.util.FileTools;
import com.hql.scm.util.UseDefaultSuccessResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ResourceController {
    @Value("${file.images.notice}")
    private String noticeImagesPath;

    @Value("${file.images.head}")
    private String headImagesPath;

    @Value("${file.images.mainpost}")
    private String mainpostImagesPath;

    @Value("${file.images.post}")
    private String postImagePath;

    private String getImagePathByType(String type) {
        if ("notice".equals(type)) return noticeImagesPath;
        else if ("mainpost".equals(type)) return mainpostImagesPath;
        else if ("post".equals(type)) return postImagePath;
        else throw new AssertException(GlobalConstant.error.getMessage());
    }

    @PostMapping("/admin/{type}/image/{id}")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void addImages(
            @PathVariable(required = false) String type,
            @PathVariable(required = false) Integer id,
            MultipartFile file
    ) throws IOException {
        File path = FileTools.makeImagePath(getImagePathByType(type), id);
        path.getParentFile().mkdirs();
        FileTools.write(file.getInputStream(), path);
    }

    @GetMapping(value = "/{type}/image/{id}/{index}", produces = "image/jpeg")
    @ResponseBody
    public ResponseEntity<FileSystemResource> getImage(
            @PathVariable String type,
            @PathVariable Integer id,
            @PathVariable Integer index
    ) {
        File file = FileTools.getImagePath(getImagePathByType(type), id, index);
        return ResponseEntity.ok(new FileSystemResource(file));
    }

    @DeleteMapping("/{type}/image/{id}/{index}")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void deleteImage(
            @PathVariable String type,
            @PathVariable Integer id,
            @PathVariable Integer index
    ) {
        File file = FileTools.getImagePath(getImagePathByType(type), id, index);
        file.delete();
    }

    @DeleteMapping("/{type}/images/{id}")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void deleteImages(
            @PathVariable String type,
            @PathVariable Integer id
    ) {
        File path = FileTools.getImagePath(getImagePathByType(type), id);
        if (path.exists()) {
            File[] files = path.listFiles();
            if (files != null) {
                for (File file : files) {
                    file.delete();
                }
            }
            path.delete();
        }
    }

    @GetMapping({"/{type}/images/{id}"})
    @ResponseBody
    public Result<Images> getImages(@PathVariable String type, @PathVariable Integer id) {
        File path = FileTools.getImagePath(getImagePathByType(type), id);
        File[] files = path.listFiles();
        if (files == null) {
            return Result.success(Images.builder()
                    .id(id)
                    .urls(new ArrayList<>())
                    .build());
        }
        List<String> urls = new ArrayList<>();
        for (File file : files) {
            urls.add("/" + type + "/image/" + id + "/" + file.getName());
        }
        return Result.success(Images.builder()
                .id(id)
                .urls(urls)
                .build());
    }

    @GetMapping("/head/raw/{id}")
    @ResponseBody
    public ResponseEntity<FileSystemResource> getHeadImage(@PathVariable Integer id) {
        File file = FileTools.getHeadImagePath(headImagesPath, id);
        return ResponseEntity.ok(new FileSystemResource(file));
    }

    @GetMapping("/head/{id}")
    public String getHead(@PathVariable Integer id) {
        File file = FileTools.getHeadImagePath(headImagesPath, id);
        if (file.exists()) {
            return "redirect:/head/raw/" + id;
        } else {
            return "redirect:/images/default-head.jpg";
        }
    }

    @GetMapping("/userhead")
    public String getHead(@SessionAttribute(value = "user", required = false) User user) {
        if (user == null) {
            return "redirect:/images/default-head.jpg";
        } else {
            return "redirect:/head/" + user.getId();
        }
    }

    @PostMapping("/user/head")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void setHead(@SessionAttribute("user") User user, MultipartFile file) throws IOException {
        File headImagePath = FileTools.getHeadImagePath(headImagesPath, user.getId());
        FileTools.write(file.getInputStream(), headImagePath);
    }

    @PostMapping("/admin/user/head/{id}")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void setUserHead(@PathVariable Integer id, MultipartFile file) throws IOException {
        File path = FileTools.getHeadImagePath(headImagesPath, id);
        FileTools.write(file.getInputStream(), path);
    }
}
