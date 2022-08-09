package com.hql.smc.ui.da.list;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.widget.TextView;

import com.hql.smc.R;
import com.hql.smc.base.recycler.SimpleSingleItemRecyclerAdapter;
import com.hql.smc.data.result.Da;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

class DaListAdapter extends SimpleSingleItemRecyclerAdapter<Da> {
    private final int[] colors;
    private final String[] texts;
    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");

    public DaListAdapter(List<Da> data) {
        super(data);
        colors = new int[]{
                Color.GRAY,
                Color.parseColor("#FF018786"),
                Color.RED
        };
        texts = new String[]{
                "进行中",
                "已打卡",
                "打卡失败"
        };
    }

    @Override
    protected int getItemViewLayout() {
        return R.layout.item_da_list_da;
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, Da data, int position) {
        TextView state = holder.getText(R.id.state);
        state.setTextColor(colors[data.getState()]);
        state.setText(texts[data.getState()]);
        holder.setText(R.id.date, format.format(new Date(data.getCreateTime())));
    }
}
