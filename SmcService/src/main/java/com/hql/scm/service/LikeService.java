package com.hql.scm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hql.scm.model.entity.Like;

public interface LikeService extends IService<Like> {
    int getMainPostCount(int mainPostId);

    int getPostCount(int postId);

    /**
     * @param type 0:mainpost,1:post
     */
    boolean isLike(int type, int targetId, int userId);

    boolean toggle(int type, int targetId, int userId);
}
