package com.hql.smc.ui.notice;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.hql.smc.R;
import com.hql.smc.base.call.Base;
import com.hql.smc.base.mvp.BaseView;
import com.hql.smc.data.result.NoticeDetails;
import com.hql.smc.ui.mainpost.ImageListLayout;

public class NoticeView extends BaseView<NoticeActivity> {
    private TextView title;
    private TextView time;
    private TextView content;
    private ImageListLayout images;
    private Button isee;

    @Override
    public void onCreate(Base base, Bundle savedInstanceState) {
        super.onCreate(base, savedInstanceState);
        title = get(R.id.title);
        time = get(R.id.time);
        content = get(R.id.content);
        images = get(R.id.images);
        isee = get(R.id.isee);
    }

    public void setNotice(NoticeDetails notice) {
        getTitle().setText(notice.getTitle());
        getTime().setText(notice.getTime());
        getContent().setText(notice.getDes());
        getImages().setImages(notice.getImages());
    }

    public TextView getTitle() {
        return title;
    }

    public TextView getTime() {
        return time;
    }

    public TextView getContent() {
        return content;
    }

    public ImageListLayout getImages() {
        return images;
    }

    public Button getIsee() {
        return isee;
    }
}
