package com.hql.smc.ui.space.comm;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.hql.smc.R;
import com.hql.smc.api.ExRequestBuilder;
import com.hql.smc.base.recycler.SimpleSingleItemRecyclerAdapter;
import com.hql.smc.data.result.Follow;
import com.hql.smc.data.result.FollowState;
import com.hql.smc.utils.GlideUtils;

import java.util.List;

public class FollowUserListAdapter extends SimpleSingleItemRecyclerAdapter<Follow> {
    private OnItemClickListener<Follow> onFollowListener;

    public FollowUserListAdapter(List<Follow> data) {
        super(data);
    }

    @Override
    protected int getItemViewLayout() {
        return R.layout.item_space_follow_user;
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, Follow data, int position) {
        Glide.with(holder.itemView)
                .load(ExRequestBuilder.getUrl("/head/" + data.getTargetId()))
                .apply(GlideUtils.skipCache())
                .into(holder.getImage(R.id.head));
        holder.setText(R.id.nickname, data.getNickname())
                .setText(R.id.des, data.getDes());
        FollowState state = data.getState();
        if (state.isFriend()) {
            holder.setText(R.id.follow, "互相关注");
        } else {
            if (state.isFollow()) {
                holder.setText(R.id.follow, "已关注");
            } else {
                holder.setText(R.id.follow, "关注");
            }
        }
        holder.click(R.id.follow, () -> {
            if (onFollowListener != null) {
                onFollowListener.onClick(data, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.size() > 0) {
            FollowState state = (FollowState) payloads.get(0);
            if (state.isFriend()) {
                holder.setText(R.id.follow, "互相关注");
            } else {
                if (state.isFollow()) {
                    holder.setText(R.id.follow, "已关注");
                } else {
                    holder.setText(R.id.follow, "关注");
                }
            }
        } else {
            super.onBindViewHolder(holder, position, payloads);
        }
    }

    public void setOnFollowListener(OnItemClickListener<Follow> onFollowListener) {
        this.onFollowListener = onFollowListener;
    }
}
