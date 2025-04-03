<template>
  <el-container class="error-page">
    <el-header>
      <h1>404</h1>
      <h2>页面未找到</h2>
    </el-header>
    <el-main class="main-content">
      <p>很抱歉，您访问的页面不存在。</p>
      <el-button type="primary" @click="goHome">返回首页</el-button>
    </el-main>
  </el-container>
</template>

<script>
import { useRouter } from 'vue-router';
import { ElContainer, ElHeader, ElMain, ElButton } from 'element-plus';

export default {
  name: 'Error404',
  components: {
    ElContainer,
    ElHeader,
    ElMain,
    ElButton,
  },
  setup() {
    const router = useRouter();
    const goHome = () => {
      router.push('/');
    };
    return {
      goHome,
    };
  },
};
</script>

<style scoped>
.error-page {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
  text-align: center;
}

.error-page .el-header {
  margin-bottom: 20px;
}

.error-page h1 {
  font-size: 100px;
  margin: 0;
}

.error-page h2 {
  font-size: 24px;
  margin: 0;
}

.error-page p {
  font-size: 18px;
  color: #666;
}

.main-content {
  padding-top: 100px;
  text-align: center;
}
</style>