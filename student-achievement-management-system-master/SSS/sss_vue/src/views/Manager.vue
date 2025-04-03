<template>
  <div class="manager-container">
    <div class="manager-header">
      <div class="manager-header-left">
        <img src="@/assets/imgs/logo.png" alt="">
        <div class="title">成绩管理系统</div>
      </div>
      <div class="manager-header-center">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item :to="{ path: '/manager/home' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item>{{ route.meta.name}}</el-breadcrumb-item>
        </el-breadcrumb>
      </div>
      <div class="manager-header-right">
        <el-dropdown style="cursor: pointer">
          <div style="padding-right: 20px; display: flex; align-items: center">
            <img style="width: 40px; height: 40px; border-radius: 50%;" :src="data.user.avatar" alt="">
            <span style="margin-left: 5px; color: white">{{ data.user.name }}</span><el-icon color="#fff"><arrow-down /></el-icon>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="router.push('/manager/person')">个人资料</el-dropdown-item>
              <el-dropdown-item @click="router.push('/manager/password')">修改密码</el-dropdown-item>
              <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>

    <!--    主题部分-->
    <div style="display: flex">
      <div class="manager-main-left">
<!--               <el-menu :default-active="router.currentRoute.value.path"-->
<!--                        :default-openeds="['1', '2']"-->
<!--                        router-->
<!--               >-->
              <el-menu :default-active="activeMenuIndex"
                       :default-openeds="['1', '2', '3', '4', '5']"
                       router
              >
          <el-menu-item index="/manager/home">
            <el-icon><HomeFilled /></el-icon>
            <span>系统首页</span>
          </el-menu-item>
          <el-sub-menu index="1">
            <template #title>
              <el-icon><Menu /></el-icon>
              <span>信息管理</span>
            </template>
            <el-menu-item index="/manager/notice" v-if="data.user.role === 'ADMIN'">系统公告</el-menu-item>
            <el-menu-item index="/manager/message">消息回复</el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="2" v-if="data.user.role === 'ADMIN'">
            <template #title>
              <el-icon><Menu /></el-icon>
              <span>用户管理</span>
            </template>
            <el-menu-item index="/manager/admin">管理员信息</el-menu-item>
            <el-menu-item index="/manager/student">学生信息</el-menu-item>
            <el-menu-item index="/manager/teacher">教师信息</el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="3" v-if="data.user.role === 'ADMIN'">
            <template #title>
              <el-icon><Menu /></el-icon>
              <span>课程管理</span>
            </template>
              <el-menu-item index="/manager/course">课程信息</el-menu-item>
          </el-sub-menu>
                <el-sub-menu index="4">
                  <template #title>
                    <el-icon><Menu /></el-icon>
                    <span>成绩管理</span>
                  </template>
                  <el-menu-item index="/manager/testscores" v-if="data.user.role === 'ADMIN'">成绩信息</el-menu-item>
                  <el-menu-item index="/manager/studentcourse" v-if="data.user.role === 'TEACHER'">成绩录入</el-menu-item>
                  <el-menu-item index="/manager/statistics">成绩分析</el-menu-item>
                </el-sub-menu>
                <el-sub-menu index="5" v-if="data.user.role === 'ADMIN'">
                  <template #title>
                    <el-icon><Menu /></el-icon>
                    <span>学院管理</span>
                  </template>
                  <el-menu-item index="/manager/college">学院信息</el-menu-item>
                </el-sub-menu>
        </el-menu>
      </div>
      <div class="manager-main-right">
        <RouterView @updateUser="updateUser" />
      </div>
    </div>
    <!--    主题部分-->

  </div>
</template>

<!--<script setup>-->
<!--import router from "@/router/index.js"-->

<!--</script>-->
<script setup lang="ts">
import { computed } from 'vue';
import { useRoute } from 'vue-router';
import router from "@/router/index"
import { reactive } from "vue";
import {ElMessage} from "element-plus";
import {ArrowDown} from "@element-plus/icons-vue";

// 使用 useRoute 钩子获取当前路由
const route = useRoute();

// 计算属性，根据当前路由的路径设置 activeMenuIndex
const activeMenuIndex = computed(() => {
  return route.path;
});

//从浏览器的本地存储中加载用户信息，并将这些信息存储在一个响应式对象中
const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}') as User
})

const logout = () => {
  localStorage.removeItem('xm-user')  //清除缓存
  router.push('/login')
}

const updateUser = () => {
  data.user = JSON.parse(localStorage.getItem('xm-user') || '{}')
}

interface User {
  id: number
  name: string
  avatar: string
  role: 'ADMIN' | 'TEACHER'  // 明确角色类型
}

if (!data.user.id) {
  logout()
  ElMessage.error('请登录！')
}

</script>

<style scoped>
@import "@/assets/css/Manager.css";
</style>





