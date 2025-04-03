package com.example.controller;

import com.example.common.Result;
import com.example.entity.College;
import com.example.service.CollegeService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/college")
public class CollegeController {

    @Resource
    private CollegeService collegeService;

    @PostMapping("/add")
    public Result add(@RequestBody College college) {
        collegeService.add(college);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody College college) {
        collegeService.updateById(college);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        collegeService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        collegeService.deleteBatch(ids);
        return Result.success();
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        College college = collegeService.selectById(id);
        return Result.success(college);
    }

    @GetMapping("/selectAll")
    public Result selectAll(College college) {
        List<College> list = collegeService.selectAll(college);
        return Result.success(list);
    }

    @GetMapping("/selectPage")
    public Result selectPage(College college,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<College> pageInfo = collegeService.selectPage(college, pageNum, pageSize);
        return Result.success(pageInfo);
    }
}