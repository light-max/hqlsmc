package com.hql.smc.ui.space.follower;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hql.smc.R;
import com.hql.smc.api.Api;
import com.hql.smc.base.fragment.BaseFragment;
import com.hql.smc.base.recycler.OnLoadMoreListener;
import com.hql.smc.ui.space.OnGetUserIdListener;
import com.hql.smc.ui.space.SpaceActivity;
import com.hql.smc.ui.space.comm.FollowUserListAdapter;

public class FollowerFragment extends BaseFragment<FollowerModel, FollowerView> {
    private OnGetUserIdListener onGetUserIdListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_space_follow_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 设置适配器
        FollowUserListAdapter adapter = new FollowUserListAdapter(getModel().getList());
        getV().getRecycler().setAdapter(adapter);
        adapter.setOnItemClickListener((data, position) -> {
            SpaceActivity.start(requireContext(), data.getTargetId());
        });
        adapter.setOnFollowListener((data, position) -> {
            Api.followToggle(data.getTargetId())
                    .error((message, e) -> toast(message))
                    .success(state -> {
                        getModel().getList().get(position).setState(state);
                        adapter.notifyItemChanged(position, state);
                    }).run();
        });
        // 初始化加载数据
        getModel().load(onGetUserIdListener.getUserId())
                .success(data -> {
                    if (data.getPager().getSize() > 0) {
                        adapter.notifyDataSetChanged();
                    }
                }).run();
        // 上拉加载更多
        getV().getRecycler().addOnScrollListener(new OnLoadMoreListener(listener -> {
            getModel().load(onGetUserIdListener.getUserId())
                    .before(() -> listener.setLoadMoreIng(true))
                    .after(() -> listener.setLoadMoreIng(false))
                    .success(data -> {
                        if (data.getPager().getSize() > 0) {
                            adapter.notifyDataSetChanged();
                        }
                    }).run();
        }));
    }

    public void setOnGetUserIdListener(OnGetUserIdListener onGetUserIdListener) {
        this.onGetUserIdListener = onGetUserIdListener;
    }
}
