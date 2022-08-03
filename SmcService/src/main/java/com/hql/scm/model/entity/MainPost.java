package com.hql.scm.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.hql.scm.fieldcheck.annotation.StringLengthMax;
import com.hql.scm.fieldcheck.annotation.StringNonNull;
import com.hql.scm.fieldcheck.interfaces.FieldCheckInterface;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_main_post")
public class MainPost implements FieldCheckInterface<MainPost> {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    @StringNonNull("标题不能为空")
    @StringLengthMax(msg = "标题不能超过${value}个字符", value = 50)
    private String title;

    @StringLengthMax(msg = "内容不能超过${value}个字符", value = 1000)
    private String content;

    @TableField(fill = FieldFill.INSERT)
    private Long createTime;
}
