package com.asaki0019.advertising.service.impl;

import com.asaki0019.advertising.mapper.UserMapper;
import com.asaki0019.advertising.model.User;
import com.asaki0019.advertising.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    // 注册用户
    public boolean registerUser(User user) {
        // 检查用户是否已存在
        User existingUser = userMapper.selectOne(
                new QueryWrapper<User>().eq("username", user.getUsername())
        );
        if (existingUser != null) {
            return false; // 用户已存在
        }
        // 插入新用户
        return userMapper.insert(user) > 0;
    }

    // 用户登录
    public User loginUser(String username, String password) {
        // 查询用户
        return userMapper.selectOne(
                new QueryWrapper<User>().eq("username", username).eq("password", password)
        );
    }
}