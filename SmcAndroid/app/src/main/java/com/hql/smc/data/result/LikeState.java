package com.hql.smc.data.result;

public class LikeState {
    private int type;
    private int targetId;
    private int userId;
    private int count;
    private boolean like;

    public int getType() {
        return type;
    }

    public int getTargetId() {
        return targetId;
    }

    public int getUserId() {
        return userId;
    }

    public int getCount() {
        return count;
    }

    public boolean isLike() {
        return like;
    }
}

