package com.mao.service.impl;

import com.mao.dao.UserMapper;
import com.mao.proj.User;
import com.mao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    /*
     * 检验用户登录业务
     */
    public User checkLogin(String username, String password) {

        User user = userMapper.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
}
