package com.hql.smc.ui.mainpost;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.hql.smc.R;
import com.hql.smc.api.ExRequestBuilder;
import com.hql.smc.data.result.Images;
import com.hql.smc.utils.GlideUtils;

public class ImageListLayout extends LinearLayout {
    private Images images;
    private LayoutInflater inflater;

    public ImageListLayout(Context context) {
        this(context, null);
    }

    public ImageListLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflater = LayoutInflater.from(getContext());
        setOrientation(VERTICAL);
    }

    public void setImages(Images images) {
        this.images = images;
        updateUI();
    }

    public void updateUI() {
        removeAllViews();
        for (String url : images.getUrls()) {
            View view = inflater.inflate(R.layout.view_image_list_layout_item, this, false);
            addView(view);
            ImageView image = view.findViewById(R.id.image);
            Glide.with(this)
                    .load(ExRequestBuilder.getUrl(url))
                    .apply(GlideUtils.skipCache().override(Target.SIZE_ORIGINAL))
                    .into(image);
        }
    }
}
