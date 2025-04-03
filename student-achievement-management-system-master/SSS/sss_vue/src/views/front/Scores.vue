<template>
  <div class="card" style="width: 80%; margin: 5px auto">
    <!-- 查询条件 -->
    <div class="card" style="margin-bottom: 10px">
      <el-input
          v-model="data.courseName"
          prefix-icon="Search"
          style="width: 240px; margin-right: 10px"
          placeholder="课程名称"
      />
      <!-- 学生用户不显示学生姓名查询 -->
      <el-input
          v-if="data.user.role !== 'STUDENT'"
          v-model="data.studentName"
          prefix-icon="Search"
          style="width: 240px; margin-right: 10px"
          placeholder="学生姓名"
      />
      <el-input
          v-model="data.code"
          prefix-icon="Search"
          style="width: 240px; margin-right: 10px"
          placeholder="课程编号"
      />
      <el-select
          v-model="data.term"
          clearable
          placeholder="选择学期"
          style="width: 240px; margin-right: 10px"
      >
        <el-option
            v-for="item in termOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
<!--      <el-button type="primary" plain @click="load">查询</el-button>-->
<!--      <el-button type="warning" plain style="margin: 0 10px" @click="reset">重置</el-button>-->
      <el-button type="primary" style="margin-left: 10px" @click="load">查询</el-button>
      <el-button type="info" @click="reset">重置</el-button>
    </div>

    <!-- 数据表格 -->
    <div class="card" style="margin-bottom: 5px">
      <el-table stripe :data="data.tableData" style="width: 100%">
        <el-table-column label="序号" width="70">
          <template #default="{ $index }">
            {{ (data.pageNum - 1) * data.pageSize + $index + 1 }}
          </template>
        </el-table-column>
        <el-table-column prop="courseName" label="课程名称" />
        <el-table-column prop="code" label="课程编号" />
        <el-table-column v-if="data.user.role !== 'STUDENT'" prop="studentName" label="学生姓名" />
        <el-table-column prop="score" label="成绩">
          <template #default="scope">
            {{ formatScore(scope.row.score) }}
          </template>
        </el-table-column>
        <el-table-column prop="term" label="学期" />
      </el-table>
    </div>

    <!-- 分页组件 -->
    <div class="card">
      <el-pagination
          v-model:current-page="data.pageNum"
          v-model:page-size="data.pageSize"
          @current-change="load"
          background
          layout="prev, pager, next"
          :total="data.total"
      />
    </div>
  </div>
</template>

<script setup>
import { reactive } from "vue";
import request from "@/utils/request";

// 动态生成学期选项
const currentYear = new Date().getFullYear();
const termOptions = [];
for (let year = currentYear - 2; year <= currentYear + 1; year++) {
  termOptions.push({ value: `${year}春季学期`, label: `${year}春季学期` });
  termOptions.push({ value: `${year}秋季学期`, label: `${year}秋季学期` });
}

const data = reactive({
  courseName: "",
  studentName: "",
  code: "",
  term: "",

  tableData: [],
  total: 0,
  pageNum: 1,
  pageSize: 10,

  user: JSON.parse(localStorage.getItem("xm-user") || "{}")
});

// 加载数据
const load = () => {
  const params = {
    pageNum: data.pageNum,
    pageSize: data.pageSize,
    courseName: data.courseName,
    studentName: data.studentName,
    code: data.code,
    term: data.term
  };

  // 学生强制查询自己的成绩
  if (data.user.role === "STUDENT") {
    params.studentId = data.user.id;
    delete params.studentName; // 清除学生姓名查询条件
  }

  request.get("/studentCourse/selectPage", { params }).then(res => {
    if (res.code === "200") {
      data.tableData = res.data?.list || [];
      data.total = res.data?.total || 0;
    }
  });
};

// 重置查询
const reset = () => {
  data.courseName = "";
  data.studentName = "";
  data.code = "";
  data.term = "";
  load();
};

// 格式化成绩显示
const formatScore = score => {
  if (score === null || score === undefined) return '-';
  return parseFloat(score).toFixed(2);
};

// 初始化加载
load();
</script>

<style scoped>
.card {
  margin-bottom: 10px;
  padding: 15px;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,.1);
}
</style>