package com.hql.smc.ui.createpost;

import com.bumptech.glide.Glide;
import com.hql.smc.R;
import com.hql.smc.api.ExRequestBuilder;
import com.hql.smc.async.Async;
import com.hql.smc.base.recycler.SimpleSingleItemRecyclerAdapter;
import com.hql.smc.net.result.Result;

class ImageListAdapter extends SimpleSingleItemRecyclerAdapter<String> {
    private OnItemClickListener<String> onDeleteImageListener;

    @Override
    protected int getItemViewLayout() {
        return R.layout.item_draft_image;
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, String data, int position) {
        Async.<byte[]>builder().success(bytes -> {
            Glide.with(holder.itemView)
                    .load(bytes)
                    .into(holder.getImage(R.id.image));
        }).run(() -> {
            Result result = ExRequestBuilder.get(data)
                    .useStream()
                    .execute();
            return result.bytes();
        });
        holder.click(R.id.delete, () -> {
            if (onDeleteImageListener != null) {
                onDeleteImageListener.onClick(data, holder.getAdapterPosition());
            }
        });
    }

    public void setOnDeleteImageListener(OnItemClickListener<String> onDeleteImageListener) {
        this.onDeleteImageListener = onDeleteImageListener;
    }
}
