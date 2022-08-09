package com.hql.smc.ui.createda;

import com.bumptech.glide.Glide;
import com.hql.smc.R;
import com.hql.smc.api.ExRequestBuilder;
import com.hql.smc.base.recycler.SimpleSingleItemRecyclerAdapter;
import com.hql.smc.data.result.Follow;
import com.hql.smc.utils.GlideUtils;

import java.util.List;

class FollowListAdapter extends SimpleSingleItemRecyclerAdapter<Follow> {
    private OnItemClickListener<Follow> onSelectListener;

    public FollowListAdapter(List<Follow> data) {
        super(data);
    }

    @Override
    protected int getItemViewLayout() {
        return R.layout.item_create_da_user;
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, Follow data, int position) {
        Glide.with(holder.itemView)
                .load(ExRequestBuilder.getUrl("/head/" + data.getTargetId()))
                .apply(GlideUtils.skipCache())
                .into(holder.getImage(R.id.head));
        holder.setText(R.id.nickname, data.getNickname())
                .setText(R.id.des, data.getDes());
        holder.click(R.id.select, () -> {
            if (onSelectListener != null) {
                onSelectListener.onClick(data, holder.getAdapterPosition());
            }
        });
    }

    public void setOnSelectListener(OnItemClickListener<Follow> onSelectListener) {
        this.onSelectListener = onSelectListener;
    }
}
