package com.hql.smc.data.result;

public class Follow {
    private int targetId;
    private String username;
    private String nickname;
    private String des;

    /**
     * 这个state是target和session user的关系
     */
    private FollowState state;

    public int getTargetId() {
        return targetId;
    }

    public String getUsername() {
        return username;
    }

    public String getNickname() {
        return nickname;
    }

    public String getDes() {
        return des;
    }

    public FollowState getState() {
        return state;
    }

    public void setState(FollowState state) {
        this.state = state;
    }
}
