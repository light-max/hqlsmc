package com.hql.smc.ui.login;

import com.hql.smc.api.Api;
import com.hql.smc.base.mvp.BaseModel;
import com.hql.smc.data.livedata.UserData;
import com.hql.smc.ui.home.HomeActivity;

public class LoginModel extends BaseModel<LoginActivity> {
    public void login(String username, String password) {
        Api.login(username, password)
                .before(() -> base.getView().showDialog())
                .after(() -> base.getView().dismissDialog())
                .error(((message, e) -> base.toast(message)))
                .success((data) -> {
                    base.getView().saveStatus();
                    base.toast("登录成功");
                    base.finish();
                    base.open(HomeActivity.class);
                    UserData.getInstance().postValue(data);
                })
                .run();
    }
}
