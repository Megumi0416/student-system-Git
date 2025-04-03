<template>
  <div class="card" style="width: 80%; margin: 5px auto">
    <div class="card" style="margin-bottom: 10px">
      <el-input style="width: 260px; margin-right: 10px" v-model="data.courseName" placeholder="请输入课程名称查询"
                :prefix-icon="Search"/>
      <el-input style="width: 260px; margin-right: 10px" v-model="data.code" placeholder="请输入课程编号查询"
                :prefix-icon="Search"/>
      <el-button type="primary" style="margin-left: 10px" @click="load">查询</el-button>
      <el-button type="info" @click="reset">重置</el-button>
    </div>

    <div class="card" style="margin-bottom: 10px">

      <div>
        <el-table :data="data.tableData" style="width: 100%">
          <el-table-column label="序号" width="70">
            <template #default="{ $index }">
              {{ (data.pageNum - 1) * data.pageSize + $index + 1 }}
            </template>
          </el-table-column>
          <el-table-column prop="courseName" label="课程名称"/>
          <el-table-column prop="code" label="课程编号"/>
          <el-table-column prop="studentName" label="学生名称"/>
          <el-table-column label="操作" width="180">
            <template #default="scope">
              <el-button type="danger" @click="del(scope.row)">退课</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

    </div>

    <div class="card">
      <el-pagination v-model:current-page="data.pageNum" v-model:page-size="data.pageSize"
                     @current-change="handelCurrentChange"
                     background layout="prev, pager, next" :total="data.total"/>
    </div>

    <el-dialog width="35%" v-model="data.formVisible" title="成绩信息">
      <el-form :model="data.gradeForm" label-width="100px" label-position="right" style="padding-right: 40px">
        <el-form-item label="课程名称">
          <el-input v-model="data.gradeForm.courseName" autocomplete="off" disabled />
        </el-form-item>
        <el-form-item label="分数">
          <el-input v-model="data.gradeForm.score" autocomplete="off" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.formVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">保 存</el-button>
        </span>
      </template>
    </el-dialog>

  </div>
</template>

<script setup>
import {reactive} from "vue"
import {Search} from '@element-plus/icons-vue'
import request from "@/utils/request";
import {ElMessage, ElMessageBox} from "element-plus";

const data = reactive({
  courseName: '',
  code: '',
  tableData: [],
  total: 0,
  pageNum: 1,  // 当前的页码
  pageSize: 10,  // 每页的个数
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  gradeForm: {},
  formVisible: false
})

const load = () => {
  let params = {
    pageNum: data.pageNum,
    pageSize: data.pageSize,
    courseName: data.courseName,
    code: data.code
  }
  if (data.user.role === 'STUDENT') {  // 如果当前登录的是学生，那就查询自己的选课记录
    params.studentId = data.user.id
  }
  request.get('/studentCourse/selectPage', {
    params: params
  }).then(res => {
    data.tableData = res.data?.list || []
    data.total = res.data?.total || 0
  })
}
// 调用方法获取后台数据
load()

const handelCurrentChange = () => {
  // 当翻页的时候重新加载数据即可
  load()
}

const reset = () => {
  data.courseName = ''
  data.code = ''
  load()
}

const del = (row) => {
  // 检查是否存在成绩
  if (row.score !== null && row.score !== undefined) {
    ElMessage.warning('该课程已结课，无法退课')
    return
  }

  ElMessageBox.confirm('退课操作不可恢复，确认要退选该课程吗？', '退课确认', {
    type: 'warning',
    confirmButtonText: '确认退课',
    cancelButtonText: '取消'
  }).then(() => {
    request.delete('/studentCourse/delete/' + row.id).then(res => {
      if (res.code === '200') {
        load()
        ElMessage.success("退课成功")
      } else {
        ElMessage.error(res.msg || "操作失败")
      }
    })
  }).catch(() => {
    // 取消操作
  })
}

const save = () => {
  request.post('/grade/add', data.gradeForm).then(res => {
    if (res.code === '200') {
      data.formVisible = false  // 关闭弹窗
      ElMessage.success("操作成功")
    } else {
      ElMessage.error(res.msg)
    }
  })
}
</script>