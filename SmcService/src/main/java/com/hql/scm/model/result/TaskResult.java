package com.hql.scm.model.result;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskResult {
    private int id;
    private int userId;
    private int reviewId;
    private String des;
    private long createTime;
    private String createDate;
    private long reviewTime;
    private int state;
    private String reviewMsg;

    private String reviewDate;
    private String reviewUsername;
    private String reviewNickname;

    private String username;
    private String nickname;
}
