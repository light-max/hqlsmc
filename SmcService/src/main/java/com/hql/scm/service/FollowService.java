package com.hql.scm.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hql.scm.model.entity.Follow;
import com.hql.scm.model.result.FollowState;

public interface FollowService extends IService<Follow> {
    FollowState check(int userId, int targetId);

    FollowState toggle(int userId, int targetId);

    int getFollowCount(int userId);

    int getFollowerCount(int userId);

    Page<Follow> followList(Integer n, int size, int userId);

    Page<Follow> followerList(Integer n,int size,int userId);
}
