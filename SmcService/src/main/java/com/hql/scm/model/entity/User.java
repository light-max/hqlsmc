package com.hql.scm.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.hql.scm.fieldcheck.annotation.*;
import com.hql.scm.fieldcheck.interfaces.FieldCheckInterface;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_user")
public class User implements FieldCheckInterface<User> {
    @TableId(type = IdType.AUTO)
    private Integer id;

    @StringLengthMin(msg = "账号长度不能小于${value}", trim = true, value = 4)
    @StringLengthMax(msg = "账号长度不能大于${value}", value = 32)
    @StringRegex(msg = "账号只能由数字字母和符号组成", value = "[a-zA-Z0-9]{0,33}")
    private String username;

    @StringLengthMin(msg = "密码长度不能小于${value}", trim = true, value = 4)
    @StringLengthMax(msg = "密码长度不能大于${value}", value = 32)
    @StringRegex(msg = "密码只能由数字字母和符号组成", value = "[a-zA-Z0-9]{0,33}")
    private String password;

    @StringLengthMax(msg = "用户昵称不能超超过${value}个字符", value = 30)
    @StringNonNull("昵称不能为空")
    private String nickname;

    @NumberEnum(msg = "性别数值错误", value = {1, 2})
    private Integer gender;

    @DateFormat(msg = "日期格式错误", value = "yyyyMMdd")
    private String birthday;

    @StringRegex(msg = "邮箱格式错误", value = "[a-zA-Z0-9@._+-]{0,128}")
    @StringLengthMax(msg = "邮箱长度不能超过${value}个字符", value = 128)
    private String email;

    @StringLengthMax(msg = "个人简介不能超过${value}个字符", value = 300)
    private String des;

    @TableField(fill = FieldFill.INSERT)
    private Long createTime;

    private Boolean enable;
}
