package com.hql.smc.base.mvp;

import android.os.Bundle;

import com.hql.smc.async.RunToMainThread;
import com.hql.smc.base.call.Base;

public class BaseModel<P extends Base> implements MvpLifecycle, RunToMainThread {
    protected P base;

    @Override
    public void onCreate(Base base, Bundle savedInstanceState) {
        this.base = (P) base;
    }

    @Override
    public void onStart(Base base, Bundle savedInstanceState) {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {
        this.base = null;
    }

    @Override
    public void onViewCreated(Base base, Bundle savedInstanceState) {

    }
}
