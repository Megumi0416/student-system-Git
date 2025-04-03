package com.example.controller;

import com.example.common.Constants;
import com.example.common.Result;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.service.AdminService;
import com.example.service.StudentService;
import com.example.service.TeacherService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class WebController {
/**
 * @Autowired 和 @Resource 都是用来实现依赖注入的注解（在 Spring/Spring Boot 项目中），但二者却有着 5 点不同：
 * 1、来源不同：@Autowired 来自 Spring 框架，而 @Resource 来自于（Java）JSR-250；
 * 2、依赖查找的顺序不同：@Autowired 先根据类型再根据名称查询，而 @Resource 先根据名称再根据类型查询；
 * 3、支持的参数不同：@Autowired 只支持设置 1 个参数，而 @Resource 支持设置 7 个参数；
 * 4、依赖注入的用法支持不同：@Autowired 既支持构造方法注入，又支持属性注入和 Setter 注入，而 @Resource 只支持属性注入和 Setter 注入；
 * 5、编译器 IDEA 的提示不同：当注入 Mapper 对象时，使用 @Autowired 注解编译器会提示错误，而使用 @Resource 注解则不会提示错误。
 * */
    @Resource
    private AdminService adminService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private StudentService studentService;

    /**
     * 默认请求接口
     */
    @GetMapping("/")
    public Result hello(){
        return Result.success();
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public Result login(@RequestBody Account account) {
        Account loginAccount = null;
        if (RoleEnum.ADMIN.name().equals(account.getRole())) {
            loginAccount = adminService.login(account);
        } else if (RoleEnum.TEACHER.name().equals(account.getRole())) {
            loginAccount = teacherService.login(account);
        } else if (RoleEnum.STUDENT.name().equals(account.getRole())) {
            loginAccount = studentService.login(account);
        }
        return Result.success(loginAccount);
    }

    /**
     * 注册
     */
//    @PostMapping("/register")
//    public Result register(@RequestBody Account account) {
//        if (RoleEnum.TEACHER.name().equals(account.getRole())) {
//            teacherService.register(account.toTeacher());
//        } else if (RoleEnum.STUDENT.name().equals(account.getRole())) {
//            studentService.register(account.toStudent());
//        } else {
//            return Result.error("角色选择错误");
//        }
//        return Result.success();
//    }
    @PostMapping("/register")
    public Result register(@RequestBody Account account) {
        String username = account.getUsername(); // 获取学号或工号
        String role = account.getRole();

        if (RoleEnum.TEACHER.name().equals(role)) {
            // 教师工号验证
            if (!isValidTeacherId(username)) {
                return Result.error("教师工号有误，请检查");
            }
            teacherService.register(account.toTeacher());
        } else if (RoleEnum.STUDENT.name().equals(role)) {
            // 学生学号验证
            if (!isValidStudentId(username)) {
                return Result.error("学生学号有误，请检查");
            }
            studentService.register(account.toStudent());
        } else {
            return Result.error("角色选择错误");
        }
        return Result.success();
    }

    /**
     * 验证学生学号是否在有效范围内
     */
    private boolean isValidStudentId(String studentId) {
        return studentId.compareTo(Constants.STUDENT_START_RANGE) >= 0 &&
                studentId.compareTo(Constants.STUDENT_END_RANGE) <= 0;
    }

    /**
     * 验证教师工号是否在有效范围内
     */
    private boolean isValidTeacherId(String teacherId) {
        return teacherId.compareTo(Constants.TEACHER_START_RANGE) >= 0 &&
                teacherId.compareTo(Constants.TEACHER_END_RANGE) <= 0;
    }

    /**
     * 修改密码
     */
    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody Account account) {
        if (RoleEnum.ADMIN.name().equals(account.getRole())) {
            adminService.updatePassword(account);
        } else if (RoleEnum.TEACHER.name().equals(account.getRole())) {
            teacherService.updatePassword(account);
        } else if (RoleEnum.STUDENT.name().equals(account.getRole())) {
            studentService.updatePassword(account);
        }
        return Result.success();
    }
}
