package com.hql.smc.ui.register;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.hql.smc.R;
import com.hql.smc.base.call.Base;
import com.hql.smc.base.mvp.BaseView;

public class RegisterView extends BaseView<RegisterActivity> {
    private EditText username;
    private EditText password;
    private EditText password1;
    private TextView post;
    private AlertDialog dialog;

    @Override
    public void onCreate(Base base, Bundle savedInstanceState) {
        super.onCreate(base, savedInstanceState);
        click(R.id.back, () -> base.getActivity().finish());
        username = get(R.id.username);
        password = get(R.id.password);
        password1 = get(R.id.password1);
        post = get(R.id.post);
    }

    public String getUsername() {
        return username.getText().toString();
    }

    public String getPassword() {
        return password.getText().toString();
    }

    public String getPassword1() {
        return password1.getText().toString();
    }

    public TextView getPost() {
        return post;
    }

    public void showDialog() {
        ProgressBar progressBar = new ProgressBar(base.getContext());
        dialog = new AlertDialog.Builder(base.getContext())
                .setCancelable(false)
                .setMessage("注册...")
                .setView(progressBar)
                .show();
    }

    public void dismissDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }
}
