package com.hql.smc.ui.home.comm;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.hql.smc.R;
import com.hql.smc.api.ExRequestBuilder;
import com.hql.smc.base.recycler.SimpleSingleItemRecyclerAdapter;
import com.hql.smc.data.result.MainPost;
import com.hql.smc.utils.GlideUtils;

import java.util.List;

public class MainPostAdapter extends SimpleSingleItemRecyclerAdapter<MainPost> {
    private OnItemClickListener<MainPost> onLikeListener;
    private OnItemClickListener<MainPost> onOpenSpaceListener;

    public MainPostAdapter(List<MainPost> data) {
        super(data);
    }

    @Override
    protected int getItemViewLayout() {
        return R.layout.item_main_post;
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, MainPost data, int position) {
        holder.setText(R.id.nickname, data.getNickname())
                .setText(R.id.time, data.getTime())
                .setText(R.id.title, data.getTitle())
                .setText(R.id.content, data.getContent())
                .setText(R.id.like_count, String.valueOf(data.getLikeCount()))
                .setText(R.id.reply_count, String.valueOf(data.getReplyCount()));
        Glide.with(holder.itemView)
                .load(ExRequestBuilder.getUrl("/head/" + data.getUserId()))
                .apply(GlideUtils.skipCache())
                .into(holder.getImage(R.id.head));

        ImageView[] images = new ImageView[]{
                holder.getImage(R.id.image1),
                holder.getImage(R.id.image2),
                holder.getImage(R.id.image3),
        };
        for (ImageView image : images) {
            image.setVisibility(View.GONE);
        }
        List<String> urls = data.getImages().getUrls();
        for (int i = 0; i < urls.size(); i++) {
            images[i].setVisibility(View.VISIBLE);
            Glide.with(holder.itemView)
                    .load(ExRequestBuilder.getUrl(urls.get(i)))
                    .apply(GlideUtils.skipCache())
                    .into(images[i]);
        }

        holder.getImage(R.id.like).setImageResource(data.isLike() ? R.drawable.ic_like_true : R.drawable.ic_like_false);
        holder.click(R.id.like, () -> {
            if (onLikeListener != null) {
                onLikeListener.onClick(data, holder.getAdapterPosition());
            }
        });
        holder.click(R.id.nickname, () -> {
            if (onOpenSpaceListener != null) {
                onOpenSpaceListener.onClick(data, holder.getAdapterPosition());
            }
        });
        holder.click(R.id.head, () -> {
            if (onOpenSpaceListener != null) {
                onOpenSpaceListener.onClick(data, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.size() == 0) {
            super.onBindViewHolder(holder, position, payloads);
        } else {
            MainPost post = (MainPost) payloads.get(0);
            holder.getImage(R.id.like).setImageResource(post.isLike() ? R.drawable.ic_like_true : R.drawable.ic_like_false);
            holder.setText(R.id.like_count, String.valueOf(post.getLikeCount()));
        }
    }

    public void setOnLikeListener(OnItemClickListener<MainPost> onLikeListener) {
        this.onLikeListener = onLikeListener;
    }

    public void setOnOpenSpaceListener(OnItemClickListener<MainPost> onOpenSpaceListener) {
        this.onOpenSpaceListener = onOpenSpaceListener;
    }
}
