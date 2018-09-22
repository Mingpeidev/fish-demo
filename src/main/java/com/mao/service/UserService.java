package com.mao.service;

import com.mao.proj.User;

//Service层接口
public interface UserService {

	// 检验用户登录
	User checkLogin(String username, String password);

}
