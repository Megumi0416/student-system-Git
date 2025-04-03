package com.example.common;

import com.example.entity.Account;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class AuthUtils {
    public static Account getCurrentUser() {
        // 从请求属性中获取用户信息
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return (Account) request.getAttribute(Constants.USER_INFO);
    }
}
