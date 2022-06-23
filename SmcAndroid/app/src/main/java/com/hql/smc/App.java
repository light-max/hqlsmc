package com.hql.smc;

import android.app.Application;

import com.hql.smc.utils.FileManager;


public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FileManager.applicationContext = getApplicationContext();
    }
}
