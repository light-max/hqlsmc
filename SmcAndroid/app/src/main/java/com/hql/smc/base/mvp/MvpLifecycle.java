package com.hql.smc.base.mvp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import com.hql.smc.base.call.Base;

public interface MvpLifecycle {
    default void onCreate(Lifecycle lifecycle, Base base, Bundle savedInstanceState) {
        onCreate(base, savedInstanceState);
        lifecycle.addObserver(new LifecycleEventObserver() {
            @Override
            public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                switch (event) {
//                    case ON_CREATE:
//                        onCreate(base, savedInstanceState);
//                        break;
                    case ON_START:
                        onStart(base, savedInstanceState);
                        break;
                    case ON_PAUSE:
                        onPause();
                        break;
                    case ON_DESTROY:
                        lifecycle.removeObserver(this);
                        onDestroy();
                        break;
                }
            }
        });
    }

    void onCreate(Base base, Bundle savedInstanceState);

    void onStart(Base base, Bundle savedInstanceState);

    void onPause();

    void onDestroy();

    /**
     * 在{@link BaseFragment#onViewCreated(View, Bundle)}之后调用
     */
    void onViewCreated(Base base, Bundle savedInstanceState);
}
