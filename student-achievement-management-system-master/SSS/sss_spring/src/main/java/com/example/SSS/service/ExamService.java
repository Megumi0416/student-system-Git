package com.example.SSS.service;

import com.example.SSS.entity.Exam;
import com.example.SSS.entity.ExamInvigilator;
import com.example.SSS.entity.ExamStudent;
import com.example.SSS.mapper.ExamInvigilatorMapper;
import com.example.SSS.mapper.ExamMapper;
import com.example.SSS.mapper.ExamStudentMapper;
import com.example.mapper.CourseMapper;
import com.example.mapper.TeacherMapper;
import com.example.entity.Course;
import com.example.entity.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

@Service
public class ExamService {

    @Autowired
    private ExamMapper examMapper;
    
    @Autowired
    private ExamStudentMapper examStudentMapper;
    
    @Autowired
    private ExamInvigilatorMapper examInvigilatorMapper;
    
    @Autowired
    private CourseMapper courseMapper;
    
    @Autowired
    private TeacherMapper teacherMapper;
    
    /**
     * 获取所有考试
     */
    public List<Exam> getAllExams() {
        List<Exam> res = examMapper.selectAll();
        System.out.println("__________________________________");
        System.out.println(res);
        System.out.println("???????????????????????????????");
        return res;
    }
    
    /**
     * 根据ID获取考试
     */
    public Exam getExamById(Integer id) {
        return examMapper.selectById(id);
    }
    
    /**
     * 搜索考试
     */
    public List<Exam> searchExams(String keyword) {
        return examMapper.search(keyword);
    }
    
    /**
     * 按分页条件查询学生的考试列表
     * @param params 查询参数，包含studentId、name、examType、status、startDate、endDate等
     * @return 分页结果，包含list（考试列表）和total（总数）
     */
    public Map<String, Object> getStudentExamsByPage(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 参数校验
            Integer studentId = (Integer) params.get("studentId");
            if (studentId == null || studentId <= 0) {
                System.err.println("无效的学生ID: " + studentId);
                result.put("list", new ArrayList<>());
                result.put("total", 0);
                result.put("pageNum", 1);
                result.put("pageSize", 10);
                return result;
            }
            
            // 计算分页参数
            Integer pageNum = (Integer) params.get("pageNum");
            Integer pageSize = (Integer) params.get("pageSize");
            if (pageNum == null) pageNum = 1;
            if (pageSize == null) pageSize = 10;
            
            // 计算偏移量
            Integer offset = (pageNum - 1) * pageSize;
            params.put("offset", offset);
            params.put("limit", pageSize);
            
            // 查询符合条件的考试列表
            List<Map<String, Object>> exams = examMapper.selectStudentExamsByPage(params);
            if (exams == null) {
                exams = new ArrayList<>();
            }
            
            // 查询符合条件的考试总数
            int total = 0;
            try {
                total = examMapper.countStudentExams(params);
            } catch (Exception e) {
                System.err.println("统计考试数量失败: " + e.getMessage());
            }
            
            // 对每个考试记录添加座位号信息
            for (Map<String, Object> exam : exams) {
                try {
                    Integer examId = (Integer) exam.get("id");
                    
                    // 查询学生在该考试中的座位号
                    ExamStudent examStudent = getStudentExamInfo(examId, studentId);
                    if (examStudent != null) {
                        exam.put("seatNo", examStudent.getSeatNo());
                        exam.put("status", examStudent.getStatus());
                    } else {
                        exam.put("seatNo", null);
                        exam.put("status", "NOT_ARRANGED");
                    }
                } catch (Exception e) {
                    System.err.println("处理考试信息失败: " + e.getMessage());
                    exam.put("seatNo", null);
                    exam.put("status", "UNKNOWN");
                }
            }
            
            result.put("list", exams);
            result.put("total", total);
            result.put("pageNum", pageNum);
            result.put("pageSize", pageSize);
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("查询学生考试列表失败: " + e.getMessage());
            result.put("list", new ArrayList<>());
            result.put("total", 0);
            result.put("pageNum", 1);
            result.put("pageSize", 10);
        }
        
