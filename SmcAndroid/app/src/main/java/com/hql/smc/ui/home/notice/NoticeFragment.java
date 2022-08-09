package com.hql.smc.ui.home.notice;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hql.smc.R;
import com.hql.smc.base.fragment.BaseFragment;
import com.hql.smc.base.recycler.OnLoadMoreListener;
import com.hql.smc.ui.notice.NoticeActivity;

public class NoticeFragment extends BaseFragment<NoticeModel, NoticeView> {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notice, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 设置适配器
        NoticeListAdapter adapter = new NoticeListAdapter(getModel().getList());
        getV().getRecycler().setAdapter(adapter);
        adapter.setOnItemClickListener((data, position) -> {
            NoticeActivity.start(requireContext(), data.getId());
        });
        // 设置上拉加载更多
        getV().getRecycler().addOnScrollListener(new OnLoadMoreListener(listener -> {
            getModel().load().before(() -> listener.setLoadMoreIng(true))
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
