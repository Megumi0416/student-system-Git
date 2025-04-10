<template>
  <div class="card" style="width: 80%; margin: 5px auto">
    <!-- 搜索和筛选区域 -->
    <div class="card" style="margin-bottom: 10px">
      <el-input 
        v-model="data.examName" 
        style="width: 240px; margin-right: 10px" 
        placeholder="请输入考试名称查询"
        :prefix-icon="Search"
      />
      <el-select 
        v-model="data.examType" 
        clearable 
        placeholder="考试类型" 
        style="width: 140px; margin-right: 10px"
      >
        <el-option label="期中考试" value="midterm" />
        <el-option label="期末考试" value="final" />
        <el-option label="补考" value="makeup" />
        <el-option label="其他" value="other" />
      </el-select>
      <el-select 
        v-model="data.status" 
        clearable 
        placeholder="考试状态" 
        style="width: 140px; margin-right: 10px"
      >
        <el-option label="未开始" value="NOT_STARTED" />
        <el-option label="即将开始" value="UPCOMING" />
        <el-option label="已结束" value="FINISHED" />
      </el-select>
      <el-date-picker
        v-model="data.dateRange"
        type="daterange"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        style="width: 240px; margin-right: 10px"
      />
      <el-button type="primary" style="margin-left: 10px" @click="loadExams">查询</el-button>
      <el-button type="info" @click="reset">重置</el-button>
    </div>

    <!-- 考试卡片列表 -->
    <div class="exam-cards" style="margin-bottom: 10px">
      <!-- 加载状态 -->
      <div v-if="data.loading" class="loading-container">
        <el-skeleton :rows="3" animated />
        <el-skeleton :rows="3" animated style="margin-top: 20px" />
      </div>
      
      <!-- 空状态 -->
      <el-empty 
        v-else-if="data.examList.length === 0" 
        description="暂无考试安排" 
      />
      
      <!-- 考试列表 -->
      <el-row v-else :gutter="20">
        <el-col 
          v-for="exam in data.examList" 
          :key="exam.id" 
          :xs="24" 
          :sm="12" 
          :md="8" 
          :lg="8" 
          :xl="6"
          style="margin-bottom: 20px"
        >
          <el-card 
            class="exam-card"
            :class="{ 
              'upcoming-exam': getExamStatus(exam) === 'UPCOMING',
              'not-started-exam': getExamStatus(exam) === 'NOT_STARTED',
              'finished-exam': getExamStatus(exam) === 'FINISHED'
            }"
            @click="showExamDetail(exam)"
            :body-style="{ padding: '15px' }"
            shadow="hover"
          >
            <div class="exam-card-header">
              <div class="exam-title">{{ exam.name }}</div>
              <div class="exam-status">
                <el-tag 
                  :type="getStatusTagType(exam)" 
                  effect="dark"
                  size="small"
                >
                  {{ getStatusText(exam) }}
                </el-tag>
              </div>
            </div>
            
            <div class="exam-info">
              <div class="info-item">
                <span class="label">课程:</span>
                <span class="value">{{ exam.courseName }}</span>
              </div>
              <div class="info-item">
                <span class="label">类型:</span>
                <span class="value">{{ getExamTypeText(exam.examType) }}</span>
              </div>
              <div class="info-item">
                <span class="label">形式:</span>
                <span class="value">{{ getExamFormatText(exam.examFormat) }}</span>
              </div>
              <div class="info-item">
                <span class="label">时间:</span>
                <span class="value">{{ formatDateTime(exam.startTime) }}</span>
              </div>
              <div class="info-item">
                <span class="label">地点:</span>
                <span class="value">{{ exam.location }}</span>
              </div>
              <div class="info-item">
                <span class="label">座位:</span>
                <span class="value highlight">{{ exam.seatNo || '待分配' }}</span>
              </div>
            </div>
            
            <div class="exam-footer">
              <div v-if="isUpcoming(exam.startTime)" class="countdown">
                <el-icon><Timer /></el-icon>
                <span>距离考试: {{ getCountdown(exam.startTime) }}</span>
              </div>
              <el-button type="primary" size="small" plain @click.stop="showExamDetail(exam)">查看详情</el-button>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
    
    <!-- 分页 -->
    <div class="card">
      <el-pagination
        v-model:current-page="data.pageNum"
        v-model:page-size="data.pageSize"
        @current-change="handleCurrentChange"
        background
        layout="prev, pager, next"
        :total="data.total"
      />
    </div>
    
    <!-- 考试详情弹窗 -->
    <el-dialog
      v-model="data.detailDialogVisible"
      :title="data.currentExam?.name"
      width="600px"
    >
      <!-- 加载状态 -->
      <div v-if="data.detailLoading" class="detail-loading">
        <el-skeleton :rows="6" animated />
      </div>
      
      <div class="exam-detail" v-else-if="data.currentExam">
        <div class="detail-header">
          <div class="course-info">{{ data.currentExam.courseName }}</div>
          <div class="exam-badge">
            <el-tag :type="getExamTypeTagType(data.currentExam.examType)">
              {{ getExamTypeText(data.currentExam.examType) }}
            </el-tag>
            <el-tag :type="getExamFormatTagType(data.currentExam.examFormat)" style="margin-left: 5px">
              {{ getExamFormatText(data.currentExam.examFormat) }}
            </el-tag>
          </div>
        </div>
        
        <el-divider />
        
        <div class="detail-content">
          <div class="detail-item">
            <div class="detail-label">考试时间</div>
            <div class="detail-value">
              {{ formatDateTime(data.currentExam.startTime) }} 至 {{ formatDateTime(data.currentExam.endTime) }}
              <div class="duration">（时长: {{ getExamDuration(data.currentExam) }}分钟）</div>
            </div>
          </div>
          
          <div class="detail-item">
            <div class="detail-label">考试地点</div>
            <div class="detail-value">{{ data.currentExam.location }}</div>
          </div>
          
          <div class="detail-item">
            <div class="detail-label">座位号</div>
            <div class="detail-value highlight">{{ data.currentExam.seatNo || '待分配' }}</div>
          </div>
          
          <div class="detail-item">
            <div class="detail-label">总分值</div>
            <div class="detail-value">{{ data.currentExam.totalScore }}分</div>
          </div>
          
          <div class="detail-item">
            <div class="detail-label">监考教师</div>
            <div class="detail-value">{{ data.currentExam.invigilators || '待安排' }}</div>
          </div>
          
          <div class="detail-item">
            <div class="detail-label">考试要求</div>
            <div class="detail-value requirements">
              <p>{{ data.currentExam.notes || '暂无特殊要求' }}</p>
            </div>
          </div>
          
          <div class="detail-item">
            <div class="detail-label">携带物品</div>
            <div class="detail-value">
              <ul class="items-list">
                <li>身份证、学生证</li>
                <li>黑色签字笔、2B铅笔</li>
                <li>橡皮、尺子</li>
                <li v-if="data.currentExam.examFormat === 'open'">课本、笔记</li>
                <li v-if="data.currentExam.examFormat === 'computer'">无需携带任何电子设备</li>
              </ul>
            </div>
          </div>
        </div>
        
        <div class="detail-footer" v-if="isUpcoming(data.currentExam.startTime)">
          <div class="reminder-text">
            <el-icon><AlarmClock /></el-icon>
            <span>距离考试开始还有: {{ getCountdown(data.currentExam.startTime) }}</span>
          </div>
          <el-button type="primary" @click="setReminder(data.currentExam)">设置提醒</el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, onMounted, onBeforeUnmount, computed } from "vue"