        return result;
    }
    
    /**
     * 获取学生在特定考试中的信息
     * @param examId 考试ID
     * @param studentId 学生ID
     * @return 学生考试信息
     */
    public ExamStudent getStudentExamInfo(Integer examId, Integer studentId) {
        return examStudentMapper.selectByExamAndStudentId(examId, studentId);
    }
    
    /**
     * 添加考试
     */
    @Transactional
    public Exam addExam(Exam exam, List<Integer> studentIds, Map<Integer, Boolean> invigilators) {
        // 保存考试信息
        examMapper.insert(exam);
        Integer examId = exam.getId();
        
        // 添加考生
        if (studentIds != null && !studentIds.isEmpty()) {
            for (Integer studentId : studentIds) {
                ExamStudent examStudent = new ExamStudent();
                examStudent.setExamId(examId);
                examStudent.setStudentId(studentId);
                examStudent.setStatus("NOT_STARTED");
                examStudentMapper.insert(examStudent);
            }
        }
        
        // 添加监考教师
        if (invigilators != null && !invigilators.isEmpty()) {
            for (Map.Entry<Integer, Boolean> entry : invigilators.entrySet()) {
                ExamInvigilator invigilator = new ExamInvigilator();
                invigilator.setExamId(examId);
                invigilator.setTeacherId(entry.getKey());
//                invigilator.setIsMain(entry.getValue());
                examInvigilatorMapper.insert(invigilator);
            }
        }
        
        return exam;
    }
    
    /**
     * 更新考试
     */
    @Transactional
    public Exam updateExam(Exam exam, List<Integer> studentIds, Integer invigilators) {
        // 更新考试信息
        examMapper.update(exam);
        Integer examId = exam.getId();
        
        // 删除原有考生和监考教师
        examStudentMapper.deleteByExamId(examId);
        examInvigilatorMapper.deleteByExamId(examId);
        
        // 添加考生
        if (studentIds != null && !studentIds.isEmpty()) {
            for (Integer studentId : studentIds) {
                ExamStudent examStudent = new ExamStudent();
                examStudent.setExamId(examId);
                examStudent.setStudentId(studentId);
                examStudent.setStatus("NOT_STARTED");
                examStudentMapper.insert(examStudent);
            }
        }
        
        // 添加监考教师
        if (invigilators != null ) {
//            for (Map.Entry<Integer, Boolean> entry : invigilators.entrySet()) {
//                ExamInvigilator invigilator = new ExamInvigilator();
//                invigilator.setExamId(examId);
//                invigilator.setTeacherId(entry.getKey());
//
//                examInvigilatorMapper.insert(invigilator);
//            }
            ExamInvigilator invigilator = new ExamInvigilator();
            invigilator.setExamId(examId);
            invigilator.setTeacherId(invigilators);
            invigilator.setIsMain(true);
            examInvigilatorMapper.insert(invigilator);
        }
        
        return exam;
    }
    
    /**
     * 删除考试
     */
    @Transactional
    public boolean deleteExam(Integer id) {
        // 删除考生和监考教师
        examStudentMapper.deleteByExamId(id);
        examInvigilatorMapper.deleteByExamId(id);
        
        // 删除考试
        return examMapper.deleteById(id) > 0;
    }
    
    /**
     * 获取考试的考生列表
     */
    public List<ExamStudent> getExamStudents(Integer examId) {
        return examStudentMapper.selectByExamId(examId);
    }
    
    /**
     * 获取考试的监考教师列表
     */
    public List<ExamInvigilator> getExamInvigilators(Integer examId) {
        return examInvigilatorMapper.selectByExamId(examId);
    }
    
    /**
     * 安排座位
     */
    @Transactional
    public boolean arrangeSeat(Integer examId, Map<Integer, Integer> seatMap) {
        List<ExamStudent> students = examStudentMapper.selectByExamId(examId);
        
        for (ExamStudent student : students) {
            if (seatMap.containsKey(student.getId())) {
                student.setSeatNo(seatMap.get(student.getId()));
                examStudentMapper.update(student);
            }
        }
        
        return true;
    }
    
    /**
     * 随机排座
     */
    @Transactional
    public boolean randomSeat(Integer examId) {
        List<ExamStudent> students = examStudentMapper.selectByExamId(examId);
        
        // 随机打乱学生顺序
        java.util.Collections.shuffle(students);
        
        // 分配座位号
        int seatNo = 1;
        for (ExamStudent student : students) {
            student.setSeatNo(seatNo++);
            examStudentMapper.update(student);
        }
        
        return true;
    }
    
    /**
     * 获取教师的考试列表
     */
    public List<Exam> getTeacherExams(Integer teacherId) {
        return examMapper.selectByTeacherId(teacherId);
    }
    
    /**
     * 搜索教师的考试
     */
    public List<Exam> searchTeacherExams(Integer teacherId, String keyword) {
        return examMapper.searchByTeacherId(teacherId, keyword);
    }
    
    /**
     * 获取学生的考试列表（分页）
     * @param studentId 学生ID
     * @param name 考试名称
     * @param examType 考试类型
     * @param status 考试状态
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 分页结果
     */
    public Map<String, Object> getStudentExams(
            Integer studentId, String name, String examType, String status,
            String startDate, String endDate, Integer pageNum, Integer pageSize) {
        
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("studentId", studentId);
            params.put("name", name);
            params.put("examType", examType);
            params.put("status", status);
            params.put("startDate", startDate);
            params.put("endDate", endDate);
            params.put("pageNum", pageNum);
            params.put("pageSize", pageSize);
            
            return getStudentExamsByPage(params);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("准备学生考试查询参数失败: " + e.getMessage());
            
            Map<String, Object> result = new HashMap<>();
            result.put("list", new ArrayList<>());
            result.put("total", 0);
            result.put("pageNum", pageNum != null ? pageNum : 1);
            result.put("pageSize", pageSize != null ? pageSize : 10);
            return result;
        }
    }
    
    /**
     * 获取学生特定考试的详细信息
     * @param examId 考试ID
     * @param studentId 学生ID
     * @return 考试详情
     */
    public Map<String, Object> getStudentExamDetail(Integer examId, Integer studentId) {
        try {
            // 获取考试基本信息
            Exam exam = examMapper.selectById(examId);
            if (exam == null) {
                System.err.println("未找到考试信息，examId: " + examId);
                return null;
            }
            
            // 获取学生在该考试中的信息（座位号等）
            ExamStudent examStudent = getStudentExamInfo(examId, studentId);
            if (examStudent == null) {
                // 学生未被安排参加此考试
                System.err.println("学生未被安排参加此考试，examId: " + examId + ", studentId: " + studentId);
                return null;
            }
            
            // 获取考试监考教师信息
            List<ExamInvigilator> invigilators = getExamInvigilators(examId);
            
            // 构建考试详情返回数据
            Map<String, Object> examDetail = new HashMap<>();
            examDetail.put("id", exam.getId());
            examDetail.put("name", exam.getName());
            examDetail.put("courseId", exam.getCourseId());
            
            // 安全获取课程名称
            String courseName = "未知课程";
            try {
                courseName = getCourseNameById(exam.getCourseId());
            } catch (Exception e) {
                System.err.println("获取课程名称失败: " + e.getMessage());
            }
            examDetail.put("courseName", courseName);
            
            examDetail.put("examType", exam.getExamType());
            examDetail.put("examFormat", exam.getExamFormat());
            examDetail.put("startTime", exam.getStartTime());
            examDetail.put("endTime", exam.getEndTime());
            examDetail.put("location", exam.getLocation());
            examDetail.put("totalScore", exam.getTotalScore());
            examDetail.put("notes", exam.getNotes());
            examDetail.put("seatNo", examStudent.getSeatNo());
            examDetail.put("status", examStudent.getStatus());
            
            // 格式化监考教师信息
            if (invigilators != null && !invigilators.isEmpty()) {
                List<String> invigilatorNames = new ArrayList<>();
                for (ExamInvigilator invigilator : invigilators) {
                    // 安全获取教师名称
                    try {
                        String teacherName = getTeacherNameById(invigilator.getTeacherId());
                        if (teacherName != null) {
                            invigilatorNames.add(teacherName);
                        }
                    } catch (Exception e) {
                        System.err.println("获取教师名称失败: " + e.getMessage());
                    }
                }
                
                if (!invigilatorNames.isEmpty()) {
                    examDetail.put("invigilators", String.join("、", invigilatorNames));
                } else {
                    examDetail.put("invigilators", "待安排");
                }
            } else {
                examDetail.put("invigilators", "待安排");
            }
            
            return examDetail;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("获取学生考试详情失败: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * 根据课程ID获取课程名称
     */
    private String getCourseNameById(Integer courseId) {
        if (courseId == null) return "未知课程";
        
        try {
            Course course = courseMapper.selectById(courseId);
            return course != null ? course.getName() : "未知课程";
        } catch (Exception e) {
            System.err.println("查询课程名称失败，课程ID: " + courseId + ", 错误: " + e.getMessage());
            return "未知课程";
        }
    }
    
    /**
     * 根据教师ID获取教师姓名
     */
    private String getTeacherNameById(Integer teacherId) {
        if (teacherId == null) return "未知教师";
        
        try {
            Teacher teacher = teacherMapper.selectById(teacherId);
            return teacher != null ? teacher.getName() : "未知教师";
        } catch (Exception e) {
            System.err.println("查询教师姓名失败，教师ID: " + teacherId + ", 错误: " + e.getMessage());
            return "未知教师";
        }
    }
} 