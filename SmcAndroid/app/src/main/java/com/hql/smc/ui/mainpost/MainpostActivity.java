package com.hql.smc.ui.mainpost;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.widget.NestedScrollView;

import com.hql.smc.R;
import com.hql.smc.api.Api;
import com.hql.smc.async.Call;
import com.hql.smc.base.activity.BaseActivity;
import com.hql.smc.data.PagerData;
import com.hql.smc.data.result.Post;
import com.hql.smc.data.result.Reply;
import com.hql.smc.ui.mainpost.reply.ReplyFragment;
import com.hql.smc.ui.mainpost.replypost.ReplyPostFragment;
import com.hql.smc.ui.space.SpaceActivity;

public class MainpostActivity extends BaseActivity<MainpostModel, MainpostView> implements ReplyPostFragment.OnOptionListener, ReplyFragment.OnOptionListener {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_mainpost;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideActionBar();
        int mainPostId = getIntent().getIntExtra("id", 0);
        // 获取帖子详情
        Api.getMainPost(mainPostId).success(data -> {
            getView().setMainPost(data);
            // 获取关注状态
            Api.followCheck(data.getUserId()).success(state -> {
                if (state.isFriend()) {
                    getView().getFollow().setText("互相关注");
                } else {
                    getView().getFollow().setText(state.isFollow() ? "已关注" : "关注");
                }
            }).run();
            // 触发关注
            click(getView().getFollow(), () -> {
                Api.followToggle(data.getUserId())
                        .error((message, e) -> toast(message))
                        .success(state -> {
                            if (state.isFriend()) {
                                getView().getFollow().setText("互相关注");
                            } else {
                                getView().getFollow().setText(state.isFollow() ? "已关注" : "关注");
                            }
                        }).run();
            });
            // 打卡主帖发帖人空间
            Runnable openSpaceRunnable = () -> {
                SpaceActivity.start(this, data.getUserId());
            };
            click(getView().getHead(), openSpaceRunnable);
            click(getView().getNickname(), openSpaceRunnable);
        }).run();
        // 触发帖子的点赞
        click(getView().getLike(), () -> {
            Api.likeToggleMainPost(mainPostId).success(state -> {
                getView().getLike().setImageResource(state.isLike() ? R.drawable.ic_like_true : R.drawable.ic_like_false);
                getView().getLikeCount().setText(String.valueOf(state.getCount()));
            }).run();
        });
        // 触发回帖
        click(R.id.reply_post, () -> {
            getView().toggleReplyPostFragment();
        });
        // 加载回帖
        // 设置回帖设配器
        PostListAdapter adapter = new PostListAdapter(getModel().getPosts());
        getView().getRecycler().setAdapter(adapter);
        map("post_list_adapter", adapter);
        // 设置监听
        adapter.setOnLikeListener((data, position) -> {
            Api.likeTogglePost(data.getId())
                    .error((message, e) -> toast(message))
                    .success(state -> {
                        data.setLike(state.isLike());
                        data.setLikeCount(state.getCount());
                        adapter.notifyItemChanged(position, data);
                    }).run();
        });
        adapter.setOnOpenSpaceListener((data, position) -> {
            SpaceActivity.start(this, data.getUserId());
        });
        adapter.setOnReplyListener((data, position) -> {
            map("post_current_reply_index", position);
            getView().toggleReplyFragment();
            getView().getReplyFragment().setOnGetPostIdListener(data::getId);
        });
        adapter.setOnDeleteListener((data, position) -> {
            new AlertDialog.Builder(this)
                    .setMessage("你确定要删除这条回帖吗?")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", (dialog, which) -> {
                        Api.deletePost(data.getId())
                                .error((message, e) -> toast(message))
                                .success(() -> {
                                    getModel().getPosts().remove(position);
                                    adapter.notifyItemRemoved(position);
                                }).run();
                    }).show();
        });
        // 回帖适配器数据刷新回调
        Call.OnReturnData<PagerData<Post>> refreshData = data -> {
            if (data.getPager().getSize() > 0) {
                adapter.notifyDataSetChanged();
            }
        };
        // 回帖的上拉加载更多
        getView().getScroll().setOnScrollChangeListener(new View.OnScrollChangeListener() {
            private boolean isLoadMoreIng = false;

            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                NestedScrollView scroll = getView().getScroll();
                if (!isLoadMoreIng && scrollY >= scroll.getChildAt(0).getMeasuredHeight() - scroll.getMeasuredHeight()) {
                    getModel().loadPost(mainPostId)
                            .before(() -> isLoadMoreIng = true)
                            .after(() -> isLoadMoreIng = false)
                            .success(refreshData)
                            .run();
                }
            }
        });
        // 初始化回帖数据
        getModel().loadPost(mainPostId)
                .success(refreshData)
                .run();
        // 设置回帖Fragment回调
        getView().getReplyPostFragment().setOnOptionListener(this);
        getView().getReplyFragment().setOnOptionListener(this);
    }

    @Override
    public void onBackPressed() {
        ReplyPostFragment fragment = getView().getReplyPostFragment();
        ReplyFragment fragment1 = getView().getReplyFragment();
        if (!fragment.isVisible() && !fragment1.isVisible()) {
            super.onBackPressed();
        } else {
            if (getView().getReplyPostFragment().isVisible()) {
                getView().toggleReplyPostFragment();
            } else if (getView().getReplyFragment().isVisible()) {
                getView().toggleReplyFragment();
            }
        }
    }

    @Override
    public int getMainPostId() {
        return getIntent().getIntExtra("id", 0);
    }

    @Override
    public void onCancelReply() {
        onBackPressed();
    }

    @Override
    public void onSubmitReply(Reply reply) {
        try {
            int position = map("post_current_reply_index");
            Api.getPost(reply.getPostId()).success(data -> {
                PostListAdapter adapter = map("post_list_adapter");
                adapter.getData().set(position, data);
                adapter.notifyItemChanged(position, data);
            }).run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSubmitReplyPost(Post post) {
        getView().toggleReplyPostFragment();
        PostListAdapter adapter = map("post_list_adapter");
        adapter.getData().add(post);
        adapter.notifyItemInserted(adapter.getItemCount() - 1);
        Api.getMainPost(getMainPostId()).success(data -> {
            getView().setMainPost(data);
        }).run();
    }

    public static void start(Context context, int id) {
        Intent intent = new Intent(context, MainpostActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }
}
