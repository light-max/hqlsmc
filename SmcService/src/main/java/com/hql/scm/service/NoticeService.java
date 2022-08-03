package com.hql.scm.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hql.scm.model.entity.Notice;
import com.hql.scm.model.td.NoticeTD;

import java.util.List;

public interface NoticeService extends IService<Notice> {
    Notice add(String title, String des, boolean top);

    void update(Integer id, String title, String des, boolean top);

    /**
     * @param type all,top,notop
     */
    Page<Notice> list(Integer n, String type);

    /**
     * @param type all,top,notop
     */
    Page<Notice> list(Integer n, int count, String type);

    Page<Notice> list(Integer n);

    NoticeTD getTD(Notice notice);

    List<NoticeTD> getTD(List<Notice> list);
}
