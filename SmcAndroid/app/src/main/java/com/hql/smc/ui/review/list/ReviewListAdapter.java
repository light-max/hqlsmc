package com.hql.smc.ui.review.list;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hql.smc.R;
import com.hql.smc.api.ExRequestBuilder;
import com.hql.smc.base.recycler.SimpleSingleItemRecyclerAdapter;
import com.hql.smc.data.result.Da;
import com.hql.smc.utils.GlideUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReviewListAdapter extends SimpleSingleItemRecyclerAdapter<Da> {
    private OnItemClickListener<Da> onOpenSpaceListener;
    private final int[] colors;
    private final String[] texts;
    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");

    public ReviewListAdapter(List<Da> data) {
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
        return R.layout.item_review_list_review;
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, Da data, int position) {
        Glide.with(holder.itemView)
                .load(ExRequestBuilder.getUrl("/head/" + data.getUserId()))
                .apply(GlideUtils.skipCache())
                .into(holder.getImage(R.id.head));
        TextView state = holder.getText(R.id.state);
        state.setText(texts[data.getState()]);
        state.setTextColor(colors[data.getState()]);
        holder.setText(R.id.create_time, format.format(new Date(data.getCreateTime())));
        holder.click(R.id.head, () -> {
            if (onOpenSpaceListener != null) {
                onOpenSpaceListener.onClick(data, holder.getAdapterPosition());
            }
        });
    }

    public void setOnOpenSpaceListener(OnItemClickListener<Da> onOpenSpaceListener) {
        this.onOpenSpaceListener = onOpenSpaceListener;
    }
}
