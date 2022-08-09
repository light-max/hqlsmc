package com.hql.smc.ui.search;

import android.os.Bundle;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.hql.smc.R;
import com.hql.smc.base.activity.NoMvpActivity;
import com.hql.smc.ui.search.post.PostSearchFragment;
import com.hql.smc.ui.search.user.UserSearchFragment;
import com.hql.smc.view.SearchView;

public class SearchActivity extends NoMvpActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideActionBar();
        click(R.id.back, this::finish);
        Fragment[] fragments = new Fragment[]{
                new PostSearchFragment(),
                new UserSearchFragment()
        };
        ViewPager2 pager = get(R.id.pager);
        pager.setAdapter(new FragmentStateAdapter(getActivity()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return fragments[position];
            }

            @Override
            public int getItemCount() {
                return fragments.length;
            }
        });
        pager.setUserInputEnabled(false);
        RadioButton post = get(R.id.post);
        RadioButton user = get(R.id.user);
        post.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                pager.setCurrentItem(0);
            }
        });
        user.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                pager.setCurrentItem(1);
            }
        });
        post.setChecked(true);
        SearchView search = get(R.id.search);
        search.setOnResetListener(() -> {
        });
        search.setOnSearchListener(text -> {
            for (Fragment fragment : fragments) {
                ((SearchView.OnSearchListener) fragment).onSearch(text);
            }
        });
        pager.setOffscreenPageLimit(2);
    }
}
