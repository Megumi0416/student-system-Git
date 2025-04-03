package com.example.mapper;

import com.example.entity.Account;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface AccountMapper {
    @Select("SELECT * FROM admin WHERE username = #{username} AND password = #{password}")
    Account findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);
}