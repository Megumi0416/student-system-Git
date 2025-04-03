package com.example.mapper;

import com.example.entity.Student;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface StudentMapper {

    int insert(Student student);

    void updateById(Student student);

    void deleteById(Integer id);

    @Select("SELECT * FROM `student` WHERE id = #{id}")
    Student selectById(Integer id);

    @Select("SELECT * FROM `student` WHERE username = #{username}")
    Student selectByUsername(String username);

    List<Student> selectAll(Student student);

    int count(@Param("teacherId") Integer teacherId);
}
