package com.hql.smc.ui.postmanager;

import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;

import com.hql.smc.R;
import com.hql.smc.base.call.Base;
import com.hql.smc.base.mvp.BaseView;

public class PostManagerView extends BaseView<PostManagerActivity> {
    private RecyclerView recycler;

    @Override
    public void onCreate(Base base, Bundle savedInstanceState) {
        super.onCreate(base, savedInstanceState);
        recycler = get(R.id.recycler);
    }

    public RecyclerView getRecycler() {
        return recycler;
    }
}
