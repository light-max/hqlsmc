package com.hql.smc.ui.review.list;

import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hql.smc.R;
import com.hql.smc.base.call.Base;
import com.hql.smc.base.mvp.BaseView;

public class ReviewListView extends BaseView<ReviewListActivity> {
    private RecyclerView recycler;
    private SwipeRefreshLayout swipe;

    @Override
    public void onCreate(Base base, Bundle savedInstanceState) {
        super.onCreate(base, savedInstanceState);
        recycler = get(R.id.recycler);
        swipe = get(R.id.swipe);
    }

    public RecyclerView getRecycler() {
        return recycler;
    }

    public SwipeRefreshLayout getSwipe() {
        return swipe;
    }
}
