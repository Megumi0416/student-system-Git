package com.example.SSS.mapper;

import com.example.SSS.entity.Exam;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ExamMapper {
    
    @Select("SELECT e.*, c.name as courseName FROM exam e LEFT JOIN course c ON e.course_id = c.id")
    List<Exam> selectAll();
    
    @Select("SELECT e.*, c.name as courseName FROM exam e LEFT JOIN course c ON e.course_id = c.id WHERE e.id = #{id}")
    Exam selectById(Integer id);
    
    @Insert("INSERT INTO exam(name, course_id, exam_type, exam_format, start_time, end_time, location, capacity, total_score, notes, creator_id) " +
            "VALUES(#{name}, #{courseId}, #{examType}, #{examFormat}, #{startTime}, #{endTime}, #{location}, #{capacity}, #{totalScore}, #{notes}, #{creatorId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Exam exam);
    
    @Update("UPDATE exam SET name = #{name}, course_id = #{courseId}, exam_type = #{examType}, exam_format = #{examFormat}, " +
            "start_time = #{startTime}, end_time = #{endTime}, location = #{location}, capacity = #{capacity}, " +
            "total_score = #{totalScore}, notes = #{notes} WHERE id = #{id}")
    int update(Exam exam);
    
    @Delete("DELETE FROM exam WHERE id = #{id}")
    int deleteById(Integer id);
    
    @Select("SELECT e.*, c.name as courseName FROM exam e " +
            "LEFT JOIN course c ON e.course_id = c.id " +
            "WHERE e.name LIKE CONCAT('%', #{keyword}, '%') " +
            "OR c.name LIKE CONCAT('%', #{keyword}, '%')")
    List<Exam> search(String keyword);
} 