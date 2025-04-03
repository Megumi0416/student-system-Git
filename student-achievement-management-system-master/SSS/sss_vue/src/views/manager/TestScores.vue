<template>
  <div>
    <div class="card" style="margin-bottom: 10px">
      <el-input v-model="data.coursename" prefix-icon="search" style="width: 240px; margin-right: 10px" placeholder="请输入课程名称查询" ></el-input>
      <el-input v-model="data.studentname" prefix-icon="search" style="width: 240px; margin-right: 10px" placeholder="请输入学生名称查询" ></el-input>
      <el-select v-model="data.term" clearable placeholder="选择学期" style="width: 240px; margin-right: 10px">
        <el-option v-for="item in termOptions" :key="item.value" :label="item.label" :value="item.value"/>
      </el-select>
      <el-button type="primary" plain @click="load">查询</el-button>
      <el-button type="warning" plain style="margin: 0 10px" @click="reset">重置</el-button>
      <el-button type="success" plain @click="exportData">导出</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-table stripe :data="data.tableData" >
        <el-table-column prop="coursename" label="课程名称"/>
        <el-table-column prop="code" label="课程编号" />
        <el-table-column prop="studentname" label="学生姓名"/>
<!--        <el-table-column prop="score" label="成绩"/>-->
        <el-table-column prop="score" label="成绩">
          <template #default="scope">
            {{ formatScore(scope.row.score) }}
          </template>
        </el-table-column>
        <el-table-column prop="term" label="学期"/>
        <el-table-column label="操作" width="100" fixed="right">
          <template v-slot="scope">
            <el-button type="primary" circle :icon="Edit" @click="handleEdit(scope.row)"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="card">
      <el-pagination
          @current-change="load"
          background
          layout="prev, pager, next"
          :page-size="data.pageSize"
          v-model:current-page="data.pageNum"
          :total="data.total"
      />
    </div>

    <el-dialog title="考试成绩信息" v-model="data.formVisible" width="40%" destroy-on-close>
      <el-form ref="form" :model="data.form" :rules="rules" label-width="70px" style="padding: 20px">
        <el-form-item prop="scores" label="成绩">
          <el-input v-model="data.form.score" placeholder="请输入成绩"></el-input>
        </el-form-item>
        <el-form-item prop="term" label="学期">
          <el-input v-model="data.form.term" placeholder="请输入学期"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span  class="dialog-footer">
          <el-button @click="data.formVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">确 定</el-button>
        </span>
      </template>
    </el-dialog>


  </div>

</template>

<script setup>

import {reactive} from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import {Edit} from "@element-plus/icons-vue";
import * as XLSX from 'xlsx';
import { saveAs } from 'file-saver';

const baseUrl = import.meta.env.VITE_BASE_URL

const data = reactive({
  formVisible: false,
  form: {},
  tableData: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
  studentname: null,
  coursename: null,
  term: null,
  ids: []
})

const load = () => {
  request.get('/testscores/selectPage',{
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      coursename: data.coursename,
      studentname: data.studentname,
      term: data.term
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data?.list || [];
      data.total = res.data?.total;
    }
  })
};

const handleAdd =() => {
  data.form= { }
  data.formVisible = true
}

const handleEdit =(row) => {
  data.form= JSON.parse(JSON.stringify(row))
  data.formVisible = true
}

const add =() => {
  request.post('/testscores/add',data.form).then(res => {
    if (res.code == '200') {
      ElMessage.success('操作成功')
      load()
      data.formVisible = false
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const update = () => {
  request.put('/testscores/update', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功'); // 正确调用消息组件
      data.formVisible = false; // 隐藏表单
      load(); // 重新加载数据
    }
  })
};

const save = () => {
  data.form.id ? update() : add(); // 根据id的存在与否决定调用update还是add函数
}


const handleSelectionChange =(rows) => {
  data.ids = rows.map(v => v.id)
}

const handleFileUpload = (res) => {
  data.form.avatar =res.data
}

const reset = () => {
  data.studentname = null
  data.coursename =null
  data.term = null
  load()
}

// 在setup()中添加：
const termOptions = reactive([])
const currentYear = new Date().getFullYear()

// 生成近三年学期选项
for (let year = currentYear - 1; year <= currentYear + 1; year++) {
  termOptions.push(
      { value: `${year}秋季学期`, label: `${year}秋季学期` },
      { value: `${year}春季学期`, label: `${year}春季学期` }
  )
}

const formatScore = (score) => {
  // 如果 score 是数值类型，直接使用 toFixed(2)；如果是字符串，先转换为数值
  return typeof score === 'number' ? score.toFixed(2) : parseFloat(score).toFixed(2);
};

const exportData = () => {
  if (data.tableData.length === 0) {
    ElMessage.warning('当前没有数据可导出');
    return;
  }

  // 处理数据格式
  const processedData = data.tableData.map((item, index) => ({
    '序号': index + 1,
    '学号': item.name || '未知',
    '姓名': item.studentname || '未知',
    '课程编号': item.code || '未知',
    '课程名称': item.coursename || '未知',
    '学分': item.credit || '0',
    '成绩': item.score ? Number(item.score).toFixed(2) : '无',
    '学期': item.term || '未指定'
  }));

  // 定义表头顺序
  const headers = [
    { key: '序号', title: '序号' },
    { key: '学号', title: '学号' },
    { key: '姓名', title: '姓名' },
    { key: '课程编号', title: '课程编号' },
    { key: '课程名称', title: '课程名称' },
    { key: '学分', title: '学分' },
    { key: '成绩', title: '成绩' },
    { key: '学期', title: '学期' }
  ];

  // 创建工作表
  const worksheet = XLSX.utils.json_to_sheet(processedData, {
    header: headers.map(h => h.key) // 指定列顺序
  });

  // 修改表头中文显示
  XLSX.utils.sheet_add_aoa(worksheet, [headers.map(h => h.title)], { origin: 'A1' });

  // 创建工作簿并添加工作表
  const workbook = XLSX.utils.book_new();
  XLSX.utils.book_append_sheet(workbook, worksheet, "学生成绩");

  // 生成文件并保存
  const excelBuffer = XLSX.write(workbook, { bookType: 'xlsx', type: 'array' });
  const dataBlob = new Blob([excelBuffer], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });

  // 生成带日期格式的文件名
  const filename = `学生成绩_${new Date().toLocaleDateString()}.xlsx`;

  saveAs(dataBlob, filename);
  ElMessage.success('导出成功');
};

load()

</script>

<style scoped>


</style>