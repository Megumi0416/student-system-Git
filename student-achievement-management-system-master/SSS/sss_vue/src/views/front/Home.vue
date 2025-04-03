<template>
  <div class="home-container">

    <!-- 主内容区 -->
    <div class="card main-content">
      <!-- 欢迎信息 -->
      <el-row :gutter="20" class="welcome-section">
        <el-col :xs="24" :sm="16">
          <h2>欢迎回来，{{ user.name }}！</h2>
          <p class="sub-info">今天是 {{ currentDate }}，你有 {{ data.unread }} 条未读消息</p>
        </el-col>
        <!-- 统计展示部分 -->
        <el-col :xs="24" :sm="8" class="quick-stats">
          <el-tooltip
              effect="light"
              placement="bottom"
              :show-after="300"
              popper-class="stats-tooltip"
          >
            <template #content>
              <div style="max-width: 250px">
                总选课数：{{ data.courseCount }} 门<br>
                （{{ data.scoredCount }} 门已出成绩，{{ data.courseCount - data.scoredCount }} 门未出成绩）
              </div>
            </template>
            <el-statistic title="已选课程" :value="data.courseCount" />
          </el-tooltip>

          <el-tooltip
              effect="light"
              placement="bottom"
              :show-after="300"
              popper-class="stats-tooltip"
          >
            <template #content>
              <div style="max-width: 250px">
                基于 {{ data.scoredCount }} 门已出成绩课程计算<br>
                （{{ data.averageScoreDesc }}）
              </div>
            </template>
            <el-statistic
                title="平均成绩"
                :value="typeof data.averageScore === 'number' ? data.averageScore : '--'"
                :precision="1"
            />
          </el-tooltip>
        </el-col>
      </el-row>

      <!-- 核心数据看板 -->
      <el-row :gutter="20" class="dashboard">
        <!-- 公告时间线 -->
        <el-col :xs="24" :md="12" class="mb-20">
          <el-card class="info-card">
            <template #header>
              <div class="card-header">
                <el-icon><Bell /></el-icon>
                <span>最新公告</span>
              </div>
            </template>
            <el-timeline>
              <el-timeline-item
                  v-for="(notice, index) in noticeData"
                  :key="index"
                  :timestamp="notice.time"
                  :type="index === 0 ? 'primary' : 'info'"
              >
                {{ notice.content }}
              </el-timeline-item>
            </el-timeline>
          </el-card>
        </el-col>

        <!-- 成绩概览 -->
        <el-col :xs="24" :md="12" class="mb-20">
          <el-card class="info-card">
            <template #header>
              <div class="card-header">
                <el-icon><DataAnalysis /></el-icon>
                <span>最新学期成绩 ---（{{ currentTerm }}）</span>
              </div>
            </template>
            <el-table :data="currentTermGrades" stripe style="width: 100%"> <!-- 新增currentTermGrades -->
              <!-- 原有列保持不变 -->
              <el-table-column prop="coursename" label="课程" width="120" />
              <el-table-column prop="score" label="成绩" width="100">
                <template #default="{row}">
                  <el-tag :type="getScoreType(row.score)">
                    {{ row.score || '待录入' }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="趋势" width="80">
                <template #default="{row}">
                  <el-icon v-if="row.trend === 'up'" color="#67C23A">
                    <Top />
                  </el-icon>
                  <el-icon v-else-if="row.trend === 'down'" color="#F56C6C">
                    <Bottom />
                  </el-icon>
                  <span v-else>-</span>
                </template>
              </el-table-column>
              <el-table-column prop="teachername" label="授课教师" />
            </el-table>
          </el-card>
        </el-col>
      </el-row>

      <!-- 快捷入口 -->
      <el-row :gutter="20" class="quick-actions">
        <el-col v-for="action in quickActions" :key="action.path" :span="6">
          <el-card shadow="hover" class="action-card" @click="router.push(action.path)">
            <el-icon :size="30" :color="action.color">
              <component :is="action.icon" />
            </el-icon>
            <div class="action-title">{{ action.title }}</div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import {
  Bell, DataAnalysis, Top, Bottom,
  Notebook, ChatRound, User, List
} from '@element-plus/icons-vue'
import request from '@/utils/request'

const router = useRouter()
const user = JSON.parse(localStorage.getItem('xm-user') || '{}')

// 响应式数据
const noticeData = ref([])
const currentNoticeIndex = ref(0)
const currentDate = ref(new Date().toLocaleDateString())

const data = reactive({
  unread: 0,
  courseCount: 0,
  averageScore: 0,
  recentGrades: [],
  scoredCount: 0,      // 已出成绩课程数
  averageScoreDesc: '' // 平均分描述
})

// 新增计算属性获取最新学期
const currentTerm = computed(() => {
  if (data.recentGrades.length === 0) return '暂无成绩信息'
  const terms = data.recentGrades.map(item => item.term)
  return terms[0] || '--'
})

// 轮播公告逻辑
let noticeInterval = null
const currentNotice = computed(() => {
  return noticeData.value[currentNoticeIndex.value]?.content || '暂无公告'
})

// 计算属性处理最新学期成绩
const currentTermGrades = computed(() => {
  // 从原始数据中筛选最新学期成绩
  const latestTerm = data.recentGrades.reduce((max, item) =>
      item.term > max ? item.term : max, ''
  )
  return data.recentGrades.filter(item => item.term === latestTerm)
})

// 快捷操作配置
const quickActions = [
  { icon: Notebook, title: '选课中心', path: '/front/courselist', color: '#409EFF' },
  { icon: List, title: '已选课程', path: '/front/studentcourse', color: '#67C23A' },
  { icon: ChatRound, title: '在线提问', path: '/front/message', color: '#E6A23C' },
  { icon: User, title: '个人中心', path: '/front/person', color: '#909399' }
]

// 获取成绩样式类型
const getScoreType = (score) => {
  if (!score) return 'info'
  if (score >= 90) return 'success'
  if (score >= 60) return 'warning'
  return 'danger'
}

// 加载数据
const loadData = async () => {
  try {
    // 获取公告
    const noticeRes = await request.get('/notice/selectAll')
    noticeData.value = noticeRes.data || []

    const statsRes = await request.get(`/studentCourse/statistics/${user.id}`)
    data.courseCount = statsRes.data.courseCount || 0
    // data.averageScore = statsRes.data.averageScore ?
    //     statsRes.data.averageScore.toFixed(1) : '--'
    data.scoredCount = statsRes.data.scoredCount || 0

    // 处理平均分显示
    if (statsRes.data.averageScore) {
      data.averageScore = Number(statsRes.data.averageScore.toFixed(1))
      data.averageScoreDesc = `计算公式：总分/${data.scoredCount}门课程`
    } else {
      data.averageScore = '--'
      data.averageScoreDesc = '暂无有效成绩数据'
    }

    // 启动公告轮播
    if (noticeData.value.length > 1) {
      noticeInterval = setInterval(() => {
        currentNoticeIndex.value = (currentNoticeIndex.value + 1) % noticeData.value.length
      }, 3000)
    }

// 获取最新学期成绩
    const scoresRes = await request.get('/testscores/latestScores', {
      params: { studentId: user.id }
    })
    data.recentGrades = scoresRes.data || []

  } catch (error) {
    console.error('数据加载失败:', error)
  }
}

onMounted(() => {
  loadData()
  return () => clearInterval(noticeInterval)
})
</script>

<style scoped lang="scss">
.home-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 60px);
}

.front-notice {
  background: #e6f7ff;
  padding: 12px 20px;
  margin-bottom: 20px;
  border-radius: 4px;
  display: flex;
  align-items: center;
  .el-icon { margin-right: 8px; color: #409EFF; }
}

.main-content {
  padding: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.1);
}

.welcome-section {
  margin-bottom: 30px;
  h2 {
    color: #303133;
    margin-bottom: 8px;
    font-size: 24px;
  }
  .sub-info {
    color: #909399;
    font-size: 14px;
  }
}

.quick-stats {
  display: flex;
  gap: 20px;
  justify-content: flex-end;
  :deep(.el-statistic) {
    text-align: center;
    .el-statistic__head {
      font-size: 14px;
      color: #909399;
      line-height: 1.2;
    }
    .el-statistic__content {
      font-size: 24px;
      font-weight: bold;
    }
  }
}

.info-card {
  margin-bottom: 20px;
  :deep(.el-card__header) {
    background: #fafbfc;
    border-bottom: 1px solid #ebeef5;
    .card-header {
      display: flex;
      align-items: center;
      .el-icon { margin-right: 8px; }
      span { font-weight: 500; }
    }
  }
}



.quick-actions {
  margin-top: 30px;
  .action-card {
    cursor: pointer;
    transition: transform 0.3s;
    text-align: center;
    &:hover {
      transform: translateY(-5px);
      box-shadow: 0 4px 12px rgba(0,0,0,0.15);
    }
    .action-title {
      margin-top: 10px;
      color: #606266;
      font-size: 14px;
    }
  }
}

.mb-20 { margin-bottom: 20px; }

@media (max-width: 768px ) {
  .quick-stats {
    justify-content: flex-start;
    margin-top: 15px;
  }

  .quick-actions .el-col {
    margin-bottom: 15px;
  }
}

</style>