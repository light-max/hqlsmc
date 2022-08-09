package com.hql.scm.model.result;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SpaceValueNumbers {
    private int postCount;
    private int followCount;
    private int followerCount;
}
