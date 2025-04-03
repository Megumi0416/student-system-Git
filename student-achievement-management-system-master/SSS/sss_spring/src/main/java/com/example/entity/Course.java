package com.example.entity;

import java.math.BigDecimal;

// 课程
public class Course {

    private Integer id; //主键ID
    private String name;    //课程名称
    private String code;    //课程编码
    private BigDecimal credit; //学分
    private String hour;        //课时
    private Integer collegeId;  // 学院ID
    private String college; //开课学院
    private Integer teacherId;  //授课教师ID
    private String teachcollege; //授课教师学院
    private String teacherName; //教师姓名

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeachcollege() {
        return teachcollege;
    }

    public void setTeachcollege(String teachcollege) {
        this.teachcollege = teachcollege;
    }

    public Integer getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(Integer collegeId) {
        this.collegeId = collegeId;
    }
}
