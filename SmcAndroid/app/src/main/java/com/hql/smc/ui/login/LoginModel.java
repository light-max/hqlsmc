package com.hql.smc.ui.login;

import com.hql.smc.api.Api;
import com.hql.smc.async.Async;
import com.hql.smc.base.mvp.BaseModel;
import com.hql.smc.ui.home.HomeActivity;

public class LoginModel extends BaseModel<LoginActivity> {
    public void login(boolean isStudent, String username, String password) {
        Async.Builder<?> async;
        if (isStudent) {
            async = null;
        } else {
            async = null;
        }
        async.before(() -> base.getView().showDialog())
                .after(() -> base.getView().dismissDialog())
                .error(((message, e) -> base.toast(message)))
                .success(() -> {
                    base.getView().saveStatus();
                    base.toast("登录成功");
                    base.finish();
                    base.open(HomeActivity.class);
                })
                .run();
    }
}
