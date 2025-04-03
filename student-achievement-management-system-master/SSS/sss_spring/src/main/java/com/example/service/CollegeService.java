package com.example.service;

import com.example.entity.College;
import com.example.mapper.CollegeMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollegeService {

    @Resource
    private CollegeMapper collegeMapper;

    public void add(College college) {
        collegeMapper.insert(college);
    }

    public void updateById(College college) {
        collegeMapper.updateById(college);
    }

    public void deleteById(Integer id) {
        collegeMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            collegeMapper.deleteById(id);
        }
    }

    public College selectById(Integer id) {
        return collegeMapper.selectById(id);
    }

    public List<College> selectAll(College college) {
        return collegeMapper.selectAll(college);
    }

    public PageInfo<College> selectPage(College college, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<College> list = this.selectAll(college);
        return PageInfo.of(list);
    }
}