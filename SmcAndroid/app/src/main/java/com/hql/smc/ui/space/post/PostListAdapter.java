package com.hql.smc.ui.space.post;

import com.hql.smc.R;
import com.hql.smc.base.recycler.SimpleSingleItemRecyclerAdapter;
import com.hql.smc.data.result.MainPost;

import java.util.List;

class PostListAdapter extends SimpleSingleItemRecyclerAdapter<MainPost> {
    public PostListAdapter(List<MainPost> data) {
        super(data);
    }

    @Override
    protected int getItemViewLayout() {
        return R.layout.item_space_main_post;
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, MainPost data, int position) {
        holder.setText(R.id.title, data.getTitle())
                .setText(R.id.content, data.getContent())
                .setText(R.id.like_count, String.valueOf(data.getLikeCount()))
                .setText(R.id.reply_count, String.valueOf(data.getReplyCount()))
                .setText(R.id.image_count, String.valueOf(data.getImages().getUrls().size()));
    }
}
