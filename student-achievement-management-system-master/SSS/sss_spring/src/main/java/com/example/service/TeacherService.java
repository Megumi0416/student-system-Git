package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.Constants;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Teacher;
import com.example.exception.CustomException;
import com.example.mapper.TeacherMapper;
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
public class TeacherService {

    @Resource
    private TeacherMapper teacherMapper;

    public void add(Teacher teacher) {
        // 首先，通过用户名查询数据库中是否已存在该用户
        Teacher dbTeacher = teacherMapper.selectByUsername(teacher.getUsername());

        // 如果用户已存在，则抛出异常
        if (ObjectUtil.isNotNull(dbTeacher)) {
            throw new CustomException(ResultCodeEnum.USER_ALREADY_EXISTS); // 注意枚举名和异常名的正确性
        }

        // 如果用户没有设置密码，则使用默认密码
        if (ObjectUtil.isEmpty(teacher.getPassword())) {
            teacher.setPassword(Constants.USER_DEFAULT_PASSWORL); // 注意常量名的正确性，以及是否应该是静态导入或类名.常量名
        }

        // 如果用户没有设置名字，则使用用户名作为名字
        if (ObjectUtil.isEmpty(teacher.getName())) {
            teacher.setName(teacher.getUsername());
        }

        // 设置用户角色为教师
        teacher.setRole(RoleEnum.TEACHER.name());

        // 最后，将新用户插入数据库
        teacherMapper.insert(teacher);
    }


    public void updateById(Teacher teacher) {
        teacherMapper.updateById(teacher);
    }

    public void deleteById(Integer id) {
        teacherMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            teacherMapper.deleteById(id);
        }
    }

    public Teacher selectById(Integer id) {
        return teacherMapper.selectById(id);
    }

    public List<Teacher> selectAll(Teacher teacher) {
        return teacherMapper.selectAll(teacher);
    }

    public PageInfo<Teacher> selectPage(Teacher teacher, Integer pageNum, Integer pageSize) {
        // 在调用查询方法之前，通过PageHelper设置分页参数
        PageHelper.startPage(pageNum, pageSize);

        List<Teacher> list = this.selectAll(teacher);

        // 使用PageInfo来封装分页结果，注意这里使用了PageInfo的静态方法of来创建实例
        // 这种方式是PageHelper 5.x版本及以后推荐的，因为它更加类型安全
        return PageInfo.of(list);
    }

    /**
     * 登录
     */
    public Teacher login(Account account) {
        Teacher dbTeacher = teacherMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isEmpty(dbTeacher)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_FOUND);
        }
        if (!dbTeacher.getPassword().equals(account.getPassword())) {
            throw new CustomException(ResultCodeEnum.USER_PASSWORD_INCORRECT);
        }
        // 生成token
        String token = TokenUtils.createToken(dbTeacher.getId() + "-" + dbTeacher.getRole(), dbTeacher.getPassword());
        dbTeacher.setToken(token);
        return dbTeacher;
    }

    /**
     * 修改密码
     */
    public void updatePassword(Account account) {
        Teacher dbTeacher = teacherMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbTeacher)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!account.getPassword().equals(dbTeacher.getPassword())) {
            throw new CustomException(ResultCodeEnum.PARAM_PASSWORD_ERROR);
        }
        dbTeacher.setPassword(account.getNewPassword());
        teacherMapper.updateById(dbTeacher);
    }

    /**
     * 注册
     */
    public void register(Teacher teacher) {
        // 首先，通过用户名查询数据库中是否已存在该教师
        Teacher dbTeacher = teacherMapper.selectByUsername(teacher.getUsername());

        // 如果用户已存在，则抛出异常
        if (ObjectUtil.isNotNull(dbTeacher)) {
            throw new CustomException(ResultCodeEnum.USER_ALREADY_EXISTS);
        }

        // 如果用户没有设置密码，则使用默认密码
        if (ObjectUtil.isEmpty(teacher.getPassword())) {
            teacher.setPassword(Constants.USER_DEFAULT_PASSWORL);
        }

        // 如果用户没有设置名字，则使用用户名作为名字
        if (ObjectUtil.isEmpty(teacher.getName())) {
            teacher.setName(teacher.getUsername());
        }

        // 设置用户角色为教师
        teacher.setRole(RoleEnum.TEACHER.name());

        // 最后，将新教师插入数据库
        teacherMapper.insert(teacher);
    }
}