package com.example.SSS.entity;

import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * 考试实体类
 */
@Data
public class Exam {
    private Integer id;
    private String name;        // 考试名称
    private Integer courseId;   // 考试科目ID
    private String courseName;  // 考试科目名称(非数据库字段)
    private Integer teacherId;  // 教师ID
    private String teacherName; // 教师姓名(非数据库字段)
    private String examType;    // 考试类型(期中/期末/补考)
    private String examFormat;  // 考试形式(闭卷/开卷/机考)
    private Timestamp startTime; // 开始时间
    private Timestamp endTime;   // 结束时间
    private String location;    // 考试地点
    private Integer duration;   // 考试时长(分钟)
    private Integer capacity;   // 考试容量
    private Integer totalScore; // 考试总分
    private String status;      // 考试状态
    private String description; // 考试描述
    private String notes;       // 考试注意事项
    private Integer creatorId;  // 创建者ID
    private String creatorName; // 创建者姓名(非数据库字段)
    private LocalDateTime createTime; // 创建时间
} 