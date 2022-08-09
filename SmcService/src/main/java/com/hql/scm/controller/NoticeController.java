package com.hql.scm.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hql.scm.constant.GlobalConstant;
import com.hql.scm.model.data.Pager;
import com.hql.scm.model.data.PagerData;
import com.hql.scm.model.data.Result;
import com.hql.scm.model.entity.Notice;
import com.hql.scm.model.result.Images;
import com.hql.scm.model.result.NoticeResult;
import com.hql.scm.model.td.NoticeTD;
import com.hql.scm.service.NoticeService;
import com.hql.scm.util.UseDefaultSuccessResponse;
import com.hql.scm.util.ump.ViewModelParameter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class NoticeController {
    @Resource
    NoticeService service;

    @Resource
    ResourceController resourceController;

    @GetMapping({"/admin/notice", "/admin/notice/list", "/admin/notice/list/{n}"})
    @ViewModelParameter(key = "view", value = "notice")
    public String list(
            Model model,
            @PathVariable(required = false) Integer n,
            @RequestParam(required = false) String type
    ) {
        type = type == null ? "all" : type;
        Page<Notice> page = service.list(n, type);
        List<NoticeTD> list = service.getTD(page.getRecords());
        Pager pager = new Pager(page, "/admin/notice/list");
        pager.setTailAppend("?type=" + type);
        model.addAttribute("list", list);
        model.addAttribute("pager", pager);
        model.addAttribute("type", type);
        return "/admin/notice/list";
    }

    @GetMapping({"/notice/list", "/notice/list/{n}"})
    public String list(Model model, @PathVariable(required = false) Integer n) {
        Page<Notice> page = service.list(n);
        Pager pager = new Pager(page, "/notice/list");
        List<Notice> list = page.getRecords();
        List<NoticeTD> tds = service.getTD(list);
        model.addAttribute("list", tds);
        model.addAttribute("pager", pager);
        return "/public/notice/list";
    }

    @GetMapping("/api/notice/list/{n}")
    @ResponseBody
    public Result<PagerData> list(@PathVariable Integer n) {
        Page<Notice> page = service.list(n);
        Pager pager = new Pager(page, "/notice/list");
        List<Notice> list = page.getRecords();
        return Result.pager(pager, list);
    }

    @GetMapping("/admin/notice/add")
    @ViewModelParameter(key = "view", value = "notice")
    public String add(Model model) {
        return "/admin/notice/add";
    }

    @PostMapping("/admin/notice/add")
    @ResponseBody
    public Result<Notice> add(String title, String des, Boolean top) {
        return Result.success(service.add(title, des, top));
    }

    @GetMapping("/admin/notice/update/{id}")
    @ViewModelParameter(key = "view", value = "notice")
    public String update(Model model, @PathVariable Integer id) {
        model.addAttribute("notice", service.getById(id));
        return "/admin/notice/update";
    }

    @PostMapping("/admin/notice/update/{id}")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void update(@PathVariable Integer id, String title, String des, Boolean top) {
        service.update(id, title, des, top);
    }

    @DeleteMapping("/admin/notice/delete/{id}")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void delete(@PathVariable Integer id) {
        service.removeById(id);
        resourceController.deleteImages("notice", id);
    }

    @GetMapping(value = "/notice/details/{id}", produces = "text/html")
    public String details(Model model, @PathVariable Integer id) {
        Notice notice = service.getById(id);
        if (notice != null) {
            Images images = resourceController.getImages("notice", notice.getId()).getData();
            model.addAttribute("notice", new NoticeResult(notice, images));
        }
        return "/public/notice/details";
    }

    @GetMapping("/notice/details/{id}")
    @ResponseBody
    public Result<NoticeResult> getById(@PathVariable Integer id) {
        Notice notice = service.getById(id);
        GlobalConstant.dataNotExists.notNull(notice);
        Images images = resourceController.getImages("notice", notice.getId()).getData();
        return Result.success(new NoticeResult(notice, images));
    }
}
