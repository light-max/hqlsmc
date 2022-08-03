package com.hql.scm.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hql.scm.model.entity.Task;

public interface DailyAttendanceService extends IService<Task> {
    Task submit(int userId, int reviewId, String des);

    Task review(int userId, int taskId, String des, boolean flag);

    /**
     * @param state {@link Task#getState()} -1:all
     */
    Page<Task> getListByUserId(int n, int size, int userId, int state);

    /**
     * @param state {@link Task#getState()} -1:all
     */
    Page<Task> getListByReviewId(int n, int size, int reviewId, int state);

    Page<Task> queryList(Integer n, Integer userId, Integer reviewId, String sort);
}
