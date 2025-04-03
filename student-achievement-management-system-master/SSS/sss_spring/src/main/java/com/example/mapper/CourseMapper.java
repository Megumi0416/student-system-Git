package com.example.mapper;

import com.example.entity.Course;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CourseMapper {

    int insert(Course course);

    void updateById(Course course);

    void deleteById(Integer id);

    @Select("SELECT * FROM `course` WHERE id = #{id}")
    Course selectById(Integer id);

    List<Course> selectAll(Course course);

    int count(@Param("teacherId") Integer teacherId);
}
