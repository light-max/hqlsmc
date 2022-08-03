package com.hql.scm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hql.scm.constant.GlobalConstant;
import com.hql.scm.mapper.UserMapper;
import com.hql.scm.model.entity.User;
import com.hql.scm.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private final Object lock = new Object();

    @Override
    public User addNew(User user) {
        user.check();
        synchronized (lock) {
            User select = getOne(new QueryWrapper<User>()
                    .lambda()
                    .eq(User::getUsername, user.getUsername()));
            GlobalConstant.usernameExists.isNull(select);
            save(user);
        }
        return user;
    }

    @Override
    public Page<User> list(Integer n, String w, boolean username, boolean nickname) {
        Page<User> page = new Page<>(n == null ? 1 : n, 10);
        if (w == null || w.trim().isEmpty()) {
            return page(page);
        } else {
            LambdaQueryWrapper<User> wrapper = new QueryWrapper<User>()
                    .lambda();
            if (username) {
                wrapper.like(User::getUsername, w);
            }
            if (nickname) {
                wrapper.like(User::getNickname, w);
            }
            return page(page, wrapper);
        }
    }

    @Override
    public User update(User user) {
        User source = getById(user.getId());
        GlobalConstant.dataNotExists.notNull(source);
        if (user.getUsername() != null) {
            source.setUsername(user.getUsername());
        }
        if (user.getNickname() != null) {
            source.setNickname(user.getNickname());
        }
        if (user.getGender() != null) {
            source.setGender(user.getGender());
        }
        if (user.getBirthday() != null) {
            source.setBirthday(user.getBirthday());
        }
        if (user.getEmail() != null) {
            source.setEmail(user.getEmail());
        }
        if (user.getEnable() != null) {
            source.setEnable(user.getEnable());
        }
        if (user.getDes() != null) {
            source.setDes(user.getDes());
        }
        updateById(source.check());
        return source;
    }

    @Override
    public List<User> query(String w) {
        if (w != null && w.trim().length() > 0) {
            return list(new QueryWrapper<User>()
                    .lambda()
                    .like(User::getUsername, w)
                    .or()
                    .like(User::getNickname, w)
            );
        } else {
            return new ArrayList<>();
        }
    }
}
