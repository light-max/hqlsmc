package com.hql.smc.ui.login;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.hql.smc.R;
import com.hql.smc.base.call.Base;
import com.hql.smc.base.mvp.BaseView;

public class LoginView extends BaseView<LoginActivity> {
    private EditText username;
    private EditText password;
    private CheckBox remember;
    private CheckBox auto;
    private TextView post;
    private AlertDialog dialog;
    private TextView register;

    @Override
    public void onCreate(Base base, Bundle savedInstanceState) {
        super.onCreate(base, savedInstanceState);
        username = get(R.id.username);
        password = get(R.id.password);
        remember = get(R.id.remember);
        auto = get(R.id.auto);
        post = get(R.id.post);
        register = get(R.id.register);

        SharedPreferences sp = this.base.getPreferences(0);
        if (sp.getBoolean("remember", false)) {
            remember.setChecked(true);
            username.setText(sp.getString("username", ""));
            password.setText(sp.getString("password", ""));
            auto.setChecked(sp.getBoolean("auto", false));
        }

        auto.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                remember.setChecked(true);
            }
        });
        remember.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked) {
                auto.setChecked(false);
            }
        });
    }

    public void showDialog() {
        ProgressBar progressBar = new ProgressBar(base.getContext());
        dialog = new AlertDialog.Builder(base.getContext())
                .setCancelable(false)
                .setMessage("登录中...")
                .setView(progressBar)
                .show();
    }

    public void dismissDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    public String getUsername() {
        return username.getText().toString();
    }

    public String getPassword() {
        return password.getText().toString();
    }

    public TextView getPost() {
        return post;
    }

    public TextView getRegister() {
        return register;
    }

    public boolean isAuto() {
        return auto.isChecked();
    }

    public boolean isRemember() {
        return remember.isChecked();
    }

    public void saveStatus() {
        base.getPreferences(0).edit()
                .putBoolean("auto", isAuto())
                .putBoolean("remember", isRemember())
                .putString("username", getUsername())
                .putString("password", getPassword())
                .apply();
    }
}
