<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input v-model="data.username" prefix-icon="search" style="width: 240px; margin-right: 10px" placeholder="请输入账号查询" ></el-input>
      <el-input v-model="data.name" prefix-icon="search" style="width: 240px; margin-right: 10px" placeholder="请输入名称查询" ></el-input>
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
        <el-table-column prop="username" label="工号"/>
        <el-table-column prop="avatar" label="头像">
          <template v-slot="scope">
            <el-image
                style="width: 40px; height: 40px; border-radius: 50%; display: block"
                :src="scope.row.avatar"
                :preview-src-list="[scope.row.avatar]"
                preview-teleported
            ></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="姓名"/>
        <el-table-column prop="title" label="职称"/>
        <el-table-column prop="role" label="角色"/>
<!--        <el-table-column prop="college" label="院系"/>-->
        <el-table-column prop="college" label="学院">
          <template v-slot="scope">
            {{ scope.row.college }}
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="电话"/>
        <el-table-column prop="email" label="邮箱"/>
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

    <el-dialog title="教师信息" v-model="data.formVisible" width="40%" destroy-on-close>
      <el-form ref="form" :model="data.form" :rules="rules" label-width="70px" style="padding: 20px">
        <el-form-item prop="username" label="工号">
          <el-input v-model="data.form.username" placeholder="请输入工号"></el-input>
        </el-form-item>
        <el-form-item prop="avatar" label="头像">
          <el-upload
              :action="baseUrl + '/files/upload' "
              :on-success="handleFileUpload"
              list-type="picture" >
            <el-button type="primary">点击上传</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item prop="name" label="姓名">
          <el-input v-model="data.form.name" placeholder="请输入姓名"></el-input>
        </el-form-item>
        <el-form-item prop="phone" label="电话">
          <el-input v-model="data.form.phone" placeholder="请输入电话"></el-input>
        </el-form-item>
        <el-form-item prop="title" label="职称">
          <el-input v-model="data.form.title" placeholder="请输入职称"></el-input>
        </el-form-item>
<!--        <el-form-item prop="college" label="院系">-->
<!--          <el-input v-model="data.form.college" placeholder="请输入院系"></el-input>-->
<!--        </el-form-item>-->
        <el-form-item prop="collegeid" label="学院">
          <el-select v-model="data.form.collegeid" placeholder="请选择学院" style="width: 100%">
            <el-option
                v-for="item in collegeOptions"
                :key="item.id"
                :label="item.name"
                :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="email" label="邮箱">
          <el-input v-model="data.form.email" placeholder="请输入邮箱"></el-input>
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

// import {reactive} from "vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
import {Delete, Edit} from "@element-plus/icons-vue";
import { reactive, ref , onMounted } from "vue";

const baseUrl = import.meta.env.VITE_BASE_URL

const data =  reactive({
  formVisible: false,
  form: {},
  tableData: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
  name: null,
  username: null,
  ids: []
})

const load = () => {
  request.get('/teacher/selectPage',{
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      username: data.username,
      name: data.name
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
  loadColleges()
  data.formVisible = true
}

const handleEdit =(row) => {
  data.form= JSON.parse(JSON.stringify(row))
  if (row.college) {
    const college = collegeOptions.value.find(item => item.name === row.college)
    if (college) {
      data.form.collegeid = college.id
    }
  }
  data.formVisible = true
}

const add =() => {
  request.post('/teacher/add',data.form).then(res => {
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
  request.put('/teacher/update', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功'); // 正确调用消息组件
      data.formVisible = false; // 隐藏表单
      load(); // 重新加载数据
    }
  })
};

const save = () => {
  data.form.collegeId = data.form.collegeid;
  data.form.id ? update() : add(); // 根据id的存在与否决定调用update还是add函数
}


const del = (id) => {
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗?', '删除确认', { type: 'warning' }).then(res => {
    if (res === 'confirm') { // 通常确认按钮的响应值为 'confirm'
      request.delete('/teacher/delete/' + id).then(response => {
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
    console.error('MessageBox error:', err); // 处理 MessageBox 的错误
  });
};

const collegeOptions = ref([]) // 使用ref包裹数组

// 加载学院数据
const loadColleges = () => {
  request.get('/college/selectAll').then(res => {
    if (res.code === '200') {
      collegeOptions.value = res.data
    }
  })
}

// 在组件挂载时加载
onMounted(() => {
  loadColleges()
})

const rules = {
  username: [
    { required: true, message: '请输入工号', trigger: 'blur' }
  ],
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ],
}

const delBatch = () => {
  if (!data.ids.length) {
    ElMessage.warning({ message: '请选择数据' });
    return;
  }
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗?', '删除确认', { type: 'warning' }).then(res => {
    request.delete('/teacher/delete/batch', { data: data.ids } ).then(response => {
      if (response.code === '200') {
        ElMessage.success({ message: '操作成功' });
        load();
      } else {
        ElMessage.error(response.msg);
      }
    })
  }).catch(err => {
    console.error(err);
  });
};

const handleSelectionChange =(rows) => {
  data.ids = rows.map(v => v.id)
}

const handleFileUpload = (res) => {
  data.form.avatar =res.data
}

const reset = () => {
  data.name = null
  data.username =null
  load()
}

load()

</script>

<style scoped>


</style>




