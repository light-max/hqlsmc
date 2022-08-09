package com.hql.smc.ui.da.details;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.hql.smc.R;
import com.hql.smc.api.Api;
import com.hql.smc.api.ExRequestBuilder;
import com.hql.smc.base.activity.NoMvpActivity;
import com.hql.smc.ui.space.SpaceActivity;
import com.hql.smc.utils.GlideUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DaDetailsActivity extends NoMvpActivity {
    private final int[] colors = new int[]{
            Color.GRAY,
            Color.parseColor("#FF018786"),
            Color.RED
    };
    private final String[] texts = new String[]{
            "进行中",
            "已打卡",
            "打卡失败"
    };
    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");

    @Override
    protected int getLayoutId() {
        return R.layout.activity_da_details;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideActionBar();
        click(R.id.back, this::finish);
        EditText des = get(R.id.des);
        ImageView head = get(R.id.head);
        TextView nickname = get(R.id.review_nickname);
        TextView state = get(R.id.state);
        TextView createTime = get(R.id.create_time);
        EditText reviewMsg = get(R.id.review_msg);
        TextView reviewTime = get(R.id.review_time);
        Api.getDaDetails(getIntent().getIntExtra("id", 0))
                .error((message, e) -> toast(message))
                .success(data -> {
                    des.setText(data.getDes());
                    Glide.with(this)
                            .load(ExRequestBuilder.getUrl("/head/" + data.getReviewId()))
                            .apply(GlideUtils.skipCache())
                            .into(head);
                    nickname.setText(data.getReviewNickname());
                    click(R.id.review_space, () -> {
                        SpaceActivity.start(this, data.getReviewId());
                    });
                    state.setText(texts[data.getState()]);
                    state.setTextColor(colors[data.getState()]);
                    createTime.setText(format.format(new Date(data.getCreateTime())));
                    if (data.getReviewTime() != 0) {
                        get(R.id.review_view).setVisibility(View.VISIBLE);
                        reviewMsg.setText(data.getReviewMsg());
                        reviewTime.setText(format.format(new Date(data.getReviewTime())));
                    } else {
                        get(R.id.review_view).setVisibility(View.GONE);
                    }
                }).run();
    }

    public static void start(Context context, int id) {
        Intent intent = new Intent(context, DaDetailsActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }
}
