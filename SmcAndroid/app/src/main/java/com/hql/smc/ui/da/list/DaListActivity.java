package com.hql.smc.ui.da.list;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.hql.smc.R;
import com.hql.smc.base.activity.BaseActivity;
import com.hql.smc.base.recycler.OnLoadMoreListener;
import com.hql.smc.ui.da.details.DaDetailsActivity;

public class DaListActivity extends BaseActivity<DaListModel, DaListView> {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_da_list;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideActionBar();
        // 设置适配器
        DaListAdapter adapter = new DaListAdapter(getModel().getList());
        getView().getRecycler().setAdapter(adapter);
        adapter.setOnItemClickListener((data, position) -> {
            DaDetailsActivity.start(this, data.getId());
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
        getModel().load()
                .success(data -> {
                    if (data.getPager().getSize() > 0) {
                        adapter.notifyDataSetChanged();
                    }
                }).run();
    }
}
