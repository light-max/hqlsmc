package com.hql.smc.ui.home;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.hql.smc.R;
import com.hql.smc.base.call.Base;
import com.hql.smc.base.mvp.BaseView;
import com.hql.smc.ui.createda.CreateDaActivity;
import com.hql.smc.ui.createpost.CreatePostActivity;
import com.hql.smc.ui.home.mine.MineFragment;
import com.hql.smc.ui.home.newest.NewestFragment;
import com.hql.smc.ui.home.notice.NoticeFragment;
import com.hql.smc.ui.home.space.SpaceFragment;

public class HomeView extends BaseView<HomeActivity> {
    private Fragment currentFragment;
    private Fragment[] fragments;
    private RadioButton[] buttons;

    @Override
    public void onCreate(Base base, Bundle savedInstanceState) {
        super.onCreate(base, savedInstanceState);
        fragments = new Fragment[]{
                new NewestFragment(),
                new SpaceFragment(),
                new NoticeFragment(),
                new MineFragment()
        };
        buttons = new RadioButton[]{
                get(R.id.newest),
                get(R.id.space),
                get(R.id.notice),
                get(R.id.mine),
        };
        for (int i = 0; i < buttons.length; i++) {
            int finalI = i;
            buttons[i].setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    replaceFragment(fragments[finalI]);
                }
            });
        }
        buttons[0].setChecked(true);
        base.click(R.id.add, () -> {
            View view = View.inflate(base.getContext(), R.layout.view_create_select, null);
            AlertDialog dialog = new AlertDialog.Builder(base.getContext())
                    .setView(view)
                    .show();
            click(view.findViewById(R.id.post),()->{
                this.base.open(CreatePostActivity.class);
                dialog.dismiss();
            });
            click(view.findViewById(R.id.da),()->{
                this.base.open(CreateDaActivity.class);
                dialog.dismiss();
            });
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = base.getActivity()
                .getSupportFragmentManager()
                .beginTransaction();
        if (currentFragment != null && currentFragment != fragment && currentFragment.isAdded()) {
            transaction.hide(currentFragment);
        }
        if (fragment.isAdded()) {
            transaction.show(fragment);
        } else {
            transaction.add(R.id.container, fragment);
        }
        transaction.commit();
        currentFragment = fragment;
    }
}
