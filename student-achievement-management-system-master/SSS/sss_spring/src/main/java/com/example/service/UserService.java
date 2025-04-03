package com.example.service;

public interface UserService {
    /**
     * 根据用户名获取用户姓名
     * @param username 用户名
     * @return 姓名
     */
    String getNameByUsername(String username);
    /**
     * 根据用户名获取用户头像
     * @param username 用户名
     * @return 姓名
     */
    String getAvatarByUsername(String username);

    String getRoleByUsername(String username);
}