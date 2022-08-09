package com.hql.smc.ui.space.follow;

import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;

import com.hql.smc.R;
import com.hql.smc.base.call.Base;
import com.hql.smc.base.mvp.BaseView;

public class FollowView extends BaseView<FollowFragment> {
    private RecyclerView recycler;

    @Override
    public void onViewCreated(Base base, Bundle savedInstanceState) {
        recycler = get(R.id.recycler);
    }

    public RecyclerView getRecycler() {
        return recycler;
    }
}
