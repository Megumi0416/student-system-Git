package com.example.controller;

import com.example.common.Result;
import com.example.entity.Account;
import com.example.entity.Admin;
import com.example.entity.Student;
import com.example.entity.Teacher;
import com.example.mapper.AccountMapper;
import com.example.mapper.AdminMapper;
import com.example.mapper.StudentMapper;
import com.example.mapper.TeacherMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private AdminMapper adminMapper;

    @PostMapping("/login")
    public Account login(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");

        Account account = accountMapper.findByUsernameAndPassword(username, password);

        if (account != null) {
            return account;
        } else {
            throw new RuntimeException("用户名或密码错误");
        }
    }

    @GetMapping("/all")
    public Result getAllUsers() {
        // 查询所有角色用户并合并
        List<Student> students = studentMapper.selectAll(new Student());
        List<Teacher> teachers = teacherMapper.selectAll(new Teacher());
        List<Admin> admins = adminMapper.selectAll(new Admin());

        List<Map<String, Object>> users = new ArrayList<>();

        admins.forEach(a -> users.add(new HashMap<>() {{
            put("username", a.getUsername());
            put("name", a.getName());
            put("avatar", a.getAvatar());
            put("role", "ADMIN");
        }}));

        teachers.forEach(t -> users.add(new HashMap<>() {{
            put("username", t.getUsername());
            put("name", t.getName());
            put("avatar", t.getAvatar());
            put("role", "TEACHER");
        }}));

        students.forEach(s -> users.add(new HashMap<>() {{
            put("username", s.getUsername());
            put("name", s.getName());
            put("avatar", s.getAvatar());
            put("role", "STUDENT");
        }}));

        return Result.success(users);
    }

}