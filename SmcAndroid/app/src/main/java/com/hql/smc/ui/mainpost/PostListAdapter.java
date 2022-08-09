package com.hql.smc.ui.mainpost;

import android.view.View;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.hql.smc.R;
import com.hql.smc.api.ExRequestBuilder;
import com.hql.smc.base.recycler.SimpleSingleItemRecyclerAdapter;
import com.hql.smc.data.result.Post;
import com.hql.smc.utils.GlideUtils;

import java.util.List;

public class PostListAdapter extends SimpleSingleItemRecyclerAdapter<Post> {
    private OnItemClickListener<Post> onLikeListener;
    private OnItemClickListener<Post> onOpenSpaceListener;
    private OnItemClickListener<Post> onReplyListener;
    private OnItemClickListener<Post> onDeleteListener;

    public PostListAdapter(List<Post> data) {
        super(data);
    }

    @Override
    protected int getItemViewLayout() {
        return R.layout.item_post;
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, Post data, int position) {
        holder.setText(R.id.nickname, data.getNickname())
                .setText(R.id.time, data.getTime())
                .setText(R.id.content, data.getContent())
                .setText(R.id.like_count, String.valueOf(data.getLikeCount()))
                .setText(R.id.reply_count, String.valueOf(data.getReplyCount()));
        Glide.with(holder.itemView)
                .load(ExRequestBuilder.getUrl("/head/" + data.getUserId()))
                .apply(GlideUtils.skipCache())
                .into(holder.getImage(R.id.head));
        holder.getImage(R.id.like).setImageResource(data.isLike() ? R.drawable.ic_like_true : R.drawable.ic_like_false);
        holder.get(R.id.delete).setVisibility(data.isMe() ? View.VISIBLE : View.GONE);
        ImageListLayout images = holder.get(R.id.images);
        images.setImages(data.getImages());

        holder.click(R.id.like, () -> {
            if (onLikeListener != null) {
                onLikeListener.onClick(data, holder.getAdapterPosition());
            }
        });
        Runnable openSpaceRunnable = () -> {
            if (onOpenSpaceListener != null) {
                onOpenSpaceListener.onClick(data, holder.getAdapterPosition());
            }
        };
        holder.click(R.id.head, openSpaceRunnable);
        holder.click(R.id.nickname, openSpaceRunnable);
        Runnable onReplyRunnable = () -> {
            if (onReplyListener != null) {
                onReplyListener.onClick(data, holder.getAdapterPosition());
            }
        };
        holder.click(R.id.reply, onReplyRunnable);
        holder.click(R.id.reply_count, onReplyRunnable);
        holder.click(R.id.delete, () -> {
            if (onDeleteListener != null) {
                onDeleteListener.onClick(data, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.size() == 0) {
            super.onBindViewHolder(holder, position, payloads);
        } else {
            Post post = (Post) payloads.get(0);
            holder.getImage(R.id.like).setImageResource(post.isLike() ? R.drawable.ic_like_true : R.drawable.ic_like_false);
            holder.setText(R.id.like_count, String.valueOf(post.getLikeCount()))
                    .setText(R.id.reply_count, String.valueOf(post.getReplyCount()));
        }
    }

    public void setOnLikeListener(OnItemClickListener<Post> onLikeListener) {
        this.onLikeListener = onLikeListener;
    }

    public void setOnOpenSpaceListener(OnItemClickListener<Post> onOpenSpaceListener) {
        this.onOpenSpaceListener = onOpenSpaceListener;
    }

    public void setOnReplyListener(OnItemClickListener<Post> onReplyListener) {
        this.onReplyListener = onReplyListener;
    }

    public void setOnDeleteListener(OnItemClickListener<Post> onDeleteListener) {
        this.onDeleteListener = onDeleteListener;
    }
}
