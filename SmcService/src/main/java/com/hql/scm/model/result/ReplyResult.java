package com.hql.scm.model.result;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReplyResult {
    private int id;
    private int userId;
    private int postId;
    private Integer targetId;
    private String content;
    private long createTime;

    private int mainPostId;
    private String time;

    private String username;
    private String nickname;

    private String targetUsername;
    private String targetNickname;

    private boolean me;
}
