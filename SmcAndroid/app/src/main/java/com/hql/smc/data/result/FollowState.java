package com.hql.smc.data.result;

public class FollowState {
    private int userId;
    private int targetId;
    private boolean follow;
    private int count;
    private boolean friend;

    public int getUserId() {
        return userId;
    }

    public int getTargetId() {
        return targetId;
    }

    public boolean isFollow() {
        return follow;
    }

    public int getCount() {
        return count;
    }

    public boolean isFriend() {
        return friend;
    }
}
