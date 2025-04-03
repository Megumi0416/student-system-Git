package com.example.service.impl;

import com.example.entity.Admin;
import com.example.entity.Teacher;
import com.example.entity.Student;
import com.example.mapper.AdminMapper;
import com.example.mapper.TeacherMapper;
import com.example.mapper.StudentMapper;
import com.example.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private AdminMapper adminMapper;

    @Resource
    private TeacherMapper teacherMapper;

    @Resource
    private StudentMapper studentMapper;

    @Override
    public String getNameByUsername(String username) {
        // 查询管理员表
        Admin admin = adminMapper.selectByUsername(username);
        if (admin != null) {
            return admin.getName();
        }

        // 查询教师表
        Teacher teacher = teacherMapper.selectByUsername(username);
        if (teacher != null) {
            return teacher.getName();
        }

        // 查询学生表
        Student student = studentMapper.selectByUsername(username);
        if (student != null) {
            return student.getName();
        }

        // 未找到对应用户
        return "未知";
    };

    @Override
    public String getAvatarByUsername(String username) {
        Admin admin = adminMapper.selectByUsername(username);
        if (admin != null) return admin.getAvatar();

        Teacher teacher = teacherMapper.selectByUsername(username);
        if (teacher != null) return teacher.getAvatar();

        Student student = studentMapper.selectByUsername(username);
        if (student != null) return student.getAvatar();

        return "default-avatar-url"; // 返回默认头像路径
    }

    @Override
    public String getRoleByUsername(String username) {
        Admin admin = adminMapper.selectByUsername(username);
        if (admin != null) return "ADMIN";
        Teacher teacher = teacherMapper.selectByUsername(username);
        if (teacher != null) return "TEACHER";
        Student student = studentMapper.selectByUsername(username);
        if (student != null) return "STUDENT";
        return "unknown";
    }
}