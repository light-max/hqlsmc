package com.hql.scm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hql.scm.constant.GlobalConstant;
import com.hql.scm.mapper.FollowMapper;
import com.hql.scm.model.entity.Follow;
import com.hql.scm.model.result.FollowState;
import com.hql.scm.service.FollowService;
import org.springframework.stereotype.Service;

@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow> implements FollowService {
    @Override
    public FollowState check(int userId, int targetId) {
        int fc = count(new QueryWrapper<Follow>()
                .lambda()
                .eq(Follow::getUserId, userId)
                .eq(Follow::getTargetId, targetId));
        int fec = count(new QueryWrapper<Follow>()
                .lambda()
                .eq(Follow::getUserId, targetId)
                .eq(Follow::getTargetId, userId));
        int count = count(new QueryWrapper<Follow>()
                .lambda()
                .eq(Follow::getUserId, userId));
        boolean check = fc > 0;
        boolean friend = check && fec > 0;
        return FollowState.builder()
                .userId(userId)
                .targetId(targetId)
                .count(count)
                .follow(check)
                .friend(friend)
                .build();
    }

    @Override
    public FollowState toggle(int userId, int targetId) {
        GlobalConstant.followError.isFalse(userId == targetId);
        Follow follow = getOne(new QueryWrapper<Follow>()
                .lambda()
                .eq(Follow::getUserId, userId)
                .eq(Follow::getTargetId, targetId));
        if (follow == null) {
            save(Follow.builder()
                    .userId(userId)
                    .targetId(targetId)
                    .build());
        } else {
            removeById(follow.getId());
        }
        return check(userId, targetId);
    }

    @Override
    public int getFollowCount(int userId) {
        return count(new QueryWrapper<Follow>()
                .lambda()
                .eq(Follow::getUserId, userId));
    }

    @Override
    public int getFollowerCount(int userId) {
        return count(new QueryWrapper<Follow>()
                .lambda()
                .eq(Follow::getTargetId, userId));
    }

    @Override
    public Page<Follow> followList(Integer n, int size, int userId) {
        Page<Follow> page = new Page<>(n == null ? 1 : n, size);
        return page(page, new QueryWrapper<Follow>()
                .lambda()
                .eq(Follow::getUserId, userId));
    }

    @Override
    public Page<Follow> followerList(Integer n, int size, int userId) {
        Page<Follow> page = new Page<>(n == null ? 1 : n, size);
        return page(page, new QueryWrapper<Follow>()
                .lambda()
                .eq(Follow::getTargetId, userId));
    }
}
