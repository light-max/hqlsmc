package com.hql.smc.ui.register;

import com.hql.smc.api.Api;
import com.hql.smc.async.Async;
import com.hql.smc.base.mvp.BaseModel;

public class RegisterModel extends BaseModel<RegisterActivity> {
    public void login(boolean isStudent, String uid, String name, String password) {
        Async.Builder<?> async;
        if (isStudent) {
            async = null;
        } else {
            async = null;
        }
        async.before(() -> base.getView().showDialog())
                .after(() -> base.getView().dismissDialog())
                .error((message, e) -> base.toast(message))
                .success(data -> {
                    base.toast("注册成功, 快去登录吧");
                    base.finish();
                }).run();
    }
}
