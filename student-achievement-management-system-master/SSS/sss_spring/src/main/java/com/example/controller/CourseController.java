package com.example.controller;

import com.example.common.AuthUtils;
import com.example.common.Result;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Course;
import com.example.service.CourseService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 前端请求接口
 */
@RestController
@RequestMapping("/course")
public class CourseController {

    @Resource
    private CourseService courseService;

    // 新增
    @PostMapping("/add")
    public Result add(@RequestBody Course course) {
        courseService.add(course);
        return Result.success();
    }

    // 修改
    @PutMapping("/update")
    public Result update(@RequestBody Course course) {
        courseService.updateById(course);
        return Result.success();
    }

    // 当个删除
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        courseService.deleteById(id);
        return Result.success();
    }

    // 批量删除
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        courseService.deleteBatch(ids);
        return Result.success();
    }

    // 单个查询
    @GetMapping("/selectById/{id}")
    public Result selectById(@RequestParam Integer id) {
        Course course = courseService.selectById(id);
        return Result.success(course);
    }

    // 查询所有
    @GetMapping("/selectAll")
    public Result selectAll(Course course) {
        List<Course> list = courseService.selectAll(course);
        return Result.success(list);
    }

    // 分页查询
    @GetMapping("/selectPage")
    public Result selectPage(Course course,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Course> pageInfo =courseService.selectPage(course,pageNum,pageSize);
        return Result.success(pageInfo);
    }

    @GetMapping("/count")
    public Result count() {
        // 获取当前登录用户
        Account currentUser = AuthUtils.getCurrentUser();

        Integer teacherId = null;
        if (RoleEnum.TEACHER.name().equals(currentUser.getRole())) {
            teacherId = currentUser.getId();
        }

        int count = courseService.count(teacherId);
        return Result.success(count);
    }
}