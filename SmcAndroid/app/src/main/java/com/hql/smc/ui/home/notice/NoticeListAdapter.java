package com.hql.smc.ui.home.notice;

import android.view.View;

import com.hql.smc.R;
import com.hql.smc.base.recycler.SimpleSingleItemRecyclerAdapter;
import com.hql.smc.data.result.Notice;

import java.util.List;

class NoticeListAdapter extends SimpleSingleItemRecyclerAdapter<Notice> {
    public NoticeListAdapter(List<Notice> data) {
        super(data);
    }

    @Override
    protected int getItemViewLayout() {
        return R.layout.item_notice;
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, Notice data, int position) {
        holder.setText(R.id.title, data.getTitle())
                .setText(R.id.content, data.getDes());
        holder.get(R.id.top).setVisibility(data.getGrade() > 0 ? View.VISIBLE : View.GONE);
    }
}
