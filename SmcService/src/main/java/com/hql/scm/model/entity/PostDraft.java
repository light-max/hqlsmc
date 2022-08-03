package com.hql.scm.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hql.scm.fieldcheck.annotation.StringLengthMax;
import com.hql.scm.fieldcheck.interfaces.FieldCheckInterface;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_draft")
public class PostDraft implements FieldCheckInterface<PostDraft> {
    @TableId
    private Integer userId;

    @StringLengthMax(msg = "标题不能超过${value}个字符", value = 50)
    private String title;

    @StringLengthMax(msg = "内容不能超过${value}个字符", value = 1000)
    private String content;
}
