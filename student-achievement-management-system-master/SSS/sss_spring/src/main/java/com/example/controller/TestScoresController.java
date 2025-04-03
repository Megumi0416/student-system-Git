package com.example.controller;

import com.example.common.Result;
import com.example.entity.TestScores;
import com.example.service.TestScoresService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/testscores")
public class TestScoresController {

    @Autowired
    private TestScoresService testscoresService;

    @PostMapping("/add")
    public Result add(@RequestBody TestScores testscores) {
        testscoresService.add(testscores);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody TestScores testscores) {
        testscoresService.update(testscores);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        testscoresService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        TestScores TestScores = testscoresService.selectById(id);
        return Result.success(TestScores);
    }

    @GetMapping("/selectAll")
    public Result selectAll(TestScores testscores) {
        List<TestScores> Testscores = testscoresService.selectAll(testscores);
        return Result.success(Testscores);
    }

    @GetMapping("/selectByStudentId/{studentId}")
    public Result selectByStudentId(@PathVariable Integer studentId) {
        List<TestScores> TestScores = testscoresService.selectByStudentId(studentId);
        return Result.success(TestScores);
    }

    @GetMapping("/selectByCourseId/{courseId}")
    public Result selectByCourseId(@PathVariable Integer courseId) {
        List<TestScores> TestScores = testscoresService.selectByCourseId(courseId);
        return Result.success(TestScores);
    }

    // 分页查询
    @GetMapping("/selectPage")
    public Result selectPage(TestScores testscores,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<TestScores> pageInfo =testscoresService.selectPage(testscores,pageNum,pageSize);
        return Result.success(pageInfo);
    }

    // TestScoresController.java 新增接口
    @GetMapping("/latestScores")
    public Result getLatestScores(@RequestParam Integer studentId) {
        List<TestScores> scores = testscoresService.getLatestTermScores(studentId);
        return Result.success(scores);
    }
}