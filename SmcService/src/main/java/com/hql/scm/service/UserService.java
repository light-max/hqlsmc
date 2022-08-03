package com.hql.scm.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hql.scm.model.entity.User;

import java.util.List;

public interface UserService extends IService<User> {
    User addNew(User user);

    Page<User> list(Integer n, String w, boolean username, boolean nickname);

    User update(User user);

    List<User> query(String w);
}
