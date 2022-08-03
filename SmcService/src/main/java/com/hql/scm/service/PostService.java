package com.hql.scm.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hql.scm.model.entity.MainPost;
import com.hql.scm.model.entity.Post;
import com.hql.scm.model.entity.PostDraft;
import com.hql.scm.model.entity.Reply;

import java.util.List;

public interface PostService {
    PostDraft saveDraft(int userId, String title, String content);

    PostDraft getDraftNotNull(int userId);

    void deleteDraft(int userId);

    MainPost addMainPost(int userId);

    void deleteMainPost(int userId, int id);

    int getMainPostCount(int userId);

    int getPostCount(int mainPostId);

    int getReplyCount(int postId);

    Page<MainPost> getMainPostNewestList(Integer n, int size);

    Page<MainPost> getMainPostList(Integer n, int size, Integer userId);

    Page<Post> getPostListByMainPostId(Integer n, int size, int mainId);

    Page<Reply> getReplyListByPostId(Integer n, int size, int postId);

    Page<MainPost> getMainPostByFollowerId(Integer n, int size, Integer followerId);

    Post addPost(int userId, int mainPostId, String content);

    Reply addReply(int userId, int postId, String content);

    Reply addReplyChild(int userId, int replyId, String content);

    void deletePost(int userId, int postId);

    void deleteReply(int userId, int replyId);

    Page<MainPost> queryMainPost(Integer n, String title, Integer userId, String sort);

    List<MainPost> queryMainPost(String w);
}
