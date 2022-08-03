package com.hql.scm.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.hql.scm.fieldcheck.annotation.DateFormat;
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
@TableName("t_task")
public class Task implements FieldCheckInterface<Task> {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    private Integer reviewId;

    @StringLengthMax(msg = "打卡描述不能超过${value}个字符", value = 100)
    private String des;

    @TableField(fill = FieldFill.INSERT)
    private Long createTime;

    @DateFormat("yyyyMMdd")
    private String createDate;

    private Long reviewTime;

    /**
     * 状态: 0:进行中,1:成功,2:失败
     */
    private Integer state;

    @StringLengthMax(msg = "审核信息不能超过${value}个字符", value = 100)
    private String reviewMsg;
}
