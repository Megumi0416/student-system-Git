package com.example.controller;

import com.example.common.AuthUtils;
import com.example.common.Result;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.CourseStatistics;
import com.example.entity.StudentCourse;

import com.example.service.StudentCourseService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/studentCourse")
public class StudentCourseController {

    @Resource
    StudentCourseService studentCourseService;

    /**
     * 学生选课
     */
    @PostMapping("/add")
    public Result add(@RequestBody StudentCourse studentCourse) {
        studentCourseService.add(studentCourse);
        return Result.success();
    }

    /**
     * 删除课选课
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        studentCourseService.deleteById(id);
        return Result.success();
    }

    /**
     * 更新课程成绩、学期
     */
    @PutMapping("/update")
    public Result update(@RequestBody StudentCourse studentCourse) {
        studentCourseService.update(studentCourse);
        return Result.success();
    }

    /**
     * 分页条件查询课程
     */
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             StudentCourse studentCourse) {
        PageInfo<StudentCourse> pageInfo = studentCourseService.selectPage(pageNum, pageSize, studentCourse);
        return Result.success(pageInfo);
    }

//    @GetMapping("/statistics")
//    public Result getStatistics(@RequestParam(required = false) String term) { // 接收学期参数
//        List<CourseStatistics> statistics = studentCourseService.getCourseStatistics(term);
//        return Result.success(statistics);
//    }
// StudentCourseController.java
//@GetMapping("/statistics")
//public Result getStatistics(
//        @RequestParam(required = false) String term,
//        @RequestParam(required = false) String username
//) {
//    List<CourseStatistics> statistics = studentCourseService.getCourseStatistics(term, username);
//    return Result.success(statistics);
//}
@GetMapping("/statistics")
public Result getStatistics(@RequestParam(required = false) String term) {
    // 获取当前登录用户
    Account currentUser = AuthUtils.getCurrentUser();

    String teacherName = null;
    if (RoleEnum.TEACHER.name().equals(currentUser.getRole())) {
        // 如果是教师，使用当前用户的用户名
        teacherName = currentUser.getUsername();
    }

    List<CourseStatistics> statistics = studentCourseService.getCourseStatistics(term, teacherName);
    return Result.success(statistics);
}

    @GetMapping("/statistics/{studentId}")
    public Result getStatistics(@PathVariable Integer studentId) {
        Map<String, Object> result = studentCourseService.getStatistics(studentId);
        return Result.success(result);
    }

    /**
     * 今日课程录入成绩数统计
     */
//    @GetMapping("/todayRecords")
//    public Result getTodayRecords() {
//        Integer count = studentCourseService.getTodayRecordsCount();
//        return Result.success(Map.of("newRecords", count));
//    }
    @GetMapping("/todayRecords")
    public Result getTodayRecords() {
        Account currentUser = AuthUtils.getCurrentUser();

        // 根据角色设置teacherId
        Integer teacherId = null;
        if (RoleEnum.TEACHER.name().equals(currentUser.getRole())) {
            teacherId = currentUser.getId();
        }

        Integer count = studentCourseService.getTodayRecordsCount(teacherId);
        return Result.success(Map.of("newRecords", count));
    }
}
