package com.hql.smc;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.hql.smc.base.activity.NoMvpActivity;
import com.hql.smc.ui.login.LoginActivity;

public class MainActivity extends NoMvpActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideActionBar();
        hideStatusBar();
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra(LoginActivity.auto, true);
            startActivity(intent);
            finish();
        }, 1000);
    }
}