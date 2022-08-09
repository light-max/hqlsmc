package com.hql.smc.ui.postmanager;

import com.hql.smc.R;
import com.hql.smc.base.recycler.SimpleSingleItemRecyclerAdapter;
import com.hql.smc.data.result.MainPost;

import java.util.List;

class PostListAdapter extends SimpleSingleItemRecyclerAdapter<MainPost> {
    private OnItemClickListener<MainPost> onDeleteListener;

    public PostListAdapter(List<MainPost> data) {
        super(data);
    }

    @Override
    protected int getItemViewLayout() {
        return R.layout.item_post_manager_post;
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, MainPost data, int position) {
        holder.setText(R.id.title, data.getTitle())
                .setText(R.id.content, data.getContent())
                .setText(R.id.like_count, String.valueOf(data.getLikeCount()))
                .setText(R.id.reply_count, String.valueOf(data.getReplyCount()))
                .setText(R.id.image_count, String.valueOf(data.getImages().getUrls().size()));
        holder.click(R.id.delete, () -> {
            if (onDeleteListener != null) {
                onDeleteListener.onClick(data, holder.getAdapterPosition());
            }
        });
    }

    public void setOnDeleteListener(OnItemClickListener<MainPost> onDeleteListener) {
        this.onDeleteListener = onDeleteListener;
    }
}
