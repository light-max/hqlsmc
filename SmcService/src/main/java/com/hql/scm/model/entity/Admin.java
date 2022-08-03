package com.hql.scm.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hql.scm.fieldcheck.annotation.StringLengthMax;
import com.hql.scm.fieldcheck.annotation.StringLengthMin;
import com.hql.scm.fieldcheck.annotation.StringNonNull;
import com.hql.scm.fieldcheck.annotation.StringRegex;
import com.hql.scm.fieldcheck.interfaces.FieldCheckInterface;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_admin")
public class Admin implements FieldCheckInterface<Admin> {
    @TableId
    @StringLengthMin(msg = "账号长度不能小于${value}", trim = true, value = 4)
    @StringLengthMax(msg = "账号长度不能大于${value}", value = 32)
    @StringRegex(msg = "账号只能由数字字母和符号组成", value = "[a-zA-Z0-9]{0,33}")
    private String username;

    @StringLengthMin(msg = "密码长度不能小于${value}", trim = true, value = 4)
    @StringLengthMax(msg = "密码长度不能大于${value}", value = 32)
    @StringNonNull("密码不能为空")
    @StringRegex(msg = "密码只能由数字字母和符号组成", value = "[a-zA-Z0-9]{0,33}")
    private String password;

    @StringLengthMax(msg = "备注长度不能超过${value}个字符", value = 100)
    private String des;
}
