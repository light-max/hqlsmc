package com.hql.smc.ui.login;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.hql.smc.R;
import com.hql.smc.base.activity.BaseActivity;
import com.hql.smc.ui.register.RegisterActivity;

public class LoginActivity extends BaseActivity<LoginModel, LoginView> {
    public static final String auto = "auto";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideActionBar();
        hideStatusBar();
        view.getPost().setOnClickListener(v -> {
            model.login(view.getUsername(), view.getPassword());
        });
        view.getRegister().setOnClickListener(v -> {
            open(RegisterActivity.class);
        });
        boolean auto = getIntent().getBooleanExtra(LoginActivity.auto, false);
        if (auto && view.isAuto()) {
            view.getPost().callOnClick();
        }
    }
}
