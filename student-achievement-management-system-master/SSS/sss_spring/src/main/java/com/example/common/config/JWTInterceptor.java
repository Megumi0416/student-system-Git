package com.example.common.config;

import cn.hutool.core.util.ObjectUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.common.Constants;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.exception.CustomException;
import com.example.service.AdminService;
import com.example.service.StudentService;
import com.example.service.TeacherService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 拦截器
 */
@Component
public class JWTInterceptor implements HandlerInterceptor {

    @Resource
    private AdminService adminService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private StudentService studentService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 从HTTP请求标头中获取token
        String token = request.getHeader(Constants.TOKEN);

        if (ObjectUtil.isNull(token)) {
            // 如果没拿到，那么再从请求参数里拿一次
            request.getHeader(Constants.TOKEN);
        }
        // 开始执行认证
        if (ObjectUtil.isNull(token)) {
            throw new CustomException(ResultCodeEnum.TOKEN_INVALID_ERROR);
        }
        Account account = null;
        try {
            String audience = JWT.decode(token).getAudience().get(0);
            String userId = audience.split("-")[0];
            String role = audience.split("-")[1];
            // 根据用户角色判断用户属于哪一个数据库表 然后查询用户数据
            if (RoleEnum.ADMIN.name().equals(role)) {
                account = adminService.selectById(Integer.valueOf(userId));
            } else if (RoleEnum.TEACHER.name().equals(role)) {
                account = teacherService.selectById(Integer.valueOf(userId));
            } else if (RoleEnum.STUDENT.name().equals(role)) {
                account = studentService.selectById(Integer.valueOf(userId));
            }
        } catch (Exception e) {
            throw new CustomException(ResultCodeEnum.TOKEN_CHECK_ERROR);
        }
        // 根据token里面携带的用户ID去对应的用户表查询 没有查到 所有报"用户不存在"错误
        if (ObjectUtil.isNull(account)) {
            // 用户不存在
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        try {
            // 通过用户的密码作为密钥再次验证token的合法性
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(account.getPassword())).build();
            jwtVerifier.verify(token);
        } catch(JWTVerificationException e){
            // 用户不存在
            throw new CustomException(ResultCodeEnum.TOKEN_CHECK_ERROR);
        }

        // 权限验证逻辑
        String requestURI = request.getRequestURI();
        String role = account.getRole();

        // 路径权限校验
        if (RoleEnum.ADMIN.name().equals(role)) {
            // 管理员明确禁止管理接口
            if (requestURI.startsWith("/studentcourse")){
                throw new CustomException(ResultCodeEnum.OPERATION_NOT_ALLOWED);
            }
        } else if (RoleEnum.TEACHER.name().equals(role)) {
            // 教师仅允许
            if (!requestURI.startsWith("/notice/selectAll") &&
                    !requestURI.startsWith("/studentCourse") &&
                    !requestURI.startsWith("/message") &&
                    !requestURI.startsWith("/course/count") &&
                    !requestURI.startsWith("/student/count") &&
                    !requestURI.startsWith("/api/all") &&
                    !requestURI.startsWith("/teacher/update") &&
                    !requestURI.startsWith("/updatePassword") &&
                    !requestURI.startsWith("/testscores") &&
                    !requestURI.startsWith("/grade/analysis") &&
                    !requestURI.startsWith("/exam")
            ) {
                System.out.println(requestURI+"::::url");
                throw new CustomException(ResultCodeEnum.OPERATION_NOT_ALLOWED);
            }
            // 明确禁止访问接口
            if (requestURI.startsWith("/admin")) {
                throw new CustomException(ResultCodeEnum.OPERATION_NOT_ALLOWED);
            }
        } else if (RoleEnum.STUDENT.name().equals(role)) {
            // 学生只允许访问指定接口
            if (!requestURI.startsWith("/studentCourse") &&
                    !requestURI.startsWith("/grade/add") &&
                    !requestURI.startsWith("/notice/selectAll") &&
                    !requestURI.startsWith("/message") &&
                    !requestURI.startsWith("/api/all") &&
                    !requestURI.startsWith("/testscores/latestScores") &&
                    !requestURI.startsWith("/student/update") &&
                    !requestURI.startsWith("/course/selectPage")) {
                throw new CustomException(ResultCodeEnum.OPERATION_NOT_ALLOWED);
            }
        }
        request.setAttribute(Constants.USER_INFO, account);
        return true;
    }
}
