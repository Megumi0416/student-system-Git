<template>
  <div class="arrange-exam-container">
    <div style="display: flex; justify-content: space-between; margin-bottom: 20px">
      <div>
        <el-button type="primary" @click="handleAdd">安排新考试</el-button>
      </div>
      <div style="display: flex">
        <el-input v-model="searchText" placeholder="请输入考试名称或课程" style="width: 300px; margin-right: 10px"></el-input>
        <el-button type="primary" @click="searchExams">搜索</el-button>
      </div>
    </div>

    <!-- 考试列表 -->
    <el-table
      :data="examList"
      border
      stripe
      style="width: 100%">
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="name" label="考试名称" width="120"></el-table-column>
      <el-table-column prop="courseName" label="考试科目" width="120"></el-table-column>
      <el-table-column prop="examType" label="考试类型" width="100">
        <template #default="scope">
          <el-tag v-if="scope.row.examType === 'midterm'">期中</el-tag>
          <el-tag v-else-if="scope.row.examType === 'final'" type="success">期末</el-tag>
          <el-tag v-else-if="scope.row.examType === 'makeup'" type="warning">补考</el-tag>
          <el-tag v-else type="info">其他</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="examFormat" label="考试形式" width="100">
        <template #default="scope">
          <el-tag v-if="scope.row.examFormat === 'closed'" type="danger">闭卷</el-tag>
          <el-tag v-else-if="scope.row.examFormat === 'open'" type="success">开卷</el-tag>
          <el-tag v-else-if="scope.row.examFormat === 'computer'" type="primary">机考</el-tag>
          <el-tag v-else type="info">其他</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="startTime" label="开始时间" width="160">
        <template #default="scope">
          {{ formatDateTime(scope.row.startTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="endTime" label="结束时间" width="160">
        <template #default="scope">
          {{ formatDateTime(scope.row.endTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="location" label="考试地点" width="120"></el-table-column>
      <el-table-column prop="capacity" label="考试容量" width="100"></el-table-column>
      <el-table-column prop="totalScore" label="总分" width="80"></el-table-column>
      <el-table-column prop="notes" label="注意事项" show-overflow-tooltip></el-table-column>
      <el-table-column prop="creatorName" label="创建人" width="100"></el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="160">
        <template #default="scope">
          {{ formatDateTime(scope.row.createTime) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="scope">
          <el-button type="primary" link @click="handleEdit(scope.row)">编辑</el-button>
          <el-button type="primary" link @click="handleViewStudents(scope.row)">查看考生</el-button>
          <el-button type="primary" link @click="handleArrangeSeat(scope.row)">安排座位</el-button>
          <el-button type="danger" link @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 安排考试对话框 -->
    <el-dialog 
      v-model="dialogVisible" 
      title="安排考试" 
      width="700px"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      @close="resetForm"
    >
      <el-form :model="examForm" :rules="examRules" ref="examFormRef" label-width="100px">
        <el-tabs v-model="activeTab">
          <el-tab-pane label="基本信息" name="basic">
            <el-form-item label="考试名称" prop="examName">
              <el-input v-model="examForm.examName" placeholder="请输入考试名称"></el-input>
            </el-form-item>
            <el-form-item label="考试科目" prop="courseId">
              <el-select v-model="examForm.courseId" placeholder="请选择科目" style="width: 100%">
                <el-option v-for="course in courseList" :key="course.id" :label="course.name" :value="course.id"></el-option>
              </el-select>
            </el-form-item>
            <el-row :gutter="20">
              <el-col :span="8">
                <el-form-item label="考试类型">
                  <el-select v-model="examForm.examType" placeholder="请选择类型" style="width: 100%">
                    <el-option label="期中考试" value="midterm"></el-option>
                    <el-option label="期末考试" value="final"></el-option>
                    <el-option label="补考" value="makeup"></el-option>
                    <el-option label="其他" value="other"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="考试形式">
                  <el-select v-model="examForm.examFormat" placeholder="请选择形式" style="width: 100%">
                    <el-option label="闭卷" value="closed"></el-option>
                    <el-option label="开卷" value="open"></el-option>
                    <el-option label="机考" value="computer"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="总分">
                  <el-input-number v-model="examForm.totalScore" :min="0" :max="200" style="width: 100%"></el-input-number>
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="考试时间" prop="examTime">
              <el-date-picker
                v-model="examForm.examTime"
                type="datetimerange"
                range-separator="至"
                start-placeholder="开始时间"
                end-placeholder="结束时间"
                format="YYYY-MM-DD HH:mm"
                value-format="YYYY-MM-DD HH:mm:ss"
                style="width: 100%"
                :teleported="true"
              ></el-date-picker>
            </el-form-item>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="考试地点" prop="examLocation">
                  <el-input v-model="examForm.examLocation" placeholder="请输入考试地点"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="考试容量">
                  <el-input-number v-model="examForm.capacity" :min="1" :max="200" style="width: 100%"></el-input-number>
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="考试说明">
              <el-input 
                v-model="examForm.notes" 
                type="textarea" 
                :rows="4" 
                placeholder="请输入考试注意事项、规则等内容"
              ></el-input>
            </el-form-item>
          </el-tab-pane>
          
          <el-tab-pane label="考生安排" name="students">
            <el-form-item label="选择班级">
              <el-select
                v-model="selectedClasses"
                multiple
                placeholder="请选择班级"
                style="width: 100%"
                @change="handleClassChange"
              >
                <el-option 
                  v-for="grade in ['2019', '2020', '2021', '2022', '2023', '2024']" 
                  :key="grade" 
                  :label="grade + '级'"
                  :value="grade"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-table :data="studentList" style="width: 100%" height="250px" @selection-change="handleStudentSelection">
              <el-table-column type="selection" width="55"></el-table-column>
              <el-table-column prop="username" label="学号" width="120"></el-table-column>
              <el-table-column prop="name" label="姓名" width="120"></el-table-column>
              <el-table-column prop="grade" label="年级"></el-table-column>
              <el-table-column prop="college" label="学院"></el-table-column>
            </el-table>
          </el-tab-pane>
          
          <el-tab-pane label="监考安排" name="invigilators">
            <el-form-item label="主监考">
              <el-select v-model="mainInvigilator" placeholder="请选择主监考" style="width: 100%">
                <el-option 
                  v-for="teacher in teacherList" 
                  :key="teacher.id" 
                  :label="teacher.name" 
                  :value="teacher.id"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="副监考">
              <el-select v-model="assistantInvigilators" multiple placeholder="请选择副监考" style="width: 100%">
                <el-option 
                  v-for="teacher in teacherList" 
                  :key="teacher.id" 
                  :label="teacher.name" 
                  :value="teacher.id"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-tab-pane>
        </el-tabs>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveExam">保存</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 查看考生对话框 -->
    <el-dialog v-model="studentDialogVisible" :title="currentExam?.name + ' - 考生列表'" width="800px">
      <el-table :data="examStudentList" style="width: 100%" border stripe>
        <el-table-column prop="studentUsername" label="学号" width="120"></el-table-column>
        <el-table-column prop="studentName" label="姓名" width="100"></el-table-column>
        <el-table-column prop="seatNo" label="座位号" width="80"></el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag v-if="scope.row.status === 'NOT_STARTED'" type="info">未开始</el-tag>
            <el-tag v-else-if="scope.row.status === 'ABSENT'" type="danger">缺席</el-tag>
            <el-tag v-else-if="scope.row.status === 'PRESENT'" type="success">到场</el-tag>
            <el-tag v-else-if="scope.row.status === 'FINISHED'" type="primary">已完成</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <!-- 排座对话框 -->
    <el-dialog v-model="seatDialogVisible" :title="currentExam?.name + ' - 座位编排'" width="800px">
      <div style="margin-bottom: 20px">
        <el-button type="primary" @click="randomSeat">随机排座</el-button>
        <el-button type="success" @click="sortByStudentId">按学号排座</el-button>
        <el-button @click="resetSeat">重置座位</el-button>
      </div>
      <el-table :data="examStudentList" style="width: 100%" border stripe>
        <el-table-column prop="studentUsername" label="学号" width="120"></el-table-column>
        <el-table-column prop="studentName" label="姓名" width="100"></el-table-column>
        <el-table-column prop="seatNo" label="座位号" width="100">
          <template #default="scope">
            <el-input-number v-model="scope.row.seatNo" :min="1" :controls="false" size="small"></el-input-number>
          </template>
        </el-table-column>
      </el-table>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="seatDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveSeat">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { reactive, ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'  // 导入request实例

export default {
  name: 'ArrangeExam',
  setup() {
    // 数据
    const examList = ref([])
    const courseList = ref([])
    const teacherList = ref([])
    const studentList = ref([])
    const examStudentList = ref([])
    const searchText = ref('')
    const dialogVisible = ref(false)
    const studentDialogVisible = ref(false)
    const seatDialogVisible = ref(false)
    const activeTab = ref('basic')
    const selectedClasses = ref([])
    const selectedStudents = ref([])
    const mainInvigilator = ref(null)
    const assistantInvigilators = ref([])
    const currentExam = ref(null)
    
    // 表单
    const examFormRef = ref(null)
    const examForm = reactive({
      examId: null,
      courseId: null,
      teacherId: null,
      examName: "",
      examTime: "",
      examLocation: "",
      examDuration: "",
      examType: "",
      examFormat: "",
      examStatus: "",
      examDescription: "",
      notes: "",
      capacity: null,
      totalScore: null,
      selectedStudents: []
    })
    
    // 表单校验规则
    const examRules = reactive({
      examName: [{ required: true, message: '请输入考试名称', trigger: 'blur' }],
      courseId: [{ required: true, message: '请选择考试科目', trigger: 'change' }],
      teacherId: [{ required: true, message: '请选择监考教师', trigger: 'change' }],
      examTime: [{ required: true, message: '请选择考试时间', trigger: 'change' }],
      examLocation: [{ required: true, message: '请输入考试地点', trigger: 'blur' }],
      examDuration: [{ required: true, message: '请输入考试时长', trigger: 'blur' }],
      examType: [{ required: true, message: '请选择考试类型', trigger: 'change' }],
      examStatus: [{ required: true, message: '请选择考试状态', trigger: 'change' }]
    })
    
    // 获取所有考试
    const fetchExams = async () => {
      try {
        const response = await request.get('/exam')
        console.log('考试列表数据:', response)
        examList.value = Array.isArray(response.data) ? response.data : 
                        Array.isArray(response) ? response : []
        console.log('处理后的考试列表:', examList.value)
      } catch (error) {
        console.error('获取考试列表失败', error)
        ElMessage.error('获取考试列表失败')
        examList.value = []
      }
    }
    
    // 获取所有课程
    const fetchCourses = async () => {
      try {
        const response = await request.get('/course/selectAll')
        console.log('课程列表数据:', response)
        courseList.value = Array.isArray(response.data) ? response.data : 
                          Array.isArray(response) ? response : []
      } catch (error) {
        console.error('获取课程列表失败', error)
        ElMessage.error('获取课程列表失败')
      }
    }
    
    // 获取所有教师
    const fetchTeachers = async () => {
      try {
        const response = await request.get('/teacher/selectAll')
        console.log('教师列表数据:', response)
        teacherList.value = Array.isArray(response.data) ? response.data : 
                           Array.isArray(response) ? response : []
      } catch (error) {
        console.error('获取教师列表失败', error)
        ElMessage.error('获取教师列表失败')
      }
    }
    
    // 获取所有学生
    const fetchStudents = async () => {
      try {
        const response = await request.get('/student/selectAll')
        console.log('学生列表数据:', response)
        studentList.value = Array.isArray(response.data) ? response.data : 
                           Array.isArray(response) ? response : []
      } catch (error) {
        console.error('获取学生列表失败', error)
        ElMessage.error('获取学生列表失败')
      }
    }
    
    // 获取考试的考生列表
    const fetchExamStudents = async (examId) => {
      try {
        const response = await request.get(`/exam/${examId}/students`)
        console.log('考生列表数据:', response)
        examStudentList.value = Array.isArray(response.data) ? response.data : 
                              Array.isArray(response) ? response : []
      } catch (error) {
        console.error('获取考生列表失败', error)
        ElMessage.error('获取考生列表失败')
      }
    }
    
    // 搜索考试
    const searchExams = async () => {
      if (!searchText.value) {
        fetchExams()
        return
      }
      
      try {
        const response = await request.get(`/exam/search?keyword=${searchText.value}`)
        examList.value = response.data
      } catch (error) {
        console.error('搜索考试失败', error)
        ElMessage.error('搜索考试失败')
      }
    }
    
    // 班级变化处理
    const handleClassChange = () => {
      if (selectedClasses.value.length === 0) {
        studentList.value = []
        return
      }
      
      // 按照年级筛选学生
      const filteredStudents = []
      for (const student of studentList.value) {
        if (selectedClasses.value.includes(student.grade)) {
          filteredStudents.push(student)
        }
      }
      
      studentList.value = filteredStudents
    }
    
    // 学生选择变化处理
    const handleStudentSelection = (selection) => {
      selectedStudents.value = selection
    }
    
    // 查看考生
    const handleViewStudents = (row) => {
      currentExam.value = row
      fetchExamStudents(row.id)
      studentDialogVisible.value = true
    }
    
    // 处理编辑
    const handleEdit = (row) => {
      currentExam.value = row
      examForm.examId = row.id
      examForm.examName = row.name
      examForm.courseId = row.courseId
      examForm.examType = row.examType || ''
      examForm.examFormat = row.examFormat || ''
      examForm.examTime = [row.startTime, row.endTime]
      examForm.examLocation = row.location || ''
      examForm.capacity = row.capacity || null
      examForm.totalScore = row.totalScore || null
      examForm.notes = row.notes || ''
      
      // 获取考生和监考教师信息
      fetchExamStudents(row.id)
      
      dialogVisible.value = true
    }
    
    // 处理新增考试
    const handleAdd = () => {
      console.log('点击了安排新考试按钮') // 添加日志
      resetForm()
      dialogVisible.value = true
      activeTab.value = 'basic'  // 确保显示基本信息标签页
      console.log('对话框状态:', dialogVisible.value) // 添加日志
    }
    
    // 处理删除
    const handleDelete = (row) => {
      ElMessageBox.confirm('确定要删除这个考试吗？此操作不可逆', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await request.delete(`/exam/${row.id}`)
          ElMessage.success('删除成功')
          fetchExams()
        } catch (error) {
          console.error('删除考试失败', error)
          ElMessage.error('删除考试失败')
        }
      }).catch(() => {})
    }
    
    // 排座
    const handleArrangeSeat = (row) => {
      currentExam.value = row
      fetchExamStudents(row.id)
      seatDialogVisible.value = true
    }
    
    // 随机排座
    const randomSeat = async () => {
      if (currentExam.value) {
        try {
          await request.post(`/exam/${currentExam.value.id}/random-seat`)
          ElMessage.success('随机排座成功')
          fetchExamStudents(currentExam.value.id)
        } catch (error) {
          console.error('随机排座失败', error)
          ElMessage.error('随机排座失败')
        }
      }
    }
    
    // 按学号排座
    const sortByStudentId = () => {
      const students = [...examStudentList.value]
      students.sort((a, b) => a.studentUsername.localeCompare(b.studentUsername))
      
      // 分配座位号
      students.forEach((student, index) => {
        student.seatNo = index + 1
      })
      
      examStudentList.value = students
      ElMessage.success('按学号排座完成')
    }
    
    // 重置座位
    const resetSeat = () => {
      examStudentList.value.forEach(student => {
        student.seatNo = null
      })
      ElMessage.info('已重置座位')
    }
    
    // 保存座位
    const saveSeat = async () => {
      if (currentExam.value) {
        const seatMap = {}
        examStudentList.value.forEach(student => {
          if (student.seatNo) {
            seatMap[student.id] = student.seatNo
          }
        })
        
        try {
          await request.post(`/exam/${currentExam.value.id}/arrange-seat`, seatMap)
          ElMessage.success('保存座位成功')
          seatDialogVisible.value = false
        } catch (error) {
          console.error('保存座位失败', error)
          ElMessage.error('保存座位失败')
        }
      }
    }
    
    // 重置表单
    const resetForm = () => {
      examForm.examId = null
      examForm.courseId = null
      examForm.teacherId = null
      examForm.examName = ""
      examForm.examTime = ""
      examForm.examLocation = ""
      examForm.examDuration = ""
      examForm.examType = ""
      examForm.examFormat = ""
      examForm.examStatus = ""
      examForm.examDescription = ""
      examForm.notes = ""
      examForm.capacity = null
      examForm.totalScore = null
      examForm.selectedStudents = []
      selectedStudents.value = []
      
      if (examFormRef.value) {
        examFormRef.value.resetFields()
      }
    }
    
    // 取消编辑
    const cancelEdit = () => {
      resetForm()
    }
    
    // 保存考试
    const saveExam = async () => {
      if (!examFormRef.value) return
      
      try {
        await examFormRef.value.validate()
        
        // 准备提交的数据
        const submitData = {
          examId: examForm.examId,
          courseId: examForm.courseId,
          teacherId: mainInvigilator.value,  // 使用主监考教师的ID
          examName: examForm.examName,
          examLocation: examForm.examLocation,
          examDuration: examForm.examDuration,
          examType: examForm.examType,
          examFormat: examForm.examFormat,
          examStatus: examForm.examStatus,
          examDescription: examForm.examDescription,
          notes: examForm.notes,
          capacity: examForm.capacity,
          totalScore: examForm.totalScore,
          selectedStudents: selectedStudents.value.map(student => student.id || student)
        }

        // 确保所有数值字段为数字类型
        if (submitData.examId) submitData.examId = parseInt(submitData.examId)
        if (submitData.courseId) submitData.courseId = parseInt(submitData.courseId)
        if (submitData.teacherId) submitData.teacherId = parseInt(submitData.teacherId)
        if (submitData.capacity) submitData.capacity = parseInt(submitData.capacity)
        if (submitData.totalScore) submitData.totalScore = parseInt(submitData.totalScore)
        if (submitData.selectedStudents) {
          submitData.selectedStudents = submitData.selectedStudents.map(id => parseInt(id))
        }
        
        // 处理考试时间
        if (Array.isArray(examForm.examTime) && examForm.examTime.length === 2) {
          // 将日期时间格式化为数据库兼容的格式 (YYYY-MM-DD HH:mm:ss)
          const formatDateTime = (date) => {
            const d = new Date(date)
            const year = d.getFullYear()
            const month = String(d.getMonth() + 1).padStart(2, '0')
            const day = String(d.getDate()).padStart(2, '0')
            const hours = String(d.getHours()).padStart(2, '0')
            const minutes = String(d.getMinutes()).padStart(2, '0')
            const seconds = String(d.getSeconds()).padStart(2, '0')
            return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
          }
          
          submitData.startTime = formatDateTime(examForm.examTime[0])
          submitData.endTime = formatDateTime(examForm.examTime[1])
        }
        
        console.log('准备提交的数据:', submitData)
        
        if (submitData.examId) {
          await request.put(`/exam/${submitData.examId}`, submitData)
          ElMessage.success('考试信息更新成功')
        } else {
          await request.post('/exam', submitData)
          ElMessage.success('考试信息添加成功')
        }
        
        resetForm()
        dialogVisible.value = false
        fetchExams()
      } catch (error) {
        console.error('保存考试失败', error)
        ElMessage.error('保存考试失败')
      }
    }
    
    // 格式化日期时间
    const formatDateTime = (dateTimeStr) => {
      if (!dateTimeStr) return ''
      const date = new Date(dateTimeStr)
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      const hours = String(date.getHours()).padStart(2, '0')
      const minutes = String(date.getMinutes()).padStart(2, '0')
      
      return `${year}-${month}-${day} ${hours}:${minutes}`
    }
    
    onMounted(() => {
      fetchExams()
      fetchCourses()
      fetchTeachers()
      fetchStudents()
    })
    
    return {
      examList,
      courseList,
      teacherList,
      studentList,
      examStudentList,
      searchText,
      dialogVisible,
      studentDialogVisible,
      seatDialogVisible,
      activeTab,
      selectedClasses,
      selectedStudents,
      mainInvigilator,
      assistantInvigilators,
      currentExam,
      examFormRef,
      examForm,
      examRules,
      formatDateTime,
      searchExams,
      handleClassChange,
      handleStudentSelection,
      handleViewStudents,
      handleEdit,
      handleDelete,
      handleArrangeSeat,
      randomSeat,
      sortByStudentId,
      resetSeat,
      saveSeat,
      handleAdd,
      saveExam
    }
  }
}
</script>

<style scoped>
.arrange-exam-container {
  padding: 20px;
}
</style> 