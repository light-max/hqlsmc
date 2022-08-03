package com.hql.scm.model.result;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MainPostResult {
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
}
