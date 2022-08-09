package com.hql.smc.data.livedata;

import androidx.lifecycle.MutableLiveData;

import com.hql.smc.data.result.User;

public class UserData extends MutableLiveData<User> {
    private static UserData instance;

    public static UserData getInstance() {
        if (instance == null) {
            instance = new UserData();
        }
        return instance;
    }
}
