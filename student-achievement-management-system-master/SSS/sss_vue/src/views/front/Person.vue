<template>
  <div style="width: 50%; margin: 5px auto" class="card">
    <el-form ref="user" :model="data.user" label-width="60px" style="padding: 20px">
      <div style="text-align: center; margin-bottom: 20px">
        <el-upload
            :action="baseUrl + '/files/upload' "
            :on-success="handleFileUpload"
            :show-file-list="false"
            class="avatar-uploader"
        >
          <img v-if="data.user.avatar" :src="data.user.avatar" class="avatar">
          <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
        </el-upload>
      </div>
      <el-form-item prop="username" label="用户名">
        <el-input disabled v-model="data.user.username" placeholder="请输入用户名"></el-input>
      </el-form-item>
      <el-form-item prop="name" label="姓名">
        <el-input v-model="data.user.name" placeholder="请输入姓名"></el-input>
      </el-form-item>
      <el-form-item prop="phone" label="电话">
        <el-input v-model="data.user.phone" placeholder="请输入电话"></el-input>
      </el-form-item>
      <el-form-item prop="email" label="邮箱">
        <el-input v-model="data.user.email" placeholder="请输入邮箱"></el-input>
      </el-form-item>
    </el-form>
    <div style="text-align: center">
      <el-button type="primary" @click="update">保 存</el-button>
    </div>
  </div>
</template>

<script setup>
import { reactive } from "vue";
import {ElMessage} from "element-plus";
import request from "@/utils/request.js";
import router from "@/router/index.js";

  const baseUrl = import.meta.env.VITE_BASE_URL
  const data = reactive({
    user: JSON.parse(localStorage.getItem('xm-user') || '{}')
    })

  const handleFileUpload = (res) => {
    data.user.avatar =res.data
  }

  const emit = defineEmits(['updateUser'])
const update = () => {
  let url = '';
  if (data.user.role === 'ADMIN') {
    url = 'admin/update';
  } else if (data.user.role === 'TEACHER') {
    url = 'teacher/update';
  } else if (data.user.role === 'STUDENT') {
    url = 'student/update';
  }

  request.put(url, data.user).then(res => {
    if (res.code === '200') {
      ElMessage.success('保存成功');
      localStorage.setItem('xm-user', JSON.stringify(data.user));
      emit('updateUser');
    } else {
      ElMessage.error(res.msg);
    }
  });
};
</script>

<style scoped>
.avatar-uploader .avatar {
  width: 120px;
  height: 120px;
  display: block;
}

.avatar-uploader {
  height: 120px;
}
</style>

<style>
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 50%;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  text-align: center;
}
</style>