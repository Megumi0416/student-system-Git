<template>
  <div>
    <!-- 查询条件 -->
    <div class="card" style="margin-bottom: 10px">
      <el-input
          v-model="data.courseName"
          prefix-icon="Search"
          style="width: 240px; margin-right: 10px"
          placeholder="课程名称"
      />
      <el-input
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
      <el-button type="primary" plain @click="load">查询</el-button>
      <el-button type="warning" plain style="margin: 0 10px" @click="reset">重置</el-button>
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
        <el-table-column prop="studentName" label="学生姓名" />
        <el-table-column prop="score" label="成绩">
          <template #default="scope">
            {{ formatScore(scope.row.score) }}
          </template>
        </el-table-column>
        <el-table-column label="学期">
          <template #default="scope">
            <el-select
                v-model="scope.row.term"
                @change="saveTerm(scope.row)"
                placeholder="选择学期"
                style="width: 100%"
            >
              <el-option
                  v-for="item in termOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
              />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column label="录入成绩" width="220" fixed="right">
          <template #default="scope">
<!--            <el-input-->
<!--                v-model="scope.row.tempScore"-->
<!--                @keyup.enter="saveScore(scope.row)"-->
<!--                placeholder="输入成绩后按回车保存"-->
<!--                style="margin-top: 10px; width: 100%"-->
<!--            />-->
            <el-input
                v-model="scope.row.tempScore"
                @keyup.enter="saveScore(scope.row)"
                placeholder="输入成绩后按回车保存"
                style="margin-top: 10px; width: 100%"
                type="number"
                step="0.5"
                :min="0"
                :max="100"
            />
          </template>
        </el-table-column>
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
import { Search } from "@element-plus/icons-vue";
import { ElMessage } from "element-plus";
import request from "@/utils/request";

// 动态学期选项
const termOptions = [];
const currentYear = new Date().getFullYear(); // 获取当前年份

for (let year = currentYear - 1; year <= currentYear + 1; year++) { // 生成当前年份前后各1年的学期
  termOptions.push({ value: `${year}秋季学期`, label: `${year}秋季学期` });
  termOptions.push({ value: `${year}春季学期`, label: `${year}春季学期` });
}

const data = reactive({
  // 查询条件
  courseName: "",
  studentName: "",
  code: "",

  // 表格数据
  tableData: [],
  total: 0,
  pageNum: 1,
  pageSize: 10,

  // 用户信息
  user: JSON.parse(localStorage.getItem("xm-user") || "{}")
});

// 加载数据
const load = () => {
  const params = {
    pageNum: data.pageNum,
    pageSize: data.pageSize,
    courseName: data.courseName,
    studentName: data.studentName,
    code: data.code
  };

  if (data.user.role === "STUDENT") {
    params.studentId = data.user.id;
  } else if (data.user.role === "TEACHER") {
    params.teacherId = data.user.id;
  }

  request.get("/studentCourse/selectPage", { params }).then(res => {
    if (res.code === "200") {
      data.tableData = (res.data?.list || []).map(item => ({
        ...item,
        tempScore: "" // 保留临时成绩字段
      }));
      data.total = res.data?.total || 0;
    }
  });
};

// 重置查询
const reset = () => {
  data.courseName = "";
  data.studentName = "";
  data.code = "";
  load();
};

// 保存学期修改
const saveTerm = row => {
  request.put("/studentCourse/update", {
    id: row.id,
    term: row.term
  }).then(res => {
    if (res.code === "200") {
      ElMessage.success("学期更新成功");
      load(); // 重新加载保持数据一致性
    } else {
      ElMessage.error("更新失败");
    }
  });
};

// 保存成绩
const saveScore = row => {
  if (!row.tempScore) {
    ElMessage.warning("请输入成绩");
    return;
  }
  const score = parseFloat(row.tempScore);
  // 校验成绩范围
  if (isNaN(score) || score < 0 || score > 100) {
    ElMessage.warning("成绩必须在 0 到 100 之间");
    return;
  }

  request.put("/studentCourse/update", {
    id: row.id,
    score: parseFloat(row.tempScore)
  }).then(res => {
    if (res.code === "200") {
      ElMessage.success("成绩保存成功");
      load();
    } else {
      ElMessage.error("保存失败");
    }
  });
};

// 格式化成绩
const formatScore = score => {
  return typeof score === 'number' ? score.toFixed(2) : parseFloat(score).toFixed(2);
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