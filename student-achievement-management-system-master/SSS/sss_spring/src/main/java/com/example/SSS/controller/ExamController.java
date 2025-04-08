package com.example.SSS.controller;

import com.example.SSS.entity.Exam;
import com.example.SSS.entity.ExamInvigilator;
import com.example.SSS.entity.ExamStudent;
import com.example.SSS.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.sql.Timestamp;

@RestController
@RequestMapping("/exam")
public class ExamController {

    @Autowired
    private ExamService examService;
    
    /**
     * 获取所有考试
     */
    @GetMapping
    public List<Exam> getAllExams() {
        return examService.getAllExams();
    }
    
    /**
     * 根据ID获取考试
     */
    @GetMapping("/{id}")
    public Exam getExamById(@PathVariable Integer id) {
        return examService.getExamById(id);
    }
    
    /**
     * 搜索考试
     */
    @GetMapping("/search")
    public List<Exam> searchExams(@RequestParam String keyword) {
        return examService.searchExams(keyword);
    }
    
    /**
     * 添加考试
     */
    @PostMapping
    public Exam addExam(@RequestBody Map<String, Object> params) {
        try {
            System.out.println("接收到的参数: " + params); // 添加日志
            
            Exam exam = mapToExam(params);
            System.out.println("转换后的Exam对象: " + exam); // 添加日志
            
            @SuppressWarnings("unchecked")
            List<Integer> studentIds = (List<Integer>) params.get("selectedStudents");
            System.out.println("学生ID列表: " + studentIds); // 添加日志
            
            @SuppressWarnings("unchecked")
            Map<String, Boolean> invigilatorsMap = (Map<String, Boolean>) params.get("invigilators");
            System.out.println("监考教师信息: " + invigilatorsMap); // 添加日志
            
            // 转换监考教师信息
            Map<Integer, Boolean> invigilators = new HashMap<>();
            if (invigilatorsMap != null) {
                for (Map.Entry<String, Boolean> entry : invigilatorsMap.entrySet()) {
                    invigilators.put(Integer.parseInt(entry.getKey()), entry.getValue());
                }
            }
            
            return examService.addExam(exam, studentIds, invigilators);
        } catch (Exception e) {
            System.err.println("添加考试时发生错误: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("添加考试失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新考试
     */
    @PutMapping("/{id}")
    public Exam updateExam(@PathVariable Integer id, @RequestBody Map<String, Object> params) {
        Exam exam = mapToExam(params);
        exam.setId(id);
        
        @SuppressWarnings("unchecked")
        List<Integer> studentIds = (List<Integer>) params.get("studentIds");

        System.out.println("param:::" + params);

        @SuppressWarnings("unchecked")
        Integer invigilatorId =(Integer) params.get("teacherId");
        System.out.println("监考教师信息: " + invigilatorId); // 添加日志
        
        // 转换监考教师信息
//        Map<Integer, Boolean> invigilators = new HashMap<>();
//        if (invigilatorsMap != null) {
//            for (Map.Entry<String, Boolean> entry : invigilatorsMap.entrySet()) {
//                invigilators.put(Integer.parseInt(entry.getKey()), entry.getValue());
//            }
//        }
        
        return examService.updateExam(exam, studentIds,invigilatorId);
    }
    
    /**
     * 删除考试
     */
    @DeleteMapping("/{id}")
    public boolean deleteExam(@PathVariable Integer id) {
        return examService.deleteExam(id);
    }
    
    /**
     * 获取考试的考生列表
     */
    @GetMapping("/{id}/students")
    public List<ExamStudent> getExamStudents(@PathVariable Integer id) {
        return examService.getExamStudents(id);
    }
    
    /**
     * 获取考试的监考教师列表
     */
    @GetMapping("/{id}/invigilators")
    public List<ExamInvigilator> getExamInvigilators(@PathVariable Integer id) {
        return examService.getExamInvigilators(id);
    }
    
    /**
     * 安排座位
     */
    @PostMapping("/{id}/arrange-seat")
    public boolean arrangeSeat(@PathVariable Integer id, @RequestBody Map<String, Integer> seatMap) {
        Map<Integer, Integer> convertedMap = new HashMap<>();
        for (Map.Entry<String, Integer> entry : seatMap.entrySet()) {
            convertedMap.put(Integer.parseInt(entry.getKey()), entry.getValue());
        }
        return examService.arrangeSeat(id, convertedMap);
    }
    
    /**
     * 随机排座
     */
    @PostMapping("/{id}/random-seat")
    public boolean randomSeat(@PathVariable Integer id) {
        return examService.randomSeat(id);
    }
    
    /**
     * 获取教师的考试列表
     */
    @GetMapping("/teacher/{teacherId}")
    public List<Exam> getTeacherExams(@PathVariable Integer teacherId) {
        return examService.getTeacherExams(teacherId);
    }
    
    /**
     * 搜索教师的考试
     */
    @GetMapping("/teacher/{teacherId}/search")
    public List<Exam> searchTeacherExams(@PathVariable Integer teacherId, @RequestParam String keyword) {
        return examService.searchTeacherExams(teacherId, keyword);
    }
    
    /**
     * 将Map转换为Exam对象
     */
    private Exam mapToExam(Map<String, Object> params) {
        try {
            System.out.println("开始转换Exam对象，参数: " + params);
            Exam exam = new Exam();
            
            if (params.containsKey("examId")) {
                Object examId = params.get("examId");
                System.out.println("examId类型: " + (examId != null ? examId.getClass() : "null"));
                if (examId != null) {
                    try {
                        exam.setId(Integer.parseInt(examId.toString()));
                    } catch (NumberFormatException e) {
                        System.err.println("examId转换失败: " + e.getMessage());
                    }
                }
            }
            
            if (params.containsKey("examName")) {
                exam.setName((String) params.get("examName"));
            }
            
            if (params.containsKey("courseId")) {
                Object courseId = params.get("courseId");
                System.out.println("courseId类型: " + (courseId != null ? courseId.getClass() : "null"));
                if (courseId != null) {
                    try {
                        exam.setCourseId(Integer.parseInt(courseId.toString()));
                    } catch (NumberFormatException e) {
                        System.err.println("courseId转换失败: " + e.getMessage());
                    }
                }
            }
            
            if (params.containsKey("teacherId")) {
                Object teacherId = params.get("teacherId");
                System.out.println("teacherId类型: " + (teacherId != null ? teacherId.getClass() : "null"));
                if (teacherId != null) {
                    try {
                        exam.setTeacherId(Integer.parseInt(teacherId.toString()));
                    } catch (NumberFormatException e) {
                        System.err.println("teacherId转换失败: " + e.getMessage());
                    }
                }
            }
            
            if (params.containsKey("examType")) {
                exam.setExamType((String) params.get("examType"));
            }
            
            if (params.containsKey("examFormat")) {
                exam.setExamFormat((String) params.get("examFormat"));
            }
            
            if (params.containsKey("startTime")) {
                try {
                    String startTimeStr = (String) params.get("startTime");
                    System.out.println("开始时间字符串: " + startTimeStr);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime startTime = LocalDateTime.parse(startTimeStr, formatter);
                    exam.setStartTime(Timestamp.valueOf(startTime));
                } catch (Exception e) {
                    System.err.println("解析开始时间失败: " + e.getMessage());
                    throw e;
                }
            }
            
            if (params.containsKey("endTime")) {
                try {
                    String endTimeStr = (String) params.get("endTime");
                    System.out.println("结束时间字符串: " + endTimeStr);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime endTime = LocalDateTime.parse(endTimeStr, formatter);
                    exam.setEndTime(Timestamp.valueOf(endTime));
                } catch (Exception e) {
                    System.err.println("解析结束时间失败: " + e.getMessage());
                    throw e;
                }
            }
            
            if (params.containsKey("examLocation")) {
                exam.setLocation((String) params.get("examLocation"));
            }

            if (params.containsKey("capacity")) {
                Object capacity = params.get("capacity");
                System.out.println("capacity类型: " + (capacity != null ? capacity.getClass() : "null"));
                if (capacity != null) {
                    try {
                        exam.setCapacity(Integer.parseInt(capacity.toString()));
                    } catch (NumberFormatException e) {
                        System.err.println("capacity转换失败: " + e.getMessage());
                    }
                }
            }

            if (params.containsKey("totalScore")) {
                Object totalScore = params.get("totalScore");
                System.out.println("totalScore类型: " + (totalScore != null ? totalScore.getClass() : "null"));
                if (totalScore != null) {
                    try {
                        exam.setTotalScore(Integer.parseInt(totalScore.toString()));
                    } catch (NumberFormatException e) {
                        System.err.println("totalScore转换失败: " + e.getMessage());
                    }
                }
            }
            
            if (params.containsKey("notes")) {
                exam.setNotes((String) params.get("notes"));
            }
            
            if (params.containsKey("examDuration")) {
                Object duration = params.get("examDuration");
                System.out.println("duration类型: " + (duration != null ? duration.getClass() : "null"));
                if (duration != null && !duration.toString().isEmpty()) {
                    try {
                        exam.setDuration(Integer.parseInt(duration.toString()));
                    } catch (NumberFormatException e) {
                        System.err.println("duration转换失败: " + e.getMessage());
                    }
                }
            }
            
            if (params.containsKey("examStatus")) {
                exam.setStatus((String) params.get("examStatus"));
            }
            
            if (params.containsKey("examDescription")) {
                exam.setDescription((String) params.get("examDescription"));
            }
            
            System.out.println("转换后的Exam对象: " + exam);
            return exam;
        } catch (Exception e) {
            System.err.println("转换Exam对象时发生错误: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
} 