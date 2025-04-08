package com.example.SSS.service;

import com.example.SSS.entity.Exam;
import com.example.SSS.entity.ExamInvigilator;
import com.example.SSS.entity.ExamStudent;
import com.example.SSS.mapper.ExamInvigilatorMapper;
import com.example.SSS.mapper.ExamMapper;
import com.example.SSS.mapper.ExamStudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class ExamService {

    @Autowired
    private ExamMapper examMapper;
    
    @Autowired
    private ExamStudentMapper examStudentMapper;
    
    @Autowired
    private ExamInvigilatorMapper examInvigilatorMapper;
    
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
} 