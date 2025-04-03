package com.example.service;

import com.example.entity.TestScores;
import com.example.mapper.TestScoresMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TestScoresService {

    @Autowired
    private TestScoresMapper testscoresMapper;

    public void add(TestScores TestScores) {
        testscoresMapper.insert(TestScores);
    }

    public void update(TestScores TestScores) {
        testscoresMapper.updateById(TestScores);
    }

    public void deleteById(Integer id) {
        testscoresMapper.deleteById(id);
    }

    public TestScores selectById(Integer id) {
        return testscoresMapper.selectById(id);
    }

    public List<TestScores> selectAll(TestScores testscores) {
        return testscoresMapper.selectAll(testscores);
    }

    public List<TestScores> selectByStudentId(Integer studentId) {
        return testscoresMapper.selectByStudentId(studentId);
    }

    public List<TestScores> selectByCourseId(Integer courseId) {
        return testscoresMapper.selectByCourseId(courseId);
    }

    public PageInfo<TestScores> selectPage(TestScores testscores, Integer pageNum, Integer pageSize) {
        // 在调用查询方法之前，通过PageHelper设置分页参数
        PageHelper.startPage(pageNum, pageSize);
        PageHelper.orderBy("term DESC");
        List<TestScores> list = this.selectAll(testscores);

        // 使用PageInfo来封装分页结果，注意这里使用了PageInfo的静态方法of来创建实例
        // 这种方式是PageHelper 5.x版本及以后推荐的，因为它更加类型安全
        return PageInfo.of(list);
    }

    // TestScoresService.java 新增方法
    public List<TestScores> getLatestTermScores(Integer studentId) {
        return testscoresMapper.selectLatestTermScores(studentId);
    }

}