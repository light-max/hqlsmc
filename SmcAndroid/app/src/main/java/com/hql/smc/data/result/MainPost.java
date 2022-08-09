package com.hql.smc.data.result;

public class MainPost {
    private int id;
    private int userId;
    private String title;
    private String content;
    private long createTime;
    private String username;
    private String nickname;
    private Images images;
    private String time;
    private int likeCount;
    private int replyCount;
    private boolean like;

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
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

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public void setLike(boolean like) {
        this.like = like;
    }
}
