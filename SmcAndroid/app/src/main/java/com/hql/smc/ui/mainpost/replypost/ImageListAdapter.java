package com.hql.smc.ui.mainpost.replypost;

import com.bumptech.glide.Glide;
import com.hql.smc.R;
import com.hql.smc.base.recycler.SimpleSingleItemRecyclerAdapter;

import java.io.File;
import java.util.ArrayList;

public class ImageListAdapter extends SimpleSingleItemRecyclerAdapter<File> {
    public ImageListAdapter() {
        super(new ArrayList<>());
    }

    @Override
    protected int getItemViewLayout() {
        return R.layout.item_reply_post_image;
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, File data, int position) {
        Glide.with(holder.itemView)
                .load(data)
                .into(holder.getImage(R.id.image));
        holder.click(R.id.delete, () -> {
            getData().remove(data);
            notifyItemRemoved(holder.getAdapterPosition());
        });
    }
}
