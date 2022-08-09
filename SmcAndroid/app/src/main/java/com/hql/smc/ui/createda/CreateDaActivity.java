package com.hql.smc.ui.createda;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.hql.smc.R;
import com.hql.smc.api.Api;
import com.hql.smc.base.activity.BaseActivity;
import com.hql.smc.base.recycler.OnLoadMoreListener;
import com.hql.smc.data.result.Follow;
import com.hql.smc.ui.space.SpaceActivity;

public class CreateDaActivity extends BaseActivity<CreateDaModel, CreateDaView> {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_create_da;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideActionBar();
        click(R.id.back, this::finish);
        click(R.id.submit, this::submit);
        // 初始化适配器
        FollowListAdapter adapter = new FollowListAdapter(getModel().getList());
        getView().getRecycler().setAdapter(adapter);
        adapter.setOnSelectListener((data, position) -> {
            selectUser(data);
        });
        adapter.setOnItemClickListener((data, position) -> {
            SpaceActivity.start(this, data.getTargetId());
        });
        // 上拉加载更多
        getView().getRecycler().addOnScrollListener(new OnLoadMoreListener(listener -> {
            getModel().load()
                    .before(() -> listener.setLoadMoreIng(true))
                    .after(() -> listener.setLoadMoreIng(false))
                    .success(data -> {
                        if (data.getPager().getSize() > 0) {
                            adapter.notifyDataSetChanged();
                        }
                    }).run();
        }));
        // 初始化数据
        getModel().load().success(data -> {
            if (data.getPager().getSize() > 0) {
                adapter.notifyDataSetChanged();
            }
        }).run();
    }

    private void submit() {
        Integer reviewId = map("reviewId");
        if (reviewId != null) {
            Api.submitDa(reviewId, getView().getDes().getText().toString())
                    .error((message, e) -> toast(message))
                    .success(data -> {
                        toast("打卡成功，等待审核");
                        finish();
                    }).run();
        } else {
            toast("请选择监督人");
        }
    }

    private void selectUser(Follow follow) {
        map("reviewId", follow.getTargetId());
        getView().getNickname().setText(follow.getNickname());
    }
}
