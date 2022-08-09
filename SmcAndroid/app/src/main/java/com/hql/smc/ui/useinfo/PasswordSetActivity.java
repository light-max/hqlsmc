package com.hql.smc.ui.useinfo;

import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.hql.smc.R;
import com.hql.smc.api.Api;
import com.hql.smc.base.activity.NoMvpActivity;

public class PasswordSetActivity extends NoMvpActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_password_set;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideActionBar();
        click(R.id.back, this::finish);
        EditText source = get(R.id.source);
        EditText pwd0 = get(R.id.pwd0);
        EditText pwd1 = get(R.id.pwd1);
        click(R.id.post, () -> {
            String[] pwd = new String[]{
                    source.getText().toString(),
                    pwd0.getText().toString(),
                    pwd1.getText().toString()
            };
            if (pwd[1].equals(pwd[2])) {
                Api.setUserPassword(pwd[0], pwd[1])
                        .error((message, e) -> toast(message))
                        .success(() -> {
                            toast("修改成功");
                            finish();
                        }).run();
            } else {
                toast("两次输入的密码不一致");
            }
        });
    }
}
