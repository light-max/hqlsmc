package com.hql.scm.model.result;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LikeState {
    private int type;
    private int targetId;
    private int userId;

    private int count;
    private boolean like;
}
