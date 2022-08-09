package com.hql.smc.ui.mainpost.reply;

import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hql.smc.R;
import com.hql.smc.api.ExRequestBuilder;
import com.hql.smc.base.recycler.SimpleSingleItemRecyclerAdapter;
import com.hql.smc.data.result.Reply;
import com.hql.smc.utils.GlideUtils;

import java.util.List;

public class ReplyListAdapter extends SimpleSingleItemRecyclerAdapter<Reply> {
    private OnItemClickListener<Reply> onReplyListener;
    private OnItemClickListener<Reply> onDeleteListener;
    private OnItemClickListener<Integer> onOpenSpaceListener;

    public ReplyListAdapter(List<Reply> data) {
        super(data);
    }

    @Override
    protected int getItemViewLayout() {
        return R.layout.item_reply;
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, Reply data, int position) {
        Glide.with(holder.itemView)
                .load(ExRequestBuilder.getUrl("/head/" + data.getUserId()))
                .apply(GlideUtils.skipCache())
                .into(holder.getImage(R.id.head));
        holder.setText(R.id.nickname, data.getNickname())
                .setText(R.id.time, data.getTime())
                .setText(R.id.content, data.getContent());
        TextView targetName = holder.getText(R.id.target_name);
        if (data.getTargetId() != 0) {
            targetName.setVisibility(View.VISIBLE);
            targetName.setText(data.getTargetNickname());
        } else {
            targetName.setVisibility(View.GONE);
        }
        holder.get(R.id.delete).setVisibility(data.isMe() ? View.VISIBLE : View.GONE);
        holder.click(R.id.reply, () -> {
            if (onReplyListener != null) {
                onReplyListener.onClick(data, holder.getAdapterPosition());
            }
        });
        holder.click(R.id.delete, () -> {
            if (onDeleteListener != null) {
                onDeleteListener.onClick(data, holder.getAdapterPosition());
            }
        });
        Runnable openSpaceRunnable = () -> {
            if (onOpenSpaceListener != null) {
                onOpenSpaceListener.onClick(data.getUserId(), holder.getAdapterPosition());
            }
        };
        holder.click(R.id.head, openSpaceRunnable);
        holder.click(R.id.nickname, openSpaceRunnable);
        holder.click(R.id.target_name, () -> {
            if (onOpenSpaceListener != null) {
                onOpenSpaceListener.onClick(data.getTargetId(), holder.getAdapterPosition());
            }
        });
    }

    public void setOnDeleteListener(OnItemClickListener<Reply> onDeleteListener) {
        this.onDeleteListener = onDeleteListener;
    }

    public void setOnReplyListener(OnItemClickListener<Reply> onReplyListener) {
        this.onReplyListener = onReplyListener;
    }

    public void setOnOpenSpaceListener(OnItemClickListener<Integer> onOpenSpaceListener) {
        this.onOpenSpaceListener = onOpenSpaceListener;
    }
}
