<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input v-model="data.name" prefix-icon="search" style="width: 240px; margin-right: 10px" placeholder="请输入课程名称查询"></el-input>
      <el-input v-model="data.code" prefix-icon="search" style="width: 240px; margin-right: 10px" placeholder="请输入课程编号查询"></el-input>
      <el-input v-model="data.teacherName" prefix-icon="search" style="width: 240px; margin-right: 10px" placeholder="请输入任课老师查询"></el-input>
      <el-button type="primary" plain @click="load">查询</el-button>
      <el-button type="warning" plain style="margin: 0 10px" @click="reset">重置</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px">
      <el-button type="success" plain @click="handleAdd">新增</el-button>
      <el-button type="danger" plain @click="delBatch">批量删除</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-table stripe :data="data.tableData" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="35"/>
        <el-table-column label="序号" width="70">
          <template #default="{ $index }">
            {{ (data.pageNum - 1) * data.pageSize + $index + 1 }}
          </template>
        </el-table-column>
        <el-table-column prop="name" label="课程名称"/>
        <el-table-column prop="code" label="课程编号"/>
        <el-table-column prop="credit" label="学分" :formatter="formatCredit"/>
        <el-table-column prop="hour" label="课时"/>
        <el-table-column prop="college" label="开课学院"/>
        <el-table-column prop="teacherName" label="任课老师"/>
        <el-table-column label="操作" width="100" fixed="right">
          <template v-slot="scope">
            <el-button type="primary" circle :icon="Edit" @click="handleEdit(scope.row)"></el-button>
            <el-button type="danger" circle :icon="Delete" @click="del(scope.row.id)"></el-button>
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

    <el-dialog title="课表信息" v-model="data.formVisible" width="40%" destroy-on-close>
      <el-form ref="form" :model="data.form" :rules="data.rules" label-width="100px" style="padding: 20px">
        <el-form-item label="课程名称" prop="name">
          <el-input v-model="data.form.name" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="课程编号" prop="code">
          <el-input v-model="data.form.code" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="学分" prop="credit">
          <el-input v-model="data.form.credit" type="number" step="0.5" autocomplete="off"
                    @keyup.native="handleCreditInput"
          />
        </el-form-item>
        <el-form-item label="课时" prop="hour">
          <el-input v-model="data.form.hour" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="开课学院" prop="collegeid">
          <el-select
              v-model="data.form.collegeId"
              clearable
              placeholder="请选择学院"
              style="width: 100%"
          >
            <el-option
                v-for="item in collegeOptions"
                :key="item.id"
                :label="item.name"
                :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="任课老师" prop="teacherId">
          <el-select
              v-model="data.form.teacherId"
              clearable
              placeholder="请选择教师"
              style="width: 100%"
          >
            <el-option
                v-for="t in data.teachers"
                :key="t.id"
                :label="`${t.name} - ${t.college || '未知学院'}`"
                :value="t.id"
            />
          </el-select>
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
import {onMounted, reactive, ref} from "vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
import {Delete, Edit} from "@element-plus/icons-vue";

const baseUrl = import.meta.env.VITE_BASE_URL;

const form = ref(null); // 获取表单实例

const collegeOptions = ref([]); // 学院下拉选项

const data = reactive({
  formVisible: false,
  form: {
    collegeId: null,
    teacherId: null
  },
  tableData: [],
  teachers: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
  name: null,
  teacherName: null,
  code: null,
  ids: [],
  rules: {
    name: [
      { required: true, message: '请输入课程名称', trigger: 'blur' }
    ],
    code: [
      { required: true, message: '请输入课程编号', trigger: 'blur' }
    ],
    credit: [
      { required: true, message: '请输入学分', trigger: 'blur' },
      {
        validator: (rule, value, callback) => {
          if (value === '') {
            callback(new Error('请输入学分'));
            return;
          }
          const numericValue = parseFloat(value);
          if (isNaN(numericValue)) {
            callback(new Error('请输入有效的数字'));
          } else if (numericValue <= 0 || numericValue > 4) {
            callback(new Error('学分必须大于0且小于等于4.0'));
          } else if (!/^\d+(\.\d)?$/.test(value)) {
            callback(new Error('最多允许一位小数'));
          } else if (Math.round(numericValue * 10) % 5 !== 0) {
            callback(new Error('学分必须是0.5的倍数（如1.0、1.5）'));
          } else {
            callback();
          }
        },
        trigger: ['blur', 'change']
      }
    ],
    collegeId: [
      { required: true, message: '请选择开课学院', trigger: 'change' }
    ],
    teacherId: [
      { required: true, message: '请选择任课老师', trigger: 'change' }
    ],
  }
});

const load = () => {
  request.get('/course/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      teacherName: data.teacherName,
      name: data.name,
      code: data.code,
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data?.list || [];
      data.total = res.data?.total;
    }
  });
};

const handleAdd = () => {
  data.form = { }
  loadColleges()
  data.formVisible = true;
};

const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row));
  data.form.collegeId = row.collegeId;
  data.formVisible = true;
};

const add = () => {
  data.form.credit = Number(data.form.credit);
  request.post('/course/add', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功');
      load();
      data.formVisible = false;
    } else {
      ElMessage.error(res.msg);
    }
  });
};

const update = () => {
  data.form.credit = Number(data.form.credit);
  request.put('/course/update', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功');
      data.formVisible = false;
      load();
    }
  });
};

const save = () => {
  form.value.validate((valid) => {
    if (valid) {
      data.form.credit = data.form.credit.toString();
      console.log('提交数据:', data.form)
      data.form.id ? update() : add();
    } else {
      ElMessage.warning('请检查表单输入是否正确');
    }
  });
};

const del = (id) => {
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗?', '删除确认', { type: 'warning' }).then(res => {
    if (res === 'confirm') {
      request.delete('/course/delete/' + id).then(response => {
        if (response.code === '200') {
          ElMessage.success('删除成功');
          load();
        } else {
          ElMessage.error(response.msg);
        }
      }).catch(error => {
        console.error(error);
      });
    }
  }).catch(err => {
    console.error('MessageBox error:', err);
  });
};

const delBatch = () => {
  if (!data.ids.length) {
    ElMessage.warning({ message: '请选择数据' });
    return;
  }
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗?', '删除确认', { type: 'warning' }).then(res => {
    request.delete('/course/delete/batch', { data: data.ids }).then(response => {
      if (response.code === '200') {
        ElMessage.success({ message: '操作成功' });
        load();
      } else {
        ElMessage.error(response.msg);
      }
    });
  }).catch(err => {
    console.error(err);
  });
};

const handleSelectionChange = (rows) => {
  data.ids = rows.map(v => v.id);
};

const handleFileUpload = (res) => {
  data.form.avatar = res.data;
};

const reset = () => {
  data.name = null;
  data.code = null;
  data.teacherName = null;
  load();
};

// 获取教师列表信息
const loadTeachers = () => {
  request.get('/teacher/selectAll').then(res => {
    if (res.code === '200') {
      data.teachers = res.data || [];
    }
  });
};

// 获取学院列表信息
const loadColleges = () => {
  request.get('/college/selectAll').then(res => {
    if (res.code === '200') {
      // data.teachers = res.data || []
      collegeOptions.value = res.data;
    }
  })
}

// 在created生命周期调用
onMounted(() => {
  load()
  loadTeachers()
  loadColleges()
})

//整数学分后加上0，如显示4.0而不是4
const formatCredit = (row, column, cellValue) => {
  return parseFloat(cellValue).toFixed(1);
};

const handleCreditInput = (e) => {
  // 过滤非法字符
  e.target.value = e.target.value.replace(/[^0-9.]/g, '')
  // 强制保留一位小数
  if (e.target.value.includes('.')) {
    e.target.value = e.target.value.slice(0, e.target.value.indexOf('.') + 2)
  }
}

load()

</script>

<style scoped>


</style>




