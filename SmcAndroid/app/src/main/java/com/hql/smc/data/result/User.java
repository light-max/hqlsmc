package com.hql.smc.data.result;

public class User {
    private int id;
    private String username;
    private String password;
    private String nickname;
    private int gender;
    private String birthday;
    private String email;
    private String des;
    private long createTime;
    private boolean enable;

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }

    public int getGender() {
        return gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getEmail() {
        return email;
    }

    public String getDes() {
        return des;
    }

    public long getCreateTime() {
        return createTime;
    }

    public boolean isEnable() {
        return enable;
    }
}
