// 注册页面
<template>
  <div class="login-container">
    <div class="login-box">
      <div style="font-weight: bold; font-size: 24px; text-align: center; margin-bottom: 30px; color: #1450aa">欢 迎 注 册</div>
      <el-form ref="formRef" :model="data.form" :rules="data.rules">
        <el-form-item prop="username">
          <el-input :prefix-icon="User" size="large" v-model="data.form.username" placeholder="请输入学号/工号"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input show-password :prefix-icon="Lock" size="large" v-model="data.form.password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input show-password :prefix-icon="Lock" size="large" v-model="data.form.confirmPassword" placeholder="请确认密码"></el-input>
        </el-form-item>
        <el-form-item prop="role">
          <el-select v-model="data.form.role" placeholder="请选择角色" size="large" style="width: 100%">
            <el-option label="教师" value="TEACHER"></el-option>
            <el-option label="学生" value="STUDENT"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button size="large" type="primary" style="width: 100%" @click="register">注 册</el-button>
        </el-form-item>
      </el-form>
      <div class="form-footer">
        <span>已有账号？</span>
        <a href="/login">立即登录</a>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import { User, Lock } from  "@element-plus/icons-vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";

// 自定义确认密码校验规则
const validatePass = (rule, value, callback) => {
  if (!value) {
    callback(new Error('请确认密码'))
  } else {
    if (value !== data.form.password) {
     callback(new Error("确认密码与原密码不一致！"))
    }
    callback()
  }
}

const data = reactive({
  form: {
    username: "", // 用户名
    password: "", // 密码
    confirmPassword: "", // 确认密码
    role: "", // 角色
  },
  rules: {
    username: [
      { required: true, message: '请输入学号或者工号', trigger: 'blur' }
    ],
    password: [
      { required: true, message: '请输入密码', trigger: 'blur' }
    ],
    confirmPassword: [
      { validator: validatePass, trigger: 'blur' }
    ],
    role: [
      { required: true, message: '请选择角色', trigger: 'change' }
    ],
  }
});

const formRef = ref()

// 注册方法
const register = () => {
  formRef.value.validate(valid => {
    if (valid) { // 表单校验通过
      request.post('/register', data.form).then(res => {
        if (res.code === '200') {
          ElMessage.success('注册成功');
          router.push('/login'); // 跳转到登录页面
        } else {
          ElMessage.error(res.msg || '注册失败');
        }
      }).catch(err => {
        ElMessage.error('请求失败，请稍后重试');
      });
    }
  });
};
</script>

<style scoped>
.login-container {
  height: 100vh;
  overflow: hidden;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(to top, #00467f, #a5cc82);
}

.login-box {
  width: 350px;
  padding: 30px;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  background-color: rgba(255, 255, 255, 0.5);
}

.form-footer {
  text-align: center;
  margin-top: 20px;
  color: #2c3e50;
}

.form-footer a {
  color: #3498db;
  text-decoration: none;
  margin-left: 5px;
  font-weight: 600;
}

.form-footer a:hover {
  text-decoration: underline;
}
</style>