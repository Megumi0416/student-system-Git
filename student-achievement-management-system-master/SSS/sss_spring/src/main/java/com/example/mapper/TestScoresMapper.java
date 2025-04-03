package com.example.mapper;

import com.example.entity.TestScores;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TestScoresMapper {

    void insert(TestScores testscores);

    void updateById(TestScores testscores);

    void deleteById(Integer id);

    TestScores selectById(Integer id);

    List<TestScores> selectAll(TestScores testscores);

    List<TestScores> selectByStudentId(Integer studentId);

    List<TestScores> selectByCourseId(Integer courseId);

    // TestScoresMapper.java 新增方法
    List<TestScores> selectLatestTermScores(@Param("studentId") Integer studentId);
}