package com.hql.smc.ui.review.details;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.hql.smc.R;
import com.hql.smc.api.Api;
import com.hql.smc.base.activity.NoMvpActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReviewDetailsActivity extends NoMvpActivity {
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
        return R.layout.activity_review_details;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideActionBar();
        click(R.id.back, this::finish);
        EditText des = get(R.id.des);
        TextView state = get(R.id.state);
        TextView createTime = get(R.id.create_time);
        EditText reviewMsg = get(R.id.review_msg);
        TextView reviewTime = get(R.id.review_time);
        EditText reviewMsgInput = get(R.id.review_msg_input);
        Api.getDaDetails(getIntent().getIntExtra("id", 0))
                .error((message, e) -> toast(message))
                .success(data -> {
                    des.setText(data.getDes());
                    state.setTextColor(colors[data.getState()]);
                    state.setText(texts[data.getState()]);
                    createTime.setText(format.format(new Date(data.getCreateTime())));
                    if (data.getReviewTime() != 0) {
                        get(R.id.review_view).setVisibility(View.VISIBLE);
                        get(R.id.noreview_view).setVisibility(View.GONE);
                        reviewMsg.setText(data.getReviewMsg());
                        reviewTime.setText(format.format(new Date(data.getReviewTime())));
                    } else {
                        get(R.id.review_view).setVisibility(View.VISIBLE);
                        get(R.id.noreview_view).setVisibility(View.VISIBLE);
                    }
                    click(R.id.success, () -> {
                        Api.reviewDa(data.getId(), reviewMsgInput.getText().toString(), true)
                                .error((message, e) -> toast(message))
                                .success(() -> {
                                    toast("已审核");
                                    finish();
                                })
                                .run();
                    });
                    click(R.id.fail, () -> {
                        Api.reviewDa(data.getId(), reviewMsgInput.getText().toString(), false)
                                .error((message, e) -> toast(message))
                                .success(() -> {
                                    toast("已审核");
                                    finish();
                                })
                                .run();
                    });
                }).run();
    }

    public static void start(Context context, int id) {
        Intent intent = new Intent(context, ReviewDetailsActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }
}
