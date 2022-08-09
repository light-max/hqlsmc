package com.hql.smc.ui.register;

import com.hql.smc.api.Api;
import com.hql.smc.base.mvp.BaseModel;

public class RegisterModel extends BaseModel<RegisterActivity> {
    public void register(String username, String password) {
        Api.register(username, password)
                .before(() -> base.getView().showDialog())
                .after(() -> base.getView().dismissDialog())
                .error((message, e) -> base.toast(message))
                .success(data -> {
                    base.toast("注册成功, 快去登录吧");
                    base.finish();
                }).run();
    }
}
