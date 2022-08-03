package com.hql.scm.model.result;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FollowResult {
    private int targetId;
    private String username;
    private String nickname;
    private String des;

    /**
     * 这个state是target和session user的关系
     */
    private FollowState state;
}
