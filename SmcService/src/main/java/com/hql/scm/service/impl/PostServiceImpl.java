package com.hql.scm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hql.scm.constant.GlobalConstant;
import com.hql.scm.mapper.*;
import com.hql.scm.model.entity.*;
import com.hql.scm.service.PostService;
import com.hql.scm.util.FileTools;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    @Resource
    PostDraftMapper draftMapper;

    @Resource
    MainPostMapper mainPostMapper;

    @Resource
    PostMapper postMapper;

    @Resource
    ReplyMapper replyMapper;

    @Resource
    LikeMapper likeMapper;

    @Resource
    FollowMapper followMapper;

    @Value("${file.images.post}")
    private String postImagePath;

    @Override
    public PostDraft saveDraft(int userId, String title, String content) {
        PostDraft draft = draftMapper.selectById(userId);
        if (draft == null) {
            draft = PostDraft.builder()
                    .userId(userId)
                    .title(title)
                    .content(content)
                    .build()
                    .check();
            draftMapper.insert(draft);
        } else {
            draft.setTitle(title);
            draft.setContent(content);
            draft.check();
            draftMapper.updateById(draft);
        }
        return draft;
    }

    @Override
    public PostDraft getDraftNotNull(int userId) {
        PostDraft draft = draftMapper.selectById(userId);
        if (draft == null) {
            draft = PostDraft.builder()
                    .userId(userId)
                    .title("")
                    .content("")
                    .build();
            draftMapper.insert(draft);
        }
        return draft;
    }

    @Override
    public void deleteDraft(int userId) {
        PostDraft draft = draftMapper.selectById(userId);
        if (draft != null) {
            draft.setTitle("");
            draft.setContent("");
            draft.setUserId(userId);
            draftMapper.updateById(draft);
        }
    }

    @Override
    public MainPost addMainPost(int userId) {
        PostDraft draft = getDraftNotNull(userId);
        MainPost mainPost = MainPost.builder()
                .title(draft.getTitle().trim())
                .content(draft.getContent())
                .userId(draft.getUserId())
                .build();
        mainPostMapper.insert(mainPost.check());
        return mainPost;
    }

    @Override
    public void deleteMainPost(int userId, int id) {
        MainPost mainPost = mainPostMapper.selectById(id);
        if (mainPost != null) {
            GlobalConstant.dataNotExists.isTrue(mainPost.getUserId() == userId);
            // 删除主帖
            mainPostMapper.deleteById(id);
            // 删除主帖的点赞
            likeMapper.delete(new QueryWrapper<Like>()
                    .lambda()
                    .eq(Like::getType, 0)
                    .eq(Like::getTargetId, id));
            // 查询子帖
            LambdaQueryWrapper<Post> postWrapper = new QueryWrapper<Post>()
                    .lambda()
                    .eq(Post::getMainId, id);
            List<Post> posts = postMapper.selectList(postWrapper);
            // 遍历子帖
            for (Post post : posts) {
                // 删除子帖的点赞
                likeMapper.delete(new QueryWrapper<Like>()
                        .lambda()
                        .eq(Like::getType, 1)
                        .eq(Like::getTargetId, post.getId()));
                // 删除子帖的回复
                replyMapper.delete(new QueryWrapper<Reply>()
                        .lambda()
                        .eq(Reply::getPostId, post.getId()));
                // 删除子帖的图片
                File path = FileTools.getImagePath(postImagePath, post.getId());
                FileTools.deleteDir(path);
            }
            // 删除子帖
            postMapper.delete(postWrapper);
        }
    }

    @Override
    public int getMainPostCount(int userId) {
        LambdaQueryWrapper<MainPost> wrapper = new QueryWrapper<MainPost>()
                .lambda()
                .eq(MainPost::getUserId, userId);
        return mainPostMapper.selectCount(wrapper);
    }

    @Override
    public int getPostCount(int mainPostId) {
        LambdaQueryWrapper<Post> wrapper = new QueryWrapper<Post>()
                .lambda()
                .eq(Post::getMainId, mainPostId);
        return postMapper.selectCount(wrapper);
    }

    @Override
    public int getReplyCount(int postId) {
        LambdaQueryWrapper<Reply> wrapper = new QueryWrapper<Reply>()
                .lambda()
                .eq(Reply::getPostId, postId);
        return replyMapper.selectCount(wrapper);
    }

    @Override
    public Page<MainPost> getMainPostNewestList(Integer n, int size) {
        Page<MainPost> page = new Page<>(n == null ? 1 : n, size);
        LambdaQueryWrapper<MainPost> wrapper = new QueryWrapper<MainPost>()
                .lambda()
                .orderByDesc(MainPost::getCreateTime);
        return mainPostMapper.selectPage(page, wrapper);
    }

    @Override
    public Page<MainPost> getMainPostList(Integer n, int size, Integer userId) {
        Page<MainPost> page = new Page<>(n == null ? 1 : n, size);
        if (userId == null) {
            return mainPostMapper.selectPage(page, Wrappers.emptyWrapper());
        } else {
            LambdaQueryWrapper<MainPost> wrapper = new QueryWrapper<MainPost>()
                    .lambda()
                    .eq(MainPost::getUserId, userId);
            return mainPostMapper.selectPage(page, wrapper);
        }
    }

    @Override
    public Page<Post> getPostListByMainPostId(Integer n, int size, int mainId) {
        Page<Post> page = new Page<>(n == null ? 1 : n, size);
        LambdaQueryWrapper<Post> wrapper = new QueryWrapper<Post>()
                .lambda()
                .eq(Post::getMainId, mainId);
        return postMapper.selectPage(page, wrapper);
    }

    @Override
    public Page<Reply> getReplyListByPostId(Integer n, int size, int postId) {
        Page<Reply> page = new Page<>(n == null ? 1 : n, size);
        LambdaQueryWrapper<Reply> wrapper = new QueryWrapper<Reply>()
                .lambda()
                .eq(Reply::getPostId, postId);
        return replyMapper.selectPage(page, wrapper);
    }

    @Override
    public Page<MainPost> getMainPostByFollowerId(Integer n, int size, Integer followerId) {
        Page<MainPost> page = new Page<>(n == null ? 1 : n, size);
        List<Follow> follows = followMapper.selectList(new QueryWrapper<Follow>()
                .lambda()
                .eq(Follow::getUserId, followerId));
        List<Integer> ids = new ArrayList<>();
        ids.add(followerId);
        for (Follow follow : follows) {
            ids.add(follow.getTargetId());
        }
        LambdaQueryWrapper<MainPost> wrapper = new QueryWrapper<MainPost>()
                .lambda()
                .in(MainPost::getUserId, ids)
                .orderByDesc(MainPost::getCreateTime);
        return mainPostMapper.selectPage(page, wrapper);
    }

    @Override
    public Post addPost(int userId, int mainPostId, String content) {
        Post post = Post.builder()
                .userId(userId)
                .mainId(mainPostId)
                .content(content)
                .build();
        postMapper.insert(post.check());
        return post;
    }

    @Override
    public Reply addReply(int userId, int postId, String content) {
        Reply reply = Reply.builder()
                .userId(userId)
                .postId(postId)
                .content(content)
                .build();
        replyMapper.insert(reply);
        return reply;
    }

    @Override
    public Reply addReplyChild(int userId, int replyId, String content) {
        Reply r = replyMapper.selectById(replyId);
        GlobalConstant.dataNotExists.notNull(r);
        Reply reply = Reply.builder()
                .userId(userId)
                .content(content)
                .postId(r.getPostId())
                .targetId(r.getUserId())
                .build();
        replyMapper.insert(reply);
        return reply;
    }

    @Override
    public void deletePost(int userId, int postId) {
        Post post = postMapper.selectById(postId);
        if (post != null) {
            GlobalConstant.dataNotExists.isTrue(post.getUserId() == userId);
            postMapper.deleteById(postId);
            LambdaQueryWrapper<Reply> wrapper = new QueryWrapper<Reply>()
                    .lambda()
                    .eq(Reply::getPostId, postId);
            replyMapper.delete(wrapper);
            likeMapper.delete(new QueryWrapper<Like>()
                    .lambda()
                    .eq(Like::getType, 1)
                    .eq(Like::getTargetId, postId));
        }
    }

    @Override
    public void deleteReply(int userId, int replyId) {
        Reply reply = replyMapper.selectById(replyId);
        if (reply != null) {
            GlobalConstant.dataNotExists.isTrue(reply.getUserId() == userId);
            replyMapper.deleteById(replyId);
        }
    }

    @Override
    public Page<MainPost> queryMainPost(Integer n, String title, Integer userId, String sort) {
        LambdaQueryWrapper<MainPost> wrapper = new QueryWrapper<MainPost>()
                .lambda();
        if (title != null) {
            wrapper.like(MainPost::getTitle, title);
        }
        if (userId != null) {
            wrapper.like(MainPost::getUserId, userId);
        }
        if ("desc".equals(sort)) {
            wrapper.orderByDesc(MainPost::getCreateTime);
        }
        if ("asc".equals(sort)) {
            wrapper.orderByAsc(MainPost::getCreateTime);
        }
        return mainPostMapper.selectPage(new Page<>(n == null ? 1 : n, 8), wrapper);
    }

    @Override
    public List<MainPost> queryMainPost(String w) {
        if (w != null && w.trim().length() > 0) {
            return mainPostMapper.selectList(new QueryWrapper<MainPost>()
                    .lambda()
                    .like(MainPost::getTitle, w)
                    .or()
                    .like(MainPost::getContent, w));
        } else {
            return new ArrayList<>();
        }
    }
}
