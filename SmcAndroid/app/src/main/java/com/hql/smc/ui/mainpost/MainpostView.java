package com.hql.smc.ui.mainpost;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hql.smc.R;
import com.hql.smc.api.ExRequestBuilder;
import com.hql.smc.base.call.Base;
import com.hql.smc.base.mvp.BaseView;
import com.hql.smc.data.result.MainPost;
import com.hql.smc.ui.mainpost.reply.ReplyFragment;
import com.hql.smc.ui.mainpost.replypost.ReplyPostFragment;
import com.hql.smc.utils.GlideUtils;

public class MainpostView extends BaseView<MainpostActivity> {
    private ImageView head;
    private TextView nickname;
    private TextView time;
    private TextView follow;
    private TextView title;
    private TextView content;
    private ImageListLayout images;
    private ImageView like;
    private TextView likeCount;
    private TextView replyCount;
    private RecyclerView recycler;
    private NestedScrollView scroll;
    private ReplyPostFragment replyPostFragment;
    private ReplyFragment replyFragment;

    @Override
    public void onCreate(Base base, Bundle savedInstanceState) {
        super.onCreate(base, savedInstanceState);
        click(R.id.back, () -> base.getActivity().finish());
        head = get(R.id.head);
        nickname = get(R.id.nickname);
        time = get(R.id.time);
        follow = get(R.id.follow);
        title = get(R.id.title);
        content = get(R.id.content);
        images = get(R.id.images);
        like = get(R.id.like);
        likeCount = get(R.id.like_count);
        replyCount = get(R.id.reply_count);
        recycler = get(R.id.recycler);
        scroll = get(R.id.scroll);
        replyPostFragment = new ReplyPostFragment();
        replyFragment = new ReplyFragment();
    }

    public void setMainPost(MainPost post) {
        Glide.with(base.getActivity())
                .load(ExRequestBuilder.getUrl("/head/" + post.getUserId()))
                .apply(GlideUtils.skipCache())
                .into(head);
        nickname.setText(post.getNickname());
        time.setText(post.getTime());
        title.setText(post.getTitle());
        content.setText(post.getContent());
        images.setImages(post.getImages());
        like.setImageResource(post.isLike() ? R.drawable.ic_like_true : R.drawable.ic_like_false);
        likeCount.setText(String.valueOf(post.getLikeCount()));
        replyCount.setText(String.valueOf(post.getReplyCount()));
    }

    public void toggleReplyPostFragment() {
        FragmentTransaction transaction = base.getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.reply_layout_botton_in, R.anim.reply_layout_botton_out);
        if (replyPostFragment.isAdded()) {
            if (replyPostFragment.isHidden()) {
                transaction.show(replyPostFragment);
            } else {
                transaction.hide(replyPostFragment);
            }
        } else {
            transaction.add(R.id.reply_post_container, replyPostFragment);
        }
        transaction.commit();
    }

    public void toggleReplyFragment() {
        FragmentTransaction transaction = base.getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.reply_layout_botton_in, R.anim.reply_layout_botton_out);
        if (replyFragment.isAdded()) {
            if (replyFragment.isHidden()) {
                transaction.show(replyFragment);
            } else {
                transaction.hide(replyFragment);
            }
        } else {
            transaction.add(R.id.reply_post_container, replyFragment);
        }
        transaction.commit();
    }

    public ImageView getHead() {
        return head;
    }

    public TextView getNickname() {
        return nickname;
    }

    public TextView getTime() {
        return time;
    }

    public TextView getFollow() {
        return follow;
    }

    public TextView getTitle() {
        return title;
    }

    public TextView getContent() {
        return content;
    }

    public ImageListLayout getImages() {
        return images;
    }

    public ImageView getLike() {
        return like;
    }

    public TextView getLikeCount() {
        return likeCount;
    }

    public TextView getReplyCount() {
        return replyCount;
    }

    public RecyclerView getRecycler() {
        return recycler;
    }

    public NestedScrollView getScroll() {
        return scroll;
    }

    public ReplyPostFragment getReplyPostFragment() {
        return replyPostFragment;
    }

    public ReplyFragment getReplyFragment() {
        return replyFragment;
    }
}