import { Search, Timer, AlarmClock } from '@element-plus/icons-vue'
import request from "@/utils/request"
import { ElMessage } from "element-plus"

// 响应式数据
const data = reactive({
  examName: '',
  examType: '',
  status: '',
  dateRange: null,
  
  examList: [],
  currentExam: null,
  total: 0,
  pageNum: 1,
  pageSize: 9,
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  detailDialogVisible: false,
  detailLoading: false,
  
  // 定时刷新考试状态用
  timer: null
})

// 加载考试列表
const loadExams = () => {
  let params = {
    pageNum: data.pageNum,
    pageSize: data.pageSize,
    name: data.examName,
    examType: data.examType,
    status: data.status
  }
  
  // 日期范围
  if (data.dateRange && data.dateRange.length === 2) {
    params.startDate = data.dateRange[0]
    params.endDate = data.dateRange[1]
  }
  
  // 学生只查询自己的考试
  params.studentId = data.user.id
  
  console.log('请求参数:', params)
  
  // 显示加载状态
  data.loading = true
  
  request.get('/exam/student', {
    params: params
  }).then(res => {
    // 隐藏加载状态
    data.loading = false
    console.log('请求成功:', res)
    
    if (res.code === '200') {
      let list = res.data?.list || []
      
      // 处理时间格式
      list.forEach(item => {
        // 确保时间戳能够被前端正确解析
        if (item.startTime && typeof item.startTime !== 'string') {
          item.startTime = new Date(item.startTime.time || item.startTime).toISOString()
        }
        if (item.endTime && typeof item.endTime !== 'string') {
          item.endTime = new Date(item.endTime.time || item.endTime).toISOString()
        }
      })
      
      // 对考试列表进行排序，即将开始的排在前面
      list.sort((a, b) => {
        // 首先按照状态排序：即将开始 > 未开始 > 已结束
        const statusOrder = {
          'UPCOMING': 0,
          'NOT_STARTED': 1,
          'FINISHED': 2
        }
        
        const statusA = getExamStatus(a)
        const statusB = getExamStatus(b)
        
        if (statusOrder[statusA] !== statusOrder[statusB]) {
          return statusOrder[statusA] - statusOrder[statusB]
        }
        
        // 状态相同时，按开始时间排序
        return new Date(a.startTime) - new Date(b.startTime)
      })
      
      data.examList = list
      data.total = res.data?.total || 0
    } else {
      ElMessage.error(res.msg || '获取考试列表失败')
      data.examList = []
      data.total = 0
    }
  }).catch(error => {
    // 隐藏加载状态
    data.loading = false
    console.error('请求失败:', error)
    ElMessage.error('服务器连接失败，请稍后再试')
    
    // 清空现有数据
    data.examList = []
    data.total = 0
  })
}

