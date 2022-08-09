package com.hql.smc.ui.search.user;

import com.bumptech.glide.Glide;
import com.hql.smc.R;
import com.hql.smc.api.ExRequestBuilder;
import com.hql.smc.base.recycler.SimpleSingleItemRecyclerAdapter;
import com.hql.smc.data.result.User;
import com.hql.smc.utils.GlideUtils;

class UserListAdapter extends SimpleSingleItemRecyclerAdapter<User> {
    @Override
    protected int getItemViewLayout() {
        return R.layout.item_search_user;
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, User data, int position) {
        Glide.with(holder.itemView)
                .load(ExRequestBuilder.getUrl("/head/" + data.getId()))
                .apply(GlideUtils.skipCache())
                .into(holder.getImage(R.id.head));
        holder.setText(R.id.nickname, data.getNickname())
                .setText(R.id.des, data.getDes());
    }
}
