package com.example.SSS.entity;

import lombok.Data;

/**
 * 考试监考教师关联实体类
 */
@Data
public class ExamInvigilator {
    private Integer id;
    private Integer examId;     // 考试ID
    private Integer teacherId;  // 教师ID
    private String teacherName; // 教师姓名(非数据库字段)
    private Boolean isMain;     // 是否主监考
} 