<template>
  <div class="teacher-exams-container">
    <!-- 搜索区域 -->
    <div class="search-container">
      <el-input
        v-model="searchText"
        placeholder="请输入考试名称或课程名称"
        class="search-input"
        clearable
        @clear="handleSearch"
        @keyup.enter="handleSearch"
      >
        <template #prefix>
          <el-icon><Search /></el-icon>
        </template>
      </el-input>
      <el-button type="primary" @click="handleSearch">搜索</el-button>
    </div>

    <!-- 考试列表表格 -->
    <el-table
      :data="examList"
      border
      stripe
      style="width: 100%"
      v-loading="loading"
    >
      <el-table-column prop="id" label="ID" width="80" fixed="left"></el-table-column>
      <el-table-column prop="name" label="考试名称" width="150" show-overflow-tooltip></el-table-column>
      <el-table-column prop="courseName" label="考试科目" width="120" show-overflow-tooltip></el-table-column>
      <el-table-column prop="examType" label="考试类型" width="100">
        <template #default="scope">
          <el-tag v-if="scope.row.examType === 'midterm'" type="warning">期中</el-tag>
          <el-tag v-else-if="scope.row.examType === 'final'" type="danger">期末</el-tag>
          <el-tag v-else-if="scope.row.examType === 'makeup'" type="info">补考</el-tag>
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
      <el-table-column prop="location" label="考试地点" width="120" show-overflow-tooltip></el-table-column>
      <el-table-column prop="capacity" label="考试容量" width="100"></el-table-column>
      <el-table-column prop="totalScore" label="总分" width="80"></el-table-column>
      <el-table-column prop="notes" label="注意事项" show-overflow-tooltip></el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Search } from '@element-plus/icons-vue'
import request from '@/utils/request'

// 响应式数据
const examList = ref([])
const searchText = ref('')
const loading = ref(false)

// 获取教师的考试列表
const fetchTeacherExams = async () => {
  loading.value = true
  try {
    const user = JSON.parse(localStorage.getItem('xm-user') || '{}')
    const response = await request.get(`/exam/teacher/${user.id}`)
    examList.value = response || []
  } catch (error) {
    console.error('获取考试列表失败', error)
    ElMessage.error('获取考试列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索考试
const handleSearch = () => {
  fetchTeacherExams()
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

// 页面加载时获取考试列表
onMounted(() => {
  fetchTeacherExams()
})
</script>

<style scoped>
.teacher-exams-container {
  padding: 20px;
}

.search-container {
  display: flex;
  margin-bottom: 20px;
  gap: 10px;
}

.search-input {
  width: 300px;
}

:deep(.el-table) {
  margin-top: 20px;
}

:deep(.el-tag) {
  margin-right: 5px;
}
</style>