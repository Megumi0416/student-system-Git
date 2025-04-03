package com.example.mapper;

import com.example.entity.College;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CollegeMapper {
    int insert(College college);
    void updateById(College college);
    void deleteById(Integer id);

    @Select("SELECT * FROM college WHERE id = #{id}")
    College selectById(Integer id);

    List<College> selectAll(College college);
}