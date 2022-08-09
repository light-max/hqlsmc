package com.hql.smc.ui.notice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.hql.smc.R;
import com.hql.smc.api.Api;
import com.hql.smc.base.activity.BaseActivity;

public class NoticeActivity extends BaseActivity<NoticeModel, NoticeView> {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_notice;
    }

    public static void start(Context context, int id) {
        Intent intent = new Intent(context, NoticeActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideActionBar();
        click(R.id.back, this::finish);
        click(getView().getIsee(), this::finish);
        Api.getNoticeDetails(getIntent().getIntExtra("id", 0))
                .error((message, e) -> toast(message))
                .success(data -> getView().setNotice(data))
                .run();
    }
}
