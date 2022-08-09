package com.hql.smc.ui.review.list;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.hql.smc.R;
import com.hql.smc.base.activity.BaseActivity;
import com.hql.smc.base.recycler.OnLoadMoreListener;
import com.hql.smc.ui.review.details.ReviewDetailsActivity;
import com.hql.smc.ui.space.SpaceActivity;

public class ReviewListActivity extends BaseActivity<ReviewListModel, ReviewListView> {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_review_list;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideActionBar();
        // 设置适配器
        ReviewListAdapter adapter = new ReviewListAdapter(getModel().getList());
        getView().getRecycler().setAdapter(adapter);
        adapter.setOnOpenSpaceListener((data, position) -> {
            SpaceActivity.start(this, data.getUserId());
        });
        adapter.setOnItemClickListener((data, position) -> {
            ReviewDetailsActivity.start(this, data.getId());
        });
        // 设置上拉加载
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
        // 初始化加载数据
        getModel().load()
                .success(data -> {
                    if (data.getPager().getSize() > 0) {
                        adapter.notifyDataSetChanged();
                    }
                }).run();
        // 下拉刷新
        getView().getSwipe().setOnRefreshListener(() -> {
            getView().getSwipe().setRefreshing(false);
            map("page", 1);
            getModel().getList().clear();
            getModel().load()
                    .success(data -> {
                        if (data.getPager().getSize() > 0) {
                            adapter.notifyDataSetChanged();
                        }
                    }).run();
        });
    }
}
