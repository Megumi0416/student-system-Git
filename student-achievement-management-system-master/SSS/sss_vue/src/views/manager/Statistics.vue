<template>
  <div>
    <!-- 筛选条件 -->
    <div class="card" style="margin-bottom: 10px">
      <el-select
          v-model="term"
          placeholder="请选择学期"
          clearable
          @change="load"
          style="width: 25%"
      >
        <el-option
            v-for="item in termOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-button type="primary" plain @click="load">查询</el-button>
    </div>

    <!-- 图表容器 -->
    <div class="card">
      <div ref="chart" style="height: 400px;"></div>
    </div>
  </div>
</template>

<script setup>
import {onMounted, ref, onBeforeUnmount} from 'vue'
import * as echarts from 'echarts'
import request from '@/utils/request'

const term = ref('')
const chart = ref(null)
// 获取当前用户信息
const user = JSON.parse(localStorage.getItem('xm-user') || '{}')
let myChart = null

// 动态生成最近3年的学期选项
const termOptions = []
const currentYear = new Date().getFullYear()
for (let year = currentYear - 2; year <= currentYear + 1; year++) {
  termOptions.push({value: `${year}秋季学期`, label: `${year}秋季学期`})
  termOptions.push({value: `${year}春季学期`, label: `${year}春季学期`})
}

// 加载数据
const load = async () => {
  try {
    const params = {term: term.value}
    // 不再添加教师名称限制
    // if (user.role === 'TEACHER') {
    //   params.teacherName = user.username
    // }
    const res = await request.get('/studentCourse/statistics', {params})
    if (res.code === '200') {
      // 移除教师筛选条件，直接使用所有数据
      // const filteredData = (res.data || []).filter(item => {
      //   if (user.role !== 'TEACHER') return true
      //   return item?.teacherName === user?.username
      // })
      initChart(res.data || [])
    }
  } catch (error) {
    console.error('数据加载失败:', error)
  }
}

// 初始化图表
const initChart = (data) => {
  if (!myChart) {
    myChart = echarts.init(chart.value)
  }

  const option = {
    title: {
      text: '课程成绩统计',
      left: 'center',
      textStyle: {
        fontSize: 18,
        color: '#333'
      }
    },
    legend: {
      orient: 'horizontal',
      right: '5%',
      top: '5%',
      itemGap: 20,
      textStyle: {
        color: '#666',
        fontSize: 14
      },
      itemWidth: 16,
      itemHeight: 12,
      backgroundColor: 'rgba(240,240,240,0.8)',
      borderRadius: 6,
      padding: [8, 12]
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    grid: {
      top: '20%',
      left: '3%',
      right: '4%',
      bottom: '12%',  // 增加底部间距
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: data.map(item => `${item.courseName}(${item.code})`),
      axisLabel: {
        rotate: 0,  // 取消倾斜
        fontSize: 12,
        interval: 0,  // 显示所有标签

        formatter: (value) => {
          const maxLength = 15
          if (value.length <= maxLength) return value // 短名称直接显示

          // 优先在最后一个左括号前换行
          const bracketIndex = value.lastIndexOf('(')
          if (bracketIndex > 0 && bracketIndex < maxLength) {
            return [
              value.substring(0, bracketIndex),  // 第一行：课程名称
              value.substring(bracketIndex)      // 第二行：(课程代码)
            ].join('\n')
          }

          // 次优方案：在空格处断开（避免截断单词）
          let breakPoint = maxLength
          while (breakPoint > 0 && value[breakPoint] !== ' ') {
            breakPoint--
          }
          return [
            value.substring(0, breakPoint || maxLength), // 避免breakPoint为0的情况
            value.substring(breakPoint || maxLength)
          ].join('\n')
        }
      },
      axisTick: {
        alignWithLabel: true,
        interval: 0  // 显示所有刻度
      }
    },
    yAxis: {
      type: 'value',
      min: 0,
      max: 100,
      axisLabel: {
        fontSize: 12
      }
    },
    series: [
      {
        name: '平均分',
        type: 'bar',
        data: data.map(item => item.avgScore.toFixed(2)),
        itemStyle: {
          color: '#409EFF',
          borderRadius: [4, 4, 0, 0]
        },
        label: {
          show: true,
          position: 'top',
          color: '#409EFF',
          formatter: ({value}) => parseFloat(value).toFixed(1)
        }
      },
      {
        name: '最高分',
        type: 'bar',
        data: data.map(item => item.maxScore),
        itemStyle: {
          color: '#67C23A',
          borderRadius: [4, 4, 0, 0]
        },
        label: {
          show: true,
          position: 'top',
          color: '#67C23A'
        }
      },
      {
        name: '最低分',
        type: 'bar',
        data: data.map(item => item.minScore),
        itemStyle: {
          color: '#F56C6C',
          borderRadius: [4, 4, 0, 0]
        },
        label: {
          show: true,
          position: 'top',
          color: '#F56C6C'
        }
      }
    ]
  }

  myChart.setOption(option)
}

// 生命周期处理
onMounted(() => {
  load()
  window.addEventListener('resize', handleResize)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize)
  myChart?.dispose()
})

const handleResize = () => {
  myChart?.resize()
}
</script>

<style scoped>
.card {
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}
</style>