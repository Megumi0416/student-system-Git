package com.example.SSS.mapper;

import com.example.SSS.entity.Exam;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface ExamMapper {

    @Select("SELECT e.*, c.name as courseName , ad.username as creator_name ,ei.teacher_id teacher_id FROM exam e LEFT JOIN course c ON e.course_id = c.id LEFT JOIN admin ad ON ad.id = e.creator_id LEFT JOIN exam_invigilator ei ON ei.exam_id = e.id")
    @Results({
        @Result(property = "courseId", column = "course_id"),
        @Result(property = "startTime", column = "start_time"),
        @Result(property = "endTime", column = "end_time"),
        @Result(property = "examType", column = "exam_type"),
        @Result(property = "examFormat", column = "exam_format"),
        @Result(property = "capacity", column = "capacity"),
        @Result(property = "totalScore", column = "total_score"),
        @Result(property = "notes", column = "notes"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "creatorId", column = "creator_id"),
            @Result(property = "creatorName", column = "creator_name"),
            @Result(property = "teacherId",column = "teacher_id")

    })
    List<Exam> selectAll();

    @Select("SELECT e.*, c.name as courseName ,ei.teacher_id teacher_id FROM exam e LEFT JOIN course c ON e.course_id = c.id LEFT JOIN exam_invigilator ei ON ei.exam_id = e.id WHERE e.id = #{id} ")
    @Results({
        @Result(property = "courseId", column = "course_id"),
        @Result(property = "startTime", column = "start_time"),
        @Result(property = "endTime", column = "end_time"),
        @Result(property = "examType", column = "exam_type"),
        @Result(property = "examFormat", column = "exam_format"),
        @Result(property = "capacity", column = "capacity"),
        @Result(property = "totalScore", column = "total_score"),
        @Result(property = "notes", column = "notes"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "creatorId", column = "creator_id"),
            @Result(property = "creatorName", column = "creatorName"),
            @Result(property = "teacherId",column = "teacher_id")
    })
    Exam selectById(Integer id);

    /**
     * 分页查询学生的考试列表
     */
    @Select("<script>" +
            "SELECT e.id, e.name, e.course_id, c.name as courseName, e.exam_type, e.exam_format, " +
            "e.start_time as startTime, e.end_time as endTime, e.location, e.total_score as totalScore, e.notes " +
            "FROM exam e " +
            "LEFT JOIN course c ON e.course_id = c.id " +
            "LEFT JOIN exam_student es ON e.id = es.exam_id " +
            "WHERE es.student_id = #{studentId} " +
            "<if test='name != null and name != \"\"'>" +
            "  AND e.name LIKE CONCAT('%', #{name}, '%') " +
            "</if>" +
            "<if test='examType != null and examType != \"\"'>" +
            "  AND e.exam_type = #{examType} " +
            "</if>" +
            "<if test='status != null and status != \"\"'>" +
            "  AND es.status = #{status} " +
            "</if>" +
            "<if test='startDate != null and startDate != \"\"'>" +
            "  AND e.start_time &gt;= #{startDate} " +
            "</if>" +
            "<if test='endDate != null and endDate != \"\"'>" +
            "  AND e.start_time &lt;= #{endDate} " +
            "</if>" +
            "ORDER BY e.start_time ASC " +
            "LIMIT #{offset}, #{limit}" +
            "</script>")
    List<Map<String, Object>> selectStudentExamsByPage(Map<String, Object> params);

    /**
     * 统计学生的考试数量
     */
    @Select("<script>" +
            "SELECT COUNT(*) " +
            "FROM exam e " +
            "LEFT JOIN exam_student es ON e.id = es.exam_id " +
            "WHERE es.student_id = #{studentId} " +
            "<if test='name != null and name != \"\"'>" +
            "  AND e.name LIKE CONCAT('%', #{name}, '%') " +
            "</if>" +
            "<if test='examType != null and examType != \"\"'>" +
            "  AND e.exam_type = #{examType} " +
            "</if>" +
            "<if test='status != null and status != \"\"'>" +
            "  AND es.status = #{status} " +
            "</if>" +
            "<if test='startDate != null and startDate != \"\"'>" +
            "  AND e.start_time &gt;= #{startDate} " +
            "</if>" +
            "<if test='endDate != null and endDate != \"\"'>" +
            "  AND e.start_time &lt;= #{endDate} " +
            "</if>" +
            "</script>")
    int countStudentExams(Map<String, Object> params);

    @Insert("INSERT INTO exam(name, course_id, exam_type, exam_format, start_time, end_time, location, " +
            "capacity, total_score, notes) " +
            "VALUES(#{name}, #{courseId}, #{examType}, #{examFormat}, #{startTime}, #{endTime}, " +
            "#{location}, #{capacity}, #{totalScore}, #{notes})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Exam exam);

    @Update("UPDATE exam SET name = #{name}, course_id = #{courseId}, exam_type = #{examType}, " +
            "exam_format = #{examFormat}, start_time = #{startTime}, end_time = #{endTime}, " +
            "location = #{location}, capacity = #{capacity}, total_score = #{totalScore}, " +
            "notes = #{notes} WHERE id = #{id}")
    int update(Exam exam);

    @Delete("DELETE FROM exam WHERE id = #{id}")
    int deleteById(Integer id);

    @Select("SELECT e.*, c.name as courseName FROM exam e " +
            "LEFT JOIN course c ON e.course_id = c.id " +
            "WHERE e.name LIKE CONCAT('%', #{keyword}, '%') " +
            "OR c.name LIKE CONCAT('%', #{keyword}, '%')")
    @Results({
        @Result(property = "courseId", column = "course_id"),
        @Result(property = "startTime", column = "start_time"),
        @Result(property = "endTime", column = "end_time"),
        @Result(property = "examType", column = "exam_type"),
        @Result(property = "examFormat", column = "exam_format"),
        @Result(property = "capacity", column = "capacity"),
        @Result(property = "totalScore", column = "total_score"),
        @Result(property = "notes", column = "notes"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "creatorId", column = "creator_id"),
            @Result(property = "creatorName", column = "creatorName")
    })
    List<Exam> search(String keyword);

    @Select("SELECT e.*, c.name as courseName, ei.is_main as isMain " +
            "FROM exam e " +
            "LEFT JOIN course c ON e.course_id = c.id " +
            "LEFT JOIN exam_invigilator ei ON e.id = ei.exam_id " +
            "WHERE ei.teacher_id = #{teacherId}")
    @Results({
        @Result(property = "courseId", column = "course_id"),
        @Result(property = "startTime", column = "start_time"),
        @Result(property = "endTime", column = "end_time"),
        @Result(property = "examType", column = "exam_type"),
        @Result(property = "examFormat", column = "exam_format"),
        @Result(property = "capacity", column = "capacity"),
        @Result(property = "totalScore", column = "total_score"),
        @Result(property = "notes", column = "notes"),
        @Result(property = "isMain", column = "is_main"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "creatorId", column = "creator_id"),
            @Result(property = "creatorName", column = "creatorName")
    })
    List<Exam> selectByTeacherId(Integer teacherId);

    @Select("SELECT e.*, c.name as courseName, ei.is_main as isMain " +
            "FROM exam e " +
            "LEFT JOIN course c ON e.course_id = c.id " +
            "LEFT JOIN exam_invigilator ei ON e.id = ei.exam_id " +
            "LEFT JOIN admin ad ON ad.id = e.creator_id"+
            "WHERE ei.teacher_id = #{teacherId} " +
            "AND (e.name LIKE CONCAT('%', #{keyword}, '%') " +
            "OR c.name LIKE CONCAT('%', #{keyword}, '%'))")
    @Results({
        @Result(property = "courseId", column = "course_id"),
        @Result(property = "startTime", column = "start_time"),
        @Result(property = "endTime", column = "end_time"),
        @Result(property = "examType", column = "exam_type"),
        @Result(property = "examFormat", column = "exam_format"),
        @Result(property = "capacity", column = "capacity"),
        @Result(property = "totalScore", column = "total_score"),
        @Result(property = "notes", column = "notes"),
        @Result(property = "isMain", column = "is_main"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "creatorId", column = "creator_id"),
            @Result(property = "creatorName", column = "username")
    })
    List<Exam> searchByTeacherId(Integer teacherId, String keyword);
} 