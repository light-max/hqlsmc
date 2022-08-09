package com.hql.smc.ui.postmanager;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.hql.smc.R;
import com.hql.smc.api.Api;
import com.hql.smc.base.activity.BaseActivity;
import com.hql.smc.base.recycler.OnLoadMoreListener;
import com.hql.smc.ui.mainpost.MainpostActivity;

public class PostManagerActivity extends BaseActivity<PostManagerModel, PostManagerView> {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_post_manager;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideActionBar();
        // 设置适配器
        PostListAdapter adapter = new PostListAdapter(getModel().getList());
        getView().getRecycler().setAdapter(adapter);
        adapter.setOnItemClickListener((data, position) -> {
            MainpostActivity.start(this, data.getId());
        });
        adapter.setOnDeleteListener((data, position) -> {
            new AlertDialog.Builder(this)
                    .setMessage("你确定要删除这条帖子吗")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", (dialog, which) -> {
                        Api.deleteMainPost(data.getId())
                                .error((message, e) -> toast(message))
                                .success(() -> adapter.notifyItemRemoved(position))
                                .run();
                    }).show();
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
        // 初始化加载数据
        getModel().load().success(data -> {
            if (data.getPager().getSize() > 0) {
                adapter.notifyDataSetChanged();
            }
        }).run();
    }
}
