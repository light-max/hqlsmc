package com.hql.smc.data.result;

public class Reply {
    private int id;
    private int userId;
    private int postId;
    // 回复回复ID，为0时代表只回复帖子
    private int targetId;
    private String content;
    private long createTime;

    private int mainPostId;
    private String time;

    private String username;
    private String nickname;

    private String targetUsername;
    private String targetNickname;

    private boolean me;

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getPostId() {
        return postId;
    }

    public int getTargetId() {
        return targetId;
    }

    public String getContent() {
        return content;
    }

    public long getCreateTime() {
        return createTime;
    }

    public int getMainPostId() {
        return mainPostId;
    }

    public String getTime() {
        return time;
    }

    public String getUsername() {
        return username;
    }

    public String getNickname() {
        return nickname;
    }

    public String getTargetUsername() {
        return targetUsername;
    }

    public String getTargetNickname() {
        return targetNickname;
    }

    public boolean isMe() {
        return me;
    }
}
