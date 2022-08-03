package com.hql.scm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hql.scm.constant.GlobalConstant;
import com.hql.scm.mapper.TaskMapper;
import com.hql.scm.model.entity.Task;
import com.hql.scm.service.DailyAttendanceService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class DailyAttendanceServiceImpl extends ServiceImpl<TaskMapper, Task> implements DailyAttendanceService {
    @Override
    public Task submit(int userId, int reviewId, String des) {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        Task task = getOne(new QueryWrapper<Task>()
                .lambda()
                .eq(Task::getUserId, userId)
                .eq(Task::getCreateDate, date));
        if (task != null) {
            if (task.getState() == 0) {
                GlobalConstant.dailyAttendanceError1.newException();
            } else {
                GlobalConstant.dailyAttendanceError.newException();
            }
        } else {
            task = Task.builder()
                    .userId(userId)
                    .reviewId(reviewId)
                    .des(des)
                    .createDate(date)
                    .build();
            save(task.check());
            task = getById(task.getId());
        }
        return task;
    }

    @Override
    public Task review(int userId, int taskId, String des, boolean flag) {
        Task task = getById(taskId);
        GlobalConstant.dataNotExists.notNull(task);
        GlobalConstant.error.isTrue(task.getReviewId() == userId);
        task.setReviewMsg(des);
        task.setReviewTime(System.currentTimeMillis());
        task.setState(flag ? 1 : 2);
        updateById(task);
        return task;
    }

    @Override
    public Page<Task> getListByUserId(int n, int size, int userId, int state) {
        LambdaQueryWrapper<Task> wrapper = new QueryWrapper<Task>()
                .lambda()
                .eq(Task::getUserId, userId)
                .orderByDesc(Task::getCreateTime);
        if (state != -1) {
            wrapper.eq(Task::getState, state);
        }
        return page(new Page<>(n, size), wrapper);
    }

    @Override
    public Page<Task> getListByReviewId(int n, int size, int reviewId, int state) {
        LambdaQueryWrapper<Task> wrapper = new QueryWrapper<Task>()
                .lambda()
                .eq(Task::getReviewId, reviewId)
                .orderByDesc(Task::getCreateTime);
        if (state != -1) {
            wrapper.eq(Task::getState, state);
        }
        return page(new Page<>(n, size), wrapper);
    }

    @Override
    public Page<Task> queryList(Integer n, Integer userId, Integer reviewId, String sort) {
        LambdaQueryWrapper<Task> wrapper = new QueryWrapper<Task>()
                .lambda();
        if (userId != null) {
            wrapper.eq(Task::getUserId, userId);
        }
        if (reviewId != null) {
            wrapper.eq(Task::getReviewId, reviewId);
        }
        if ("asc".equals(sort)) {
            wrapper.orderByAsc(Task::getCreateTime);
        }
        if ("desc".equals(sort)) {
            wrapper.orderByDesc(Task::getCreateTime);
        }
        return page(new Page<>(n == null ? 1 : n, 8), wrapper);
    }
}
