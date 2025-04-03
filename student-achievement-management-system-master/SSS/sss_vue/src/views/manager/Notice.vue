<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input v-model="data.title" prefix-icon="search" style="width: 240px; margin-right: 10px" placeholder="请输入公告标题查询" ></el-input>
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
        <el-table-column prop="title" label="公告标题"/>
        <el-table-column prop="content" label="公告内容"/>
        <el-table-column prop="time" label="发布时间"/>
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

    <el-dialog title="公告信息" v-model="data.formVisible" width="40%" destroy-on-close>
      <el-form ref="form" :model="data.form" :rules="rules" label-width="70px" style="padding: 20px">
        <el-form-item prop="title" label="公告标题">
          <el-input v-model="data.form.title" placeholder="请输入公告标题"></el-input>
        </el-form-item>
        <el-form-item prop="content" label="公告内容">
          <el-input type="textarea" :rows="4" v-model="data.form.content" placeholder="请输入公告内容"></el-input>
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
import {ElMessage, ElMessageBox} from "element-plus";
import {Delete, Edit} from "@element-plus/icons-vue";

const data =  reactive({
  formVisible: false,
  form: {},
  tableData: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
  title: null,
  ids: []
})

const load = () => {
  request.get('/notice/selectPage',{
      params: {
        pageNum: data.pageNum,
        pageSize: data.pageSize,
        title: data.title
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
 request.post('/notice/add',data.form).then(res => {
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
  request.put('/notice/update', data.form).then(res => {
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

const del = (id) => {
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗?', '删除确认', { type: 'warning' }).then(res => {
    if (res === 'confirm') { // 通常确认按钮的响应值为 'confirm'
      request.delete('/notice/delete/' + id).then(response => {
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

const delBatch = () => {
  if (!data.ids.length) {
    ElMessage.warning({ message: '请选择数据' });
    return;
  }
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗?', '删除确认', { type: 'warning' }).then(res => {
    request.delete('/notice/delete/batch', { data: data.ids } ).then(response => {
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

const reset = () => {
  data.title = null
  load()
}

load()

</script>

<style scoped>


</style>