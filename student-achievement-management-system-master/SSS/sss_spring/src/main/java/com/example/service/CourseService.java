package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Course;
import com.example.mapper.CourseMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 业务层方法
 */
@Service
public class CourseService {

    @Resource
    private CourseMapper courseMapper;

    public void add(Course course) {
        courseMapper.insert(course);
    }


    public void updateById(Course course) {
        courseMapper.updateById(course);
    }

    public void deleteById(Integer id) {
        courseMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            courseMapper.deleteById(id);
        }
    }

    public Course selectById(Integer id) {
        return courseMapper.selectById(id);
    }

    public List<Course> selectAll(Course course) {
        return courseMapper.selectAll(course);
    }

    public PageInfo<Course> selectPage(Course course, Integer pageNum, Integer pageSize) {
        // 在调用查询方法之前，通过PageHelper设置分页参数
        PageHelper.startPage(pageNum, pageSize);

        List<Course> list = this.selectAll(course);

        // 使用PageInfo来封装分页结果，注意这里使用了PageInfo的静态方法of来创建实例
        // 这种方式是PageHelper 5.x版本及以后推荐的，因为它更加类型安全
        return PageInfo.of(list);
    }

    public int count(Integer teacherId) {
        return courseMapper.count(teacherId);
    }

}