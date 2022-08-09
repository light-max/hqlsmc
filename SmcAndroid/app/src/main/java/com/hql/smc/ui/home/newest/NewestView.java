package com.hql.smc.ui.home.newest;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hql.smc.R;
import com.hql.smc.base.call.Base;
import com.hql.smc.base.mvp.BaseView;

public class NewestView extends BaseView<NewestFragment> {
    private RecyclerView recycler;
    private SwipeRefreshLayout refresh;
    private LinearLayout search;

    @Override
    public void onViewCreated(Base base, Bundle savedInstanceState) {
        super.onViewCreated(base, savedInstanceState);
        recycler = get(R.id.recycler);
        refresh = get(R.id.swipe);
        search = get(R.id.search);
    }

    public RecyclerView getRecycler() {
        return recycler;
    }

    public SwipeRefreshLayout getRefresh() {
        return refresh;
    }

    public LinearLayout getSearch() {
        return search;
    }
}
