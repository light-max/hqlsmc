package com.hql.smc.ui.search.post;

import com.bumptech.glide.Glide;
import com.hql.smc.R;
import com.hql.smc.api.ExRequestBuilder;
import com.hql.smc.base.recycler.SimpleSingleItemRecyclerAdapter;
import com.hql.smc.data.result.MainPost;
import com.hql.smc.utils.GlideUtils;

class PostListAdapter extends SimpleSingleItemRecyclerAdapter<MainPost> {
    private OnItemClickListener<MainPost> onOpenSpaceListener;

    @Override
    protected int getItemViewLayout() {
        return R.layout.item_search_post;
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, MainPost data, int position) {
        Glide.with(holder.itemView)
                .load(ExRequestBuilder.getUrl("/head/" + data.getUserId()))
                .apply(GlideUtils.skipCache())
                .into(holder.getImage(R.id.head));
        holder.setText(R.id.nickname, data.getNickname())
                .setText(R.id.time, data.getTime())
                .setText(R.id.title, data.getTitle())
                .setText(R.id.content, data.getContent())
                .setText(R.id.like_count, String.valueOf(data.getLikeCount()))
                .setText(R.id.reply_count, String.valueOf(data.getReplyCount()))
                .setText(R.id.image_count, String.valueOf(data.getImages().getUrls().size()));
        Runnable openSpaceRunnable = () -> {
            if (onOpenSpaceListener != null) {
                onOpenSpaceListener.onClick(data, holder.getAdapterPosition());
            }
        };
        holder.click(R.id.head, openSpaceRunnable);
        holder.click(R.id.nickname, openSpaceRunnable);
    }

    public void setOnOpenSpaceListener(OnItemClickListener<MainPost> onOpenSpaceListener) {
        this.onOpenSpaceListener = onOpenSpaceListener;
    }
}
