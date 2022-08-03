package com.hql.scm.model.result;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FollowState {
    private int userId;
    private int targetId;

    private boolean follow;
    private int count;
    private boolean friend;
}
