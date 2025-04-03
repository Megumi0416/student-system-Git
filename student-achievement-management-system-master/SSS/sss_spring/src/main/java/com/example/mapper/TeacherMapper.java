package com.example.mapper;

import com.example.entity.Teacher;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TeacherMapper {

    int insert(Teacher teacher);

    void updateById(Teacher teacher);

    void deleteById(Integer id);

    @Select("SELECT * FROM `teacher` WHERE id = #{id}")
    Teacher selectById(Integer id);

    @Select("SELECT * FROM `teacher` WHERE username = #{username}")
    Teacher selectByUsername(String username);

    List<Teacher> selectAll(Teacher teacher);
}
