package com.example.SSS.mapper;

import com.example.SSS.entity.ExamStudent;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ExamStudentMapper {
    
    @Select("SELECT es.*, s.name as studentName, s.username as studentUsername FROM exam_student es " +
            "LEFT JOIN student s ON es.student_id = s.id WHERE es.exam_id = #{examId}")
    List<ExamStudent> selectByExamId(Integer examId);
    
    @Select("SELECT es.*, s.name as studentName, s.username as studentUsername FROM exam_student es " +
            "LEFT JOIN student s ON es.student_id = s.id WHERE es.id = #{id}")
    ExamStudent selectById(Integer id);
    
    @Insert("INSERT INTO exam_student(exam_id, student_id, seat_no, status) " +
            "VALUES(#{examId}, #{studentId}, #{seatNo}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(ExamStudent examStudent);
    
    @Insert({
        "<script>",
        "INSERT INTO exam_student(exam_id, student_id, status) VALUES ",
        "<foreach collection='list' item='item' separator=','>",
        "(#{item.examId}, #{item.studentId}, #{item.status})",
        "</foreach>",
        "</script>"
    })
    int batchInsert(List<ExamStudent> examStudentList);
    
    @Update("UPDATE exam_student SET seat_no = #{seatNo}, status = #{status} WHERE id = #{id}")
    int update(ExamStudent examStudent);
    
    @Delete("DELETE FROM exam_student WHERE id = #{id}")
    int deleteById(Integer id);
    
    @Delete("DELETE FROM exam_student WHERE exam_id = #{examId}")
    int deleteByExamId(Integer examId);
    
    @Select("SELECT COUNT(*) FROM exam_student WHERE exam_id = #{examId}")
    int countByExamId(Integer examId);
} 