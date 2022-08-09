package com.hql.smc.ui.space;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.hql.smc.R;
import com.hql.smc.api.ExRequestBuilder;
import com.hql.smc.base.call.Base;
import com.hql.smc.base.mvp.BaseView;
import com.hql.smc.data.result.User;
import com.hql.smc.ui.space.follow.FollowFragment;
import com.hql.smc.ui.space.follower.FollowerFragment;
import com.hql.smc.ui.space.post.PostListFragment;
import com.hql.smc.utils.GlideUtils;

import java.lang.reflect.Method;

public class SpaceView extends BaseView<SpaceActivity> {
    private ImageView head;
    private TextView nickname;
    private TextView username;
    private TextView des;
    private TextView follow;
    private RadioButton[] buttons;
    private RadioGroup group;
    private ViewPager2 pager;
    private Fragment[] fragments;
    private SwipeRefreshLayout swipe;

    @Override
    public void onCreate(Base base, Bundle savedInstanceState) {
        super.onCreate(base, savedInstanceState);
        head = get(R.id.head);
        nickname = get(R.id.nickname);
        username = get(R.id.username);
        des = get(R.id.des);
        follow = get(R.id.follow);
        buttons = new RadioButton[]{
                get(R.id.lab_post),
                get(R.id.lab_follow),
                get(R.id.lab_follower),
        };
        group = get(R.id.group);
        pager = get(R.id.pager);
        swipe = get(R.id.swipe);

        refreshFragment();
        pager.setUserInputEnabled(false);
        pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                buttons[position].setChecked(true);
            }
        });
        for (int i = 0; i < buttons.length; i++) {
            int finalI = i;
            buttons[i].setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    pager.setCurrentItem(finalI);
                }
            });
        }
        buttons[0].setChecked(true);
    }

    public void refreshFragment() {
        fragments = new Fragment[]{
                new PostListFragment(),
                new FollowFragment(),
                new FollowerFragment()
        };
        for (Fragment fragment : fragments) {
            Class<? extends Fragment> aClass = fragment.getClass();
            try {
                Method method = aClass.getDeclaredMethod("setOnGetUserIdListener", OnGetUserIdListener.class);
                method.invoke(fragment, this.base);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        pager.setAdapter(new FragmentStateAdapter(base.getActivity()) {
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
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void setUser(User user) {
        Glide.with(base.getActivity())
                .load(ExRequestBuilder.getUrl("/head/" + user.getId()))
                .apply(GlideUtils.skipCache())
                .into(head);
        nickname.setText(user.getNickname());
        username.setText(user.getUsername());
        if (user.getDes().trim().length() > 0) {
            des.setVisibility(View.VISIBLE);
            des.setText(user.getDes());
        } else {
            des.setVisibility(View.GONE);
        }
        Drawable drawable = base.getContext().getDrawable(user.getGender() == 1 ? R.mipmap.male : R.mipmap.female);
        nickname.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, drawable, null);
    }

    public ImageView getHead() {
        return head;
    }

    public TextView getNickname() {
        return nickname;
    }

    public TextView getUsername() {
        return username;
    }

    public TextView getDes() {
        return des;
    }

    public TextView getFollow() {
        return follow;
    }

    public RadioButton[] getButtons() {
        return buttons;
    }

    public RadioGroup getGroup() {
        return group;
    }

    public SwipeRefreshLayout getSwipe() {
        return swipe;
    }
}