// 重置查询条件
const reset = () => {
  data.examName = ''
  data.examType = ''
  data.status = ''
  data.dateRange = null
  loadExams()
}

// 翻页
const handleCurrentChange = () => {
  loadExams()
}

// 显示考试详情
const showExamDetail = (exam) => {
  // 首先显示基本信息，然后再异步加载详细信息
  data.currentExam = { ...exam }
  data.detailDialogVisible = true
  data.detailLoading = true
  
  // 使用接口获取更详细的考试信息，包括监考教师等
  request.get(`/exam/student/${exam.id}/detail`, {
    params: {
      studentId: data.user.id
    }
  }).then(res => {
    data.detailLoading = false
    if (res.code === '200') {
      // 处理时间格式
      const detailData = res.data
      if (detailData.startTime && typeof detailData.startTime !== 'string') {
        detailData.startTime = new Date(detailData.startTime.time || detailData.startTime).toISOString()
      }
      if (detailData.endTime && typeof detailData.endTime !== 'string') {
        detailData.endTime = new Date(detailData.endTime.time || detailData.endTime).toISOString()
      }
      
      data.currentExam = detailData
    } else {
      ElMessage.warning('获取详情失败，显示基本信息')
    }
  }).catch(() => {
    data.detailLoading = false
    ElMessage.warning('获取详情失败，显示基本信息')
  })
}

// 设置提醒
const setReminder = (exam) => {
  ElMessage.success(`已设置考试 "${exam.name}" 的提醒`)
  // 这里实际项目中可以调用接口实现提醒功能
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

// 格式化时间（不含日期）
const formatTime = (dateTimeStr) => {
  if (!dateTimeStr) return ''
  const date = new Date(dateTimeStr)
  const hours = String(date.getHours()).padStart(2, '0')
  const minutes = String(date.getMinutes()).padStart(2, '0')
  
  return `${hours}:${minutes}`
}

// 计算考试时长（分钟）
const getExamDuration = (exam) => {
  if (!exam || !exam.startTime || !exam.endTime) return 0
  const start = new Date(exam.startTime)
  const end = new Date(exam.endTime)
  return Math.floor((end - start) / (1000 * 60))
}

// 获取考试状态
const getExamStatus = (exam) => {
  if (!exam || !exam.startTime || !exam.endTime) return 'NOT_STARTED'
  
  const now = new Date()
  const startTime = new Date(exam.startTime)
  const endTime = new Date(exam.endTime)
  
  // 已结束
  if (now > endTime) {
    return 'FINISHED'
  }
  
  // 即将开始（3天内）
  const threeDaysInMs = 3 * 24 * 60 * 60 * 1000
  if (now < startTime && startTime - now <= threeDaysInMs) {
    return 'UPCOMING'
  }
  
  // 未开始
  return 'NOT_STARTED'
}

// 获取考试状态文本
const getStatusText = (exam) => {
  const status = getExamStatus(exam)
  switch (status) {
    case 'FINISHED': return '已结束'
    case 'UPCOMING': return '即将开始'
    case 'NOT_STARTED': return '未开始'
    default: return '未知状态'
  }
}

// 获取考试状态标签类型
const getStatusTagType = (exam) => {
  const status = getExamStatus(exam)
  switch (status) {
    case 'FINISHED': return 'success'
    case 'UPCOMING': return 'warning'
    case 'NOT_STARTED': return 'info'
    default: return 'info'
  }
}

// 获取考试类型文本
const getExamTypeText = (type) => {
  switch (type) {
    case 'midterm': return '期中'
    case 'final': return '期末'
    case 'makeup': return '补考'
    case 'other': return '其他'
    default: return '未知'
  }
}

// 获取考试类型标签类型
const getExamTypeTagType = (type) => {
  switch (type) {
    case 'midterm': return 'warning'
    case 'final': return 'danger'
    case 'makeup': return 'info'
    default: return 'info'
  }
}

// 获取考试形式文本
const getExamFormatText = (format) => {
  switch (format) {
    case 'closed': return '闭卷'
    case 'open': return '开卷'
    case 'computer': return '机考'
    default: return '未知'
  }
}

// 获取考试形式标签类型
const getExamFormatTagType = (format) => {
  switch (format) {
    case 'closed': return 'danger'
    case 'open': return 'success'
    case 'computer': return 'primary'
    default: return 'info'
  }
}

// 检查考试是否即将开始（3天内）
const isUpcoming = (startTimeStr) => {
  if (!startTimeStr) return false
  
  const now = new Date()
  const startTime = new Date(startTimeStr)
  
  // 如果考试已经结束，则不是即将开始
  if (now > startTime) return false
  
  // 计算距离考试开始的毫秒数
  const diffMs = startTime - now
  
  // 检查是否在3天内
  return diffMs <= 3 * 24 * 60 * 60 * 1000
}

// 获取倒计时文本
const getCountdown = (startTimeStr) => {
  if (!startTimeStr) return ''
  
  const now = new Date()
  const startTime = new Date(startTimeStr)
  
  // 如果考试已经开始或结束，不显示倒计时
  if (now >= startTime) return '已开始'
  
  // 计算时间差
  let diffMs = startTime - now
  
  // 转换为天、小时、分钟
  const days = Math.floor(diffMs / (1000 * 60 * 60 * 24))
  diffMs -= days * (1000 * 60 * 60 * 24)
  
  const hours = Math.floor(diffMs / (1000 * 60 * 60))
  diffMs -= hours * (1000 * 60 * 60)
  
  const minutes = Math.floor(diffMs / (1000 * 60))
  
  // 生成倒计时文本
  if (days > 0) {
    return `${days}天${hours}小时`
  } else if (hours > 0) {
    return `${hours}小时${minutes}分钟`
  } else {
    return `${minutes}分钟`
  }
}

// 设置定时更新定时器
onMounted(() => {
  loadExams()
  
  // 定时刷新考试状态（每分钟一次）
  data.timer = setInterval(() => {
    // 仅在列表不为空时更新UI
    if (data.examList.length > 0) {
      // 强制刷新UI以更新倒计时
      data.examList = [...data.examList]
    }
  }, 60 * 1000)
})

// 清除定时器
onBeforeUnmount(() => {
  if (data.timer) {
    clearInterval(data.timer)
  }
})
</script>

<style scoped>
.card {
  margin-bottom: 10px;
  padding: 15px;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,.1);
}

