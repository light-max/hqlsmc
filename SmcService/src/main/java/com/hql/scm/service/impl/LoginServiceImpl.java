package com.hql.scm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hql.scm.constant.GlobalConstant;
import com.hql.scm.mapper.AdminMapper;
import com.hql.scm.mapper.UserMapper;
import com.hql.scm.model.data.Result;
import com.hql.scm.model.entity.Admin;
import com.hql.scm.model.entity.User;
import com.hql.scm.service.LoginService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class LoginServiceImpl implements LoginService {
    @Resource
    AdminMapper adminMapper;

    @Resource
    UserMapper userMapper;

    @Override
    public Result<Admin> admin(String username, String password) {
        Admin admin = adminMapper.selectOne(new QueryWrapper<Admin>()
                .lambda()
                .eq(Admin::getUsername, username)
                .eq(Admin::getPassword, password));
        if (admin == null) {
            return Result.error(GlobalConstant.loginError.getMessage());
        } else {
            return Result.success(admin);
        }
    }

    @Override
    public Result<User> user(String username, String password) {
        User user = userMapper.selectOne(new QueryWrapper<User>()
                .lambda()
                .eq(User::getUsername, username)
                .eq(User::getPassword, password));
        if (user == null) {
            return Result.error(GlobalConstant.loginError.getMessage());
        } else if (!user.getEnable()) {
            return Result.error(GlobalConstant.accountDisable.getMessage());
        } else {
            return Result.success(user);
        }
    }
}
