package com.example.SSS.entity;

import lombok.Data;

/**
 * 考试学生关联实体类
 */
@Data
public class ExamStudent {
    private Integer id;
    private Integer examId;     // 考试ID
    private Integer studentId;  // 学生ID
    private String studentName; // 学生姓名(非数据库字段)
    private String studentUsername; // 学号(非数据库字段)
    private Integer seatNo;     // 座位号
    private String status;      // 状态(NOT_STARTED/ABSENT/PRESENT/FINISHED)
} 