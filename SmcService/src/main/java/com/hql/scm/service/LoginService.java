package com.hql.scm.service;

import com.hql.scm.model.data.Result;
import com.hql.scm.model.entity.Admin;
import com.hql.scm.model.entity.User;

public interface LoginService {
    Result<Admin> admin(String username, String password);

    Result<User> user(String username, String password);
}