.exam-cards {
  margin-top: 15px;
}

.exam-card {
  height: 100%;
  transition: all 0.3s;
  border-left: 4px solid #909399;
}

.exam-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 15px rgba(0, 0, 0, 0.1) !important;
}

.upcoming-exam {
  border-left: 4px solid #E6A23C;
}

.not-started-exam {
  border-left: 4px solid #409EFF;
}

.finished-exam {
  border-left: 4px solid #67C23A;
}

.exam-card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.exam-title {
  font-size: 16px;
  font-weight: bold;
  color: #303133;
}

.exam-info {
  margin-bottom: 15px;
}

.info-item {
  margin-bottom: 5px;
  display: flex;
}

.info-item .label {
  color: #909399;
  width: 45px;
}

.info-item .value {
  color: #606266;
  flex: 1;
}

.info-item .highlight {
  color: #409EFF;
  font-weight: bold;
}

.exam-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 10px;
}

.countdown {
  display: flex;
  align-items: center;
  color: #E6A23C;
  font-size: 13px;
}

.countdown .el-icon {
  margin-right: 5px;
}

/* 详情弹窗样式 */
.exam-detail {
  padding: 0 15px;
}

.detail-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.course-info {
  font-size: 16px;
  font-weight: bold;
}

.detail-content {
  margin-top: 20px;
}

.detail-item {
  margin-bottom: 15px;
}

.detail-label {
  font-weight: bold;
  margin-bottom: 5px;
  color: #303133;
}

.detail-value {
  color: #606266;
}

.detail-value.highlight {
  color: #409EFF;
  font-weight: bold;
}

.duration {
  color: #909399;
  font-size: 14px;
  margin-top: 3px;
}

.requirements {
  background-color: #f8f8f8;
  padding: 10px;
  border-radius: 4px;
  font-size: 14px;
}

.items-list {
  padding-left: 20px;
  margin: 5px 0;
}

.detail-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 20px;
  padding: 15px;
  background-color: #f8f8f8;
  border-radius: 4px;
}

.reminder-text {
  display: flex;
  align-items: center;
  color: #E6A23C;
}

.reminder-text .el-icon {
  margin-right: 5px;
}

.loading-container {
  padding: 20px;
  background: #f9f9f9;
  border-radius: 8px;
}

.detail-loading {
  padding: 15px;
}

.exam-status {
  position: absolute;
  top: 10px;
  right: 10px;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
  color: white;
}

.status-upcoming {
  background-color: #E6A23C;
}

.status-not-started {
  background-color: #409EFF;
}

.status-finished {
  background-color: #67C23A;
}
</style> 