<template>
  <div class="dashboard-container">
    <!-- 顶部欢迎卡片 -->
    <div class="card welcome-card">
      <span class="welcome-text">您好! <span style="color: dodgerblue">{{ data.user?.name }}</span>，欢迎使用学生成绩管理系统!</span>
      <el-button v-if="data.user.role === 'admin'" type="primary" size="small" @click="handleAdminShortcut">进入管理后台</el-button>
    </div>

    <!-- 主内容区 -->
    <div class="main-content">
      <!-- 左侧公告栏 -->
      <div class="left-panel">
        <div class="card notice-card">
          <div class="notice-header">
            <h3>系统公告</h3>
            <el-button v-if="data.user.role === 'ADMIN'" type="text" @click="handleNoticeManage">管理公告</el-button>
          </div>
          <el-timeline>
            <el-timeline-item
                v-for="(item, index) in data.noticeData"
                :key="index"
                :timestamp="item.time"
            >
              <div class="notice-content">
                <h4>{{ item.title }}</h4>
                <p>{{ item.content }}</p>
              </div>
            </el-timeline-item>
          </el-timeline>
        </div>
      </div>

      <!-- 右侧新增内容 -->
      <div class="right-panel">
        <!-- 数据统计卡片 -->
        <div class="stats-cards">
          <div class="card stats-card">
            <div class="stats-item">
              <h3>{{ studentCount }}</h3>
              <p>学生总数</p>
            </div>
            <div class="stats-item">
              <h3>{{ courseCount }}</h3>
              <p>课程总数</p>
            </div>
            <div class="stats-item">
              <h3>{{ newRecords }}</h3>
              <p>今日成绩录入</p>
            </div>
          </div>

          <!-- 成绩分布图表 -->
          <div class="card chart-card">
            <h3>成绩分布统计</h3>
            <div ref="chart" style="height: 300px;"></div>
          </div>
        </div>

        <!-- 待办事项 -->
        <div class="card todo-card">
          <h3>待办事项</h3>
          <el-list>
            <el-list-item v-for="(item, index) in todoList" :key="index">
              <template #default>
                <div class="todo-item">
                  <span class="todo-title">{{ item.title }}</span>
                  <el-tag :type="item.type">{{ item.status }}</el-tag>
                </div>
              </template>
            </el-list-item>
          </el-list>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref, reactive } from 'vue'
import * as echarts from 'echarts'
import request from '@/utils/request'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  noticeData: []
})

const router = useRouter()

// 统计数据和图表
const studentCount = ref(0)
const courseCount = ref(0)
const newRecords = ref(0)
const chart = ref(null)
let myChart = null

// 待办事项
const todoList = ref([
  { title: '待审核成绩记录', type: 'warning', status: '进行中' },
  { title: '未完成课程安排', type: 'danger', status: '未完成' }
])

// 加载公告数据
const loadNotice = () => {
  request.get("/notice/selectAll").then(res => {
    if (res.code === '200') {
      data.noticeData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  }).catch(error => {
    ElMessage.error('请求失败，请稍后再试')
  })
}

// 加载统计数据
const loadStatistics = async () => {
  try {
    // 根据角色获取学生总数
    if (data.user.role === 'ADMIN') {
      // 管理员调用selectPage接口获取总数
      const res = await request.get('/student/selectPage', {
        params: {
          pageNum: 1,
          pageSize: 1
        }
      })
      if (res.code === '200') {
        studentCount.value = res.data?.total || 0
      }
    } else if (data.user.role === 'TEACHER') {
      // 教师调用count接口获取自己课程的学生数
      const res = await request.get('/student/count')
      if (res.code === '200') {
        studentCount.value = res.data || 0
      }
    } else {
      // 学生或其他角色显示0
      studentCount.value = 0
    }

    // 获取课程总数
    const courseRes = await request.get('/course/count')
    if (courseRes.code === '200') {
      courseCount.value = courseRes.data || 0
    }

    // 获取今日成绩录入数
    const recordsRes = await request.get('/studentCourse/todayRecords')
    if (recordsRes.code === '200') {
      newRecords.value = recordsRes.data?.newRecords || 0
    }
  } catch (error) {
    ElMessage.error('获取统计数据失败')
    console.error(error)
  }
}

// 初始化图表
const initChart = () => {
  if (!myChart) {
    myChart = echarts.init(chart.value)
  }

  const option = {
    title: {
      text: '成绩分布统计',
      left: 'center'
    },
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    series: [
      {
        name: '成绩分布',
        type: 'pie',
        radius: '55%',
        data: [
          {value: 335, name: '90-100分'},
          {value: 310, name: '80-89分'},
          {value: 234, name: '70-79分'},
          {value: 1548, name: '60-69分'},
          {value: 135, name: '不及格'}
        ],
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  }

  myChart.setOption(option)
}

// 事件处理
const handleAdminShortcut = () => {
  // 管理员快捷操作
}

const handleNoticeManage = () => {
  // const router = useRouter()
  router.push('/manager/notice')
}

// 生命周期钩子
onMounted(() => {
  loadNotice()
  loadStatistics()
  initChart()
  window.addEventListener('resize', handleResize)
})

const handleResize = () => {
  myChart?.resize()
}
</script>

<style scoped>
.dashboard-container {
  padding: 20px;
}

.welcome-card {
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.main-content {
  display: flex;
  gap: 20px;
  margin-top: 20px;
}

.left-panel {
  flex: 1;
}

.right-panel {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.notice-card {
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.notice-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.notice-content h4 {
  margin: 0 0 10px;
  color: #409EFF;
}

.notice-content p {
  margin: 0;
  color: #666;
  line-height: 1.5;
}

.stats-cards {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.stats-card {
  display: flex;
  justify-content: space-around;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.stats-item {
  text-align: center;
}

.stats-item h3 {
  margin: 0 0 10px;
  color: #409EFF;
  font-size: 24px;
}

.stats-item p {
  margin: 0;
  color: #999;
}

.chart-card {
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.todo-card {
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.todo-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
  border-bottom: 1px solid #eee;
}

.todo-title {
  flex: 1;
}
</style>