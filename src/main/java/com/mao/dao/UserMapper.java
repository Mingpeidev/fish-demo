package com.mao.dao;

import com.mao.proj.User;

public interface UserMapper {
    User findByUsername(String username);
}