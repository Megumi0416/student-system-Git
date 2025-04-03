package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.Constants;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Student;
import com.example.exception.CustomException;
import com.example.mapper.StudentMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 业务层方法
 */
@Service
public class StudentService {

    @Resource
    private StudentMapper studentMapper;

    public void add(Student student) {
        // 首先，通过用户名查询数据库中是否已存在该用户
        Student dbStudent = studentMapper.selectByUsername(student.getUsername());

        // 如果用户已存在，则抛出异常
        if (ObjectUtil.isNotNull(dbStudent)) {
            throw new CustomException(ResultCodeEnum.USER_ALREADY_EXISTS); // 注意枚举名和异常名的正确性
        }

        // 如果用户没有设置密码，则使用默认密码
        if (ObjectUtil.isEmpty(student.getPassword())) {
            student.setPassword(Constants.USER_DEFAULT_PASSWORL); // 注意常量名的正确性，以及是否应该是静态导入或类名.常量名
        }

        // 如果用户没有设置名字，则使用用户名作为名字
        if (ObjectUtil.isEmpty(student.getName())) {
            student.setName(student.getUsername());
        }

        // 设置用户角色为学生
        student.setRole(RoleEnum.STUDENT.name());

        // 最后，将新用户插入数据库
        studentMapper.insert(student);
    }


    public void updateById(Student student) {
        studentMapper.updateById(student);
    }

    public void deleteById(Integer id) {
        studentMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            studentMapper.deleteById(id);
        }
    }

    public Student selectById(Integer id) {
        return studentMapper.selectById(id);
    }

    public List<Student> selectAll(Student student) {
        return studentMapper.selectAll(student);
    }

    public PageInfo<Student> selectPage(Student student, Integer pageNum, Integer pageSize) {
        // 在调用查询方法之前，通过PageHelper设置分页参数
        PageHelper.startPage(pageNum, pageSize);

        List<Student> list = this.selectAll(student);

        // 使用PageInfo来封装分页结果，注意这里使用了PageInfo的静态方法of来创建实例
        // 这种方式是PageHelper 5.x版本及以后推荐的，因为它更加类型安全
        return PageInfo.of(list);
    }

    /**
    * 登录
    * */
    public Student login(Account account) {
        Student dbStudent = studentMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isEmpty(dbStudent)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_FOUND);
        }
        if (!dbStudent.getPassword().equals(account.getPassword())) {
            throw new CustomException(ResultCodeEnum.USER_PASSWORD_INCORRECT);
        }
        // 生成token
        String token = TokenUtils.createToken(dbStudent.getId() + "-" +dbStudent.getRole(), dbStudent.getPassword());
        dbStudent.setToken(token);
        return dbStudent;
    }

    /**
     * 修改密码
     * */
    public void updatePassword(Account account) {
        Student dbStudent = studentMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbStudent)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!account.getPassword().equals(dbStudent.getPassword())) {
            throw new CustomException(ResultCodeEnum.PARAM_PASSWORD_ERROR);
        }
        dbStudent.setPassword(account.getNewPassword());
        studentMapper.updateById(dbStudent);
    }

    /**
     * 注册
     * */
    public void register(Student student) {
        // 首先，通过用户名查询数据库中是否已存在该学生
        Student dbStudent = studentMapper.selectByUsername(student.getUsername());

        // 如果用户已存在，则抛出异常
        if (ObjectUtil.isNotNull(dbStudent)) {
            throw new CustomException(ResultCodeEnum.USER_ALREADY_EXISTS);
        }

        // 如果用户没有设置密码，则使用默认密码
        if (ObjectUtil.isEmpty(student.getPassword())) {
            student.setPassword(Constants.USER_DEFAULT_PASSWORL);
        }

        // 如果用户没有设置名字，则使用用户名作为名字
        if (ObjectUtil.isEmpty(student.getName())) {
            student.setName(student.getUsername());
        }

        // 设置用户角色为学生
        student.setRole(RoleEnum.STUDENT.name());

        // 最后，将新学生插入数据库
        studentMapper.insert(student);
    }

    public int count(Integer teacherId) {
        return studentMapper.count(teacherId);
    }
}