package com.hql.scm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hql.scm.mapper.LikeMapper;
import com.hql.scm.model.entity.Like;
import com.hql.scm.service.LikeService;
import org.springframework.stereotype.Service;

@Service
public class LikeServiceImpl extends ServiceImpl<LikeMapper, Like> implements LikeService {
    @Override
    public int getMainPostCount(int mainPostId) {
        LambdaQueryWrapper<Like> wrapper = new QueryWrapper<Like>()
                .lambda()
                .eq(Like::getType, 0)
                .eq(Like::getTargetId, mainPostId);
        return count(wrapper);
    }

    @Override
    public int getPostCount(int postId) {
        LambdaQueryWrapper<Like> wrapper = new QueryWrapper<Like>()
                .lambda()
                .eq(Like::getType, 1)
                .eq(Like::getTargetId, postId);
        return count(wrapper);
    }

    @Override
    public boolean isLike(int type, int targetId, int userId) {
        LambdaQueryWrapper<Like> wrapper = new QueryWrapper<Like>()
                .lambda()
                .eq(Like::getType, type)
                .eq(Like::getTargetId, targetId)
                .eq(Like::getUserId, userId);
        return count(wrapper) > 0;
    }

    @Override
    public boolean toggle(int type, int targetId, int userId) {
        LambdaQueryWrapper<Like> wrapper = new QueryWrapper<Like>()
                .lambda()
                .eq(Like::getType, type)
                .eq(Like::getTargetId, targetId)
                .eq(Like::getUserId, userId);
        Like like = getOne(wrapper);
        if (like == null) {
            save(Like.builder().type(type)
                    .targetId(targetId)
                    .userId(userId)
                    .build());
            return true;
        } else {
            removeById(like.getId());
            return false;
        }
    }
}
