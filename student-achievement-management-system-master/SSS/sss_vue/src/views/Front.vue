<template>
  <div>
    <div class="front-notice"><el-icon><Bell /></el-icon>公告：<span v-if="data.noticeData.length">{{ data.top }}</span>
      <span v-else>暂无公告</span></div>
    <div class="front-header">
      <div class="front-header-left">
        <img src="../assets/imgs/logo.png" alt="">
        <div class="title">成绩管理系统</div>
      </div>
      <div class="front-header-center">
        <el-menu :default-active="activeMenuIndex" router mode="horizontal">
          <el-menu-item index="/front/home">首页</el-menu-item>
          <el-menu-item index="/front/courselist">选课中心</el-menu-item>
          <el-menu-item index="/front/studentcourse">已选课程</el-menu-item>
          <el-menu-item index="/front/scores">我的成绩</el-menu-item>
          <el-menu-item index="/front/message">消息回复</el-menu-item>
          <el-menu-item index="/front/person">个人中心</el-menu-item>
        </el-menu>
      </div>
      <div class="front-header-right">
        <div v-if="!data.user.id">
          <el-button @click="router.push('/login')">登录</el-button>
          <el-button @click="router.push('/register')">注册</el-button>
        </div>
        <div v-else>
          <el-dropdown style="cursor: pointer">
            <div style="padding-right: 20px; display: flex; align-items: center">
              <img style="width: 40px; height: 40px; border-radius: 50%;" :src="data.user.avatar" alt="">
              <span style="margin-left: 5px;">{{ data.user.name }}</span><el-icon><arrow-down /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="jjj">修改密码（无效）</el-dropdown-item>
                <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </div>
    <div class="main-body">
      <RouterView @updateUser="updateUser" />
    </div>
  </div>
</template>

<script setup>
import router from "@/router/index.js";
import { computed } from 'vue';
import { useRoute } from 'vue-router';
import {ArrowDown} from "@element-plus/icons-vue";
import { reactive} from "vue";
import request from "@/utils/request.js";

// 使用 useRoute 钩子获取当前路由
const route = useRoute();

// 计算属性，根据当前路由的路径设置 activeMenuIndex
const activeMenuIndex = computed(() => {
  return route.path;
});

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  top: '暂无公告', // 初始化
  noticeData: []
})

const logout = () => {
  localStorage.removeItem('xm-user')  //清除缓存
  router.push('/login')
}

const updateUser = () => {
  data.user = JSON.parse(localStorage.getItem('xm-user') || '{}')
}

const loadNotice = () => {
  request.get('/notice/selectAll').then(res => {
    data.noticeData = res.data
    let i = 0
    if (data.noticeData && data.noticeData.length) {
      data.top = data.noticeData[0].content
      setInterval(() => {
        data.top = data.noticeData[i].content
        i = (i + 1) % data.noticeData.length; // 使用模运算来循环索引
        // i++
        // if (i === data.noticeData.length) {
        //   i = 0
        // }
      },2500)
    }
  })
}
loadNotice()
</script >

<style scoped>
@import "@/assets/css/front.css";
</style>