package com.example.service;

import com.example.common.enums.ResultCodeEnum;
import com.example.entity.CourseStatistics;
import com.example.entity.StudentCourse;
import com.example.entity.TestScores;
import com.example.exception.CustomException;
import com.example.mapper.StudentCourseMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class StudentCourseService {

    @Resource
    StudentCourseMapper studentCourseMapper;

    public void add(StudentCourse studentCourse) {
        StudentCourse course = studentCourseMapper.selectByCondition(studentCourse);// 通过学生ID和课程ID做一次查询的筛选，看看这个学生之前有没有选过这个课程
        if (course != null) {
            throw new CustomException(ResultCodeEnum.COURSE_ALREADY_SELECTED);
        }
        studentCourseMapper.insert(studentCourse);
    }

    public PageInfo<StudentCourse> selectPage(Integer pageNum, Integer pageSize, StudentCourse studentCourse) {
        PageHelper.startPage(pageNum, pageSize);
        List<StudentCourse> list = studentCourseMapper.selectAll(studentCourse);
        return PageInfo.of(list);
    }

    public void deleteById(Integer id) {
        studentCourseMapper.deleteById(id);
    }

    public void update(StudentCourse studentCourse) {
        studentCourseMapper.updateById(studentCourse);
    }

//    public List<Map<String, Object>> getScoreStatistics(Integer studentId) {
//        return studentCourseMapper.selectScoreStatistics(studentId);
//    }

    // StudentCourseService.java
    public List<CourseStatistics> getCourseStatistics(String term,String username) {
        return studentCourseMapper.selectCourseStatistics(term,username);
    }

    public Map<String, Object> getStatistics(Integer studentId) {
        Integer courseCount = studentCourseMapper.selectCourseCount(studentId);
        Integer scoredCount = studentCourseMapper.selectScoredCount(studentId);
        BigDecimal averageScore = studentCourseMapper.selectAverageScore(studentId);
        return Map.of("courseCount", courseCount, "scoredCount", scoredCount,"averageScore", averageScore);
    }

//    public Integer getTodayRecordsCount() {
//        return studentCourseMapper.selectTodayRecordsCount();
//    }
    public Integer getTodayRecordsCount(Integer teacherId) {
        return studentCourseMapper.selectTodayRecordsCount(teacherId);
    }

}
