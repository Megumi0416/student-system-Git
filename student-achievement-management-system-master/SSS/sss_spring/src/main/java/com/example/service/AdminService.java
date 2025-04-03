package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.Constants;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Admin;
import com.example.exception.CustomException;
import com.example.mapper.AdminMapper;
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
public class AdminService {

    @Resource
    private AdminMapper adminMapper;

    public void add(Admin admin) {
        // 首先，通过用户名查询数据库中是否已存在该用户
        Admin dbAdmin = adminMapper.selectByUsername(admin.getUsername());

        // 如果用户已存在，则抛出异常
        if (ObjectUtil.isNotNull(dbAdmin)) {
            throw new CustomException(ResultCodeEnum.USER_ALREADY_EXISTS); // 注意枚举名和异常名的正确性
        }

        // 如果用户没有设置密码，则使用默认密码
        if (ObjectUtil.isEmpty(admin.getPassword())) {
            admin.setPassword(Constants.USER_DEFAULT_PASSWORL); // 注意常量名的正确性，以及是否应该是静态导入或类名.常量名
        }

        // 如果用户没有设置名字，则使用用户名作为名字
        if (ObjectUtil.isEmpty(admin.getName())) {
            admin.setName(admin.getUsername());
        }

        // 设置用户角色为管理员
        admin.setRole(RoleEnum.ADMIN.name());

        // 最后，将新用户插入数据库
        adminMapper.insert(admin);
    }


    public void updateById(Admin admin) {
        adminMapper.updateById(admin);
    }

    public void deleteById(Integer id) {
        adminMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            adminMapper.deleteById(id);
        }
    }

    public Admin selectById(Integer id) {
        return adminMapper.selectById(id);
    }

    public List<Admin> selectAll(Admin admin) {
        return adminMapper.selectAll(admin);
    }

    public PageInfo<Admin> selectPage(Admin admin, Integer pageNum, Integer pageSize) {
        // 在调用查询方法之前，通过PageHelper设置分页参数
        PageHelper.startPage(pageNum, pageSize);

        List<Admin> list = this.selectAll(admin);

        // 使用PageInfo来封装分页结果，注意这里使用了PageInfo的静态方法of来创建实例
        // 这种方式是PageHelper 5.x版本及以后推荐的，因为它更加类型安全
        return PageInfo.of(list);
    }

    /**
    * 登录
    * */
    public Admin login(Account account) {
        Admin dbAdmin = adminMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isEmpty(dbAdmin)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_FOUND);
        }
        if (!dbAdmin.getPassword().equals(account.getPassword())) {
            throw new CustomException(ResultCodeEnum.USER_PASSWORD_INCORRECT);
        }
        // 生成token
        String token = TokenUtils.createToken(dbAdmin.getId() + "-" +dbAdmin.getRole(), dbAdmin.getPassword());
        dbAdmin.setToken(token);
        return dbAdmin;
    }

    /**
     * 修改密码
     * */
    public void updatePassword(Account account) {
        Admin dbAdmin = adminMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbAdmin)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!account.getPassword().equals(dbAdmin.getPassword())) {
            throw new CustomException(ResultCodeEnum.PARAM_PASSWORD_ERROR);
        }
        dbAdmin.setPassword(account.getNewPassword());
        adminMapper.updateById(dbAdmin);
    }
}