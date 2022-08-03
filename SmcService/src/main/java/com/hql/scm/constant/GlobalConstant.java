package com.hql.scm.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GlobalConstant implements Assert {
    error("系统错误"),

    noAccess("没有访问权限，请重新登录"),

    usernameExists("用户名已存在"),
    usernameFormat("用户名格式错误，只能使用英文字母和数字何下划线，并且长度在4-16位"),
    passwordFormat("密码格式错误，只能使用英文字母、数字、下划线和小数点，并且长度在4-16位"),

    loginError("登陆错误，用户名或密码不正确"),
    loginError1("登录错误，权限错误"),
    accountDisable("登陆失败，此账号已被禁用"),

    dataNotExists("数据不存在"),

    sourcePasswordError("原密码错误"),

    followError("自己不能关注自己"),

    dailyAttendanceError("今天已经打过卡了，不能重复打卡"),
    dailyAttendanceError1("今日打卡进行中，请等待审核"),
    ;

    private String message;
}
