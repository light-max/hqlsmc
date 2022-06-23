package com.hql.smc.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hql.smc.R;
import com.hql.smc.base.call.Base;
import com.hql.smc.base.mvp.BaseView;

import org.jetbrains.annotations.NotNull;

public class HomeView extends BaseView<HomeActivity> {
    private ViewPager2 pager;
    private BottomNavigationView nav;

    @Override
    public void onCreate(Base base, Bundle savedInstanceState) {
        super.onCreate(base, savedInstanceState);
        pager = get(R.id.pager);
        nav = get(R.id.nav);
    }


}
