package com.example.utils;

import cn.hutool.core.date.DateUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.common.Constants;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.exception.CustomException;
import com.example.service.AdminService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;


/**
 * Token工具类
 */
@Component
public class TokenUtils {
    private static final Logger log = LoggerFactory.getLogger(TokenUtils.class);

    @Resource
    private AdminService adminService;

    private  static AdminService staticAdminService;

    @PostConstruct
    public void init() {
        staticAdminService = adminService;
    }


    /**
     * 生成JWT token
     */
    public static String createToken(String data, String sign) {
        // audience 是存储数据的一个媒介 存储用户ID和用户的角色  1-ADMIN
        return  JWT.create().withAudience(data)
                .withExpiresAt(DateUtil.offsetDay(new Date(), 1)) //设置过期时间1天后
                .sign(Algorithm.HMAC256(sign));
    }

    /**
     * 获取当前登录的用户
     */
    public static Account getCurrentUser() {
        try {
            HttpServletRequest request =((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = request.getHeader(Constants.TOKEN);
            String audience = JWT.decode(token).getAudience().get(0);
            String[] userRoles = audience.split("-");
            Integer userId = Integer.valueOf(userRoles[0]);
            String role = userRoles[1];
            if (RoleEnum.ADMIN.name().equals(role)) {
                staticAdminService.selectById(userId);
            }
        } catch (Exception e) {
            log.error("获取当前用户出错", e);
        }
        return null;
    }
}
