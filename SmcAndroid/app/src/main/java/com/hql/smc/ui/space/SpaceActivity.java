package com.hql.smc.ui.space;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.hql.smc.R;
import com.hql.smc.api.Api;
import com.hql.smc.async.Call;
import com.hql.smc.base.activity.BaseActivity;
import com.hql.smc.data.result.FollowState;

public class SpaceActivity extends BaseActivity<SpaceModel, SpaceView> implements OnGetUserIdListener {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_space;
    }

    public static void start(Context context, int userId) {
        Intent intent = new Intent(context, SpaceActivity.class);
        intent.putExtra("id", userId);
        context.startActivity(intent);
    }

    @Override
    public int getUserId() {
        return getIntent().getIntExtra("id", 0);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideActionBar();
        click(R.id.back, this::finish);
        loadUser();
        setFollow();
        setNumbers();
        getView().getSwipe().setOnRefreshListener(() -> {
            loadUser();
            setFollow();
            setNumbers();
            getView().refreshFragment();
            getView().getSwipe().setRefreshing(false);
        });
    }

    private void loadUser() {
        Api.getUser(getUserId())
                .error((message, e) -> toast(message))
                .success(data -> getView().setUser(data))
                .run();
    }

    private void setFollow() {
        TextView follow = getView().getFollow();
        Call.OnReturnData<FollowState> success = data -> {
            if (data.isFriend()) {
                follow.setText("互相关注");
            } else {
                if (data.isFollow()) {
                    follow.setText("取消关注");
                } else {
                    follow.setText("关注");
                }
            }
        };
        Api.followCheck(getUserId())
                .success(success)
                .run();
        click(follow, () -> {
            Api.followToggle(getUserId())
                    .error((message, e) -> toast(message))
                    .success(success)
                    .run();
        });
    }

    @SuppressLint("SetTextI18n")
    private void setNumbers() {
        Api.getSpaceValues(getUserId())
                .success(data -> {
                    RadioButton[] buttons = getView().getButtons();
                    buttons[0].setText("帖子" + data.getPostCount());
                    buttons[1].setText("关注" + data.getFollowCount());
                    buttons[2].setText("粉丝" + data.getFollowerCount());
                }).run();
    }
}
