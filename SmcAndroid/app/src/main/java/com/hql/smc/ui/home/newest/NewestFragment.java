package com.hql.smc.ui.home.newest;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hql.smc.R;
import com.hql.smc.api.Api;
import com.hql.smc.async.Call;
import com.hql.smc.base.fragment.BaseFragment;
import com.hql.smc.base.recycler.OnLoadMoreListener;
import com.hql.smc.data.PagerData;
import com.hql.smc.data.result.MainPost;
import com.hql.smc.ui.home.comm.MainPostAdapter;
import com.hql.smc.ui.mainpost.MainpostActivity;
import com.hql.smc.ui.search.SearchActivity;
import com.hql.smc.ui.space.SpaceActivity;

public class NewestFragment extends BaseFragment<NewestModel, NewestView> {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_newest, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 设置适配器
        MainPostAdapter adapter = new MainPostAdapter(getModel().getList());
        getV().getRecycler().setAdapter(adapter);
        adapter.setOnItemClickListener((data, position) -> {
            MainpostActivity.start(requireContext(), data.getId());
        });
        adapter.setOnLikeListener((data, position) -> {
            Api.likeToggleMainPost(data.getId())
                    .error((message, e) -> toast(message))
                    .success(state -> {
                        data.setLikeCount(state.getCount());
                        data.setLike(state.isLike());
                        adapter.notifyItemChanged(position, data);
                    }).run();
        });
        adapter.setOnOpenSpaceListener((data, position) -> {
            SpaceActivity.start(requireContext(), data.getUserId());
        });
        // 更新数据的回调
        Call.OnReturnData<PagerData<MainPost>> refreshView = data -> {
            if (data.getPager().getSize() > 0) {
                adapter.notifyDataSetChanged();
            }
        };
        // 初始化加载数据
        getModel().load().success(refreshView).run();
        // 设置上拉加载更多
        getV().getRecycler().addOnScrollListener(new OnLoadMoreListener(listener -> {
            getModel().load()
                    .before(() -> listener.setLoadMoreIng(true))
                    .after(() -> listener.setLoadMoreIng(false))
                    .success(refreshView)
                    .run();
        }));
        // 下拉刷新
        getV().getRefresh().setOnRefreshListener(() -> {
            getModel().clear();
            getModel().load().after(() -> {
                getV().getRefresh().setRefreshing(false);
                adapter.notifyDataSetChanged();
            }).run();
        });
        // 搜索按钮
        click(getV().getSearch(), () -> {
            Intent intent = new Intent(requireContext(), SearchActivity.class);
            startActivity(intent);
        });
    }
}
