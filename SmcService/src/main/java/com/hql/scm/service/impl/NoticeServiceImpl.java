package com.hql.scm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hql.scm.constant.GlobalConstant;
import com.hql.scm.mapper.NoticeMapper;
import com.hql.scm.model.entity.Notice;
import com.hql.scm.model.td.NoticeTD;
import com.hql.scm.service.NoticeService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {
    @Override
    public Notice add(String title, String des, boolean top) {
        Notice notice = Notice.builder()
                .title(title)
                .des(des)
                .build()
                .check();
        if (top) {
            Integer topIndex = baseMapper.getTopGrade();
            notice.setGrade(topIndex == null ? 1 : topIndex + 1);
        }
        save(notice);
        return notice;
    }

    @Override
    public void update(Integer id, String title, String des, boolean top) {
        Notice notice = getById(id);
        GlobalConstant.dataNotExists.notNull(notice);
        notice.setTitle(title);
        notice.setDes(des);
        if (top) {
            Integer topIndex = baseMapper.getTopGrade();
            notice.setGrade(topIndex == null ? 1 : topIndex + 1);
        } else {
            notice.setGrade(0);
        }
        notice.check();
        updateById(notice);
    }

    @Override
    public Page<Notice> list(Integer n, String type) {
        return list(n, 4, type);
    }

    @Override
    public Page<Notice> list(Integer n, int count, String type) {
        Page<Notice> page = new Page<>(n == null ? 1 : n, count);
        LambdaQueryWrapper<Notice> wrapper = new QueryWrapper<Notice>()
                .lambda()
                .orderByDesc(Notice::getCreateTime);
        if ("top".equals(type)) {
            wrapper.ne(Notice::getGrade, 0);
        } else if ("notop".equals(type)) {
            wrapper.eq(Notice::getGrade, 0);
        }
        return page(page, wrapper);
    }

    @Override
    public Page<Notice> list(Integer n) {
        Page<Notice> page = new Page<>(n == null ? 1 : n, 16);
        LambdaQueryWrapper<Notice> wrapper = new QueryWrapper<Notice>()
                .lambda()
                .orderByDesc(Notice::getGrade)
                .orderByDesc(Notice::getCreateTime);
        return page(page, wrapper);
    }

    @Override
    public NoticeTD getTD(Notice notice) {
        return NoticeTD.builder()
                .id(notice.getId())
                .tittle(notice.getTitle())
                .des(notice.getDes())
                .createTime(notice.getCreateTime())
                .grade(notice.getGrade())
                .build();
    }

    @Override
    public List<NoticeTD> getTD(List<Notice> list) {
        List<NoticeTD> tds = new ArrayList<>();
        for (Notice notice : list) {
            tds.add(getTD(notice));
        }
        return tds;
    }
}
