package com.example.SSS.mapper;

import com.example.SSS.entity.ExamInvigilator;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ExamInvigilatorMapper {
    
    @Select("SELECT ei.*, t.name as teacherName FROM exam_invigilator ei " +
            "LEFT JOIN teacher t ON ei.teacher_id = t.id WHERE ei.exam_id = #{examId}")
    List<ExamInvigilator> selectByExamId(Integer examId);
    
    @Select("SELECT ei.*, t.name as teacherName FROM exam_invigilator ei " +
            "LEFT JOIN teacher t ON ei.teacher_id = t.id WHERE ei.id = #{id}")
    ExamInvigilator selectById(Integer id);
    
    @Insert("INSERT INTO exam_invigilator(exam_id, teacher_id, is_main) " +
            "VALUES(#{examId}, #{teacherId}, #{isMain})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(ExamInvigilator examInvigilator);
    
    @Update("UPDATE exam_invigilator SET teacher_id = #{teacherId}, is_main = #{isMain} WHERE id = #{id}")
    int update(ExamInvigilator examInvigilator);
    
    @Delete("DELETE FROM exam_invigilator WHERE id = #{id}")
    int deleteById(Integer id);
    
    @Delete("DELETE FROM exam_invigilator WHERE exam_id = #{examId}")
    int deleteByExamId(Integer examId);
} 