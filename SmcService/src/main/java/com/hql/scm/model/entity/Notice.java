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
@TableName("t_notice")
public class Notice implements FieldCheckInterface<Notice> {
    @TableId(type = IdType.AUTO)
    private Integer id;

    @StringLengthMax(msg = "公告标题不能超过${value}个字符", value = 30)
    @StringNonNull("公告标题不能为空")
    public String title;

    @StringLengthMax(msg = "公告内容不能超过${value}个字符", value = 1000)
    @StringNonNull("公告内容不能为空")
    public String des;

    @TableField(fill = FieldFill.INSERT)
    public Long createTime;

    public Integer grade;
}
