package com.hql.scm.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.hql.scm.fieldcheck.annotation.StringLengthMax;
import com.hql.scm.fieldcheck.annotation.StringNonNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_reply")
public class Reply {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    private Integer postId;

    /**
     * 被回复人的ID，
     * 为空时只回复帖子的楼主，
     * 不回复某个人；
     *
     * 不为空时回复的是某人，并不会回复帖子的楼主
     */
    @Nullable
    private Integer targetId;

    @StringNonNull("帖子内容不能为空")
    @StringLengthMax(msg = "帖子内容不能超过${value}个字符", value = 300)
    private String content;

    @TableField(fill = FieldFill.INSERT)
    private Long createTime;
}
