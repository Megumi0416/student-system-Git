package com.example.mapper;

import com.example.entity.CourseStatistics;
import com.example.entity.StudentCourse;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface StudentCourseMapper {

    @Insert("insert into student_course (student_id, course_id) values(#{studentId}, #{courseId})")
    void insert(StudentCourse studentCourse);

    @Select("select * from student_course where student_id = #{studentId} and course_id = #{courseId}")
    StudentCourse selectByCondition(StudentCourse studentCourse);

//    @Select("select * from student_course where name like concat('%', #{name}, '%') and code like concat('%', #{code}, '%') and student_id = #{studentId}")
    List<StudentCourse> selectAll(StudentCourse studentCourse);

    @Delete("delete from student_course where id = #{id}")
    void deleteById(Integer id);

    void updateById(StudentCourse studentCourse);

    //List<Map<String, Object>> selectScoreStatistics(Integer studentId);

    // StudentCourseMapper.java
//    @Select("SELECT c.name as courseName, c.code as code, " +
//            "AVG(sc.score) as avgScore, MAX(sc.score) as maxScore, MIN(sc.score) as minScore " +
//            "FROM student_course sc " +
//            "LEFT JOIN course c ON sc.course_id = c.id " +
//            "WHERE sc.term = #{term} " +
//            "GROUP BY sc.course_id")
//    List<CourseStatistics> selectCourseStatistics(String term);
    @Select("<script>" +
            "SELECT c.name AS courseName, c.code AS code, t.username AS teacherName," +
            "       AVG(sc.score) AS avgScore, MAX(sc.score) AS maxScore, MIN(sc.score) AS minScore " +
            "FROM student_course sc " +
            "LEFT JOIN course c ON sc.course_id = c.id " +
            "LEFT JOIN teacher t ON c.teacher_id = t.id " +
            "WHERE sc.term = #{term} " +
            "<if test='teacherName != null'> AND t.username = #{teacherName} </if> " +
            "GROUP BY sc.course_id" +
            "</script>")
    List<CourseStatistics> selectCourseStatistics(@Param("term") String term, @Param("teacherName") String teacherName);

    @Select("SELECT COUNT(*) FROM student_course WHERE student_id = #{studentId}")
    Integer selectCourseCount(Integer studentId);

    @Select("SELECT AVG(score) FROM student_course WHERE student_id = #{studentId} AND score IS NOT NULL")
    BigDecimal selectAverageScore(Integer studentId);

    @Select("SELECT COUNT(*) FROM student_course WHERE student_id = #{studentId} AND score IS NOT NULL")
    Integer selectScoredCount(Integer studentId);

//    @Select("SELECT COUNT(*) FROM student_course WHERE DATE(update_time) = CURDATE()")
//    Integer selectTodayRecordsCount();
    @Select("<script>" +
            "SELECT COUNT(*) FROM student_course " +
            "WHERE DATE(update_time) = CURDATE() " +
            "<if test='teacherId != null'>" +
            "AND course_id IN (SELECT id FROM course WHERE teacher_id = #{teacherId})" +
            "</if>" +
            "</script>")
    Integer selectTodayRecordsCount(@Param("teacherId") Integer teacherId);
}
