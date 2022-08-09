package com.hql.smc.data.result;

public class Post {
    private int id;
    private int userId;
    private int mainId;
    private String content;
    private long createTime;
    private String username;
    private String nickname;
    private Images images;
    private String time;
    private int likeCount;
    private int replyCount;
    private boolean like;
    private boolean me;

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getMainId() {
        return mainId;
    }

    public String getContent() {
        return content;
    }

    public long getCreateTime() {
        return createTime;
    }

    public String getUsername() {
        return username;
    }

    public String getNickname() {
        return nickname;
    }

    public Images getImages() {
        return images;
    }

    public String getTime() {
        return time;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public boolean isLike() {
        return like;
    }

    public boolean isMe() {
        return me;
    }
}
