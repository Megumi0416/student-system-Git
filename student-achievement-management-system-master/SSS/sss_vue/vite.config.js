import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
  ],
  // 预加载项目必需的组件
  optimizeDeps: {
    include: [
      "vue",
      "vue-router",
      "axios",
      "element-plus/es/components/base/style/css",
      "element-plus/es/components/message/style/css",
      "element-plus/es/components/message-box/style/css",
      "element-plus/es/components/form/style/css",
      "element-plus/es/components/form-item/style/css",
      "element-plus/es/components/button/style/css",
      "element-plus/es/components/input/style/css",
      "element-plus/es/components/input-number/style/css",
      "element-plus/es/components/switch/style/css",
      "element-plus/es/components/upload/style/css",
      "element-plus/es/components/menu/style/css",
      "element-plus/es/components/col/style/css",
      "element-plus/es/components/icon/style/css",
      "element-plus/es/components/row/style/css",
      "element-plus/es/components/tag/style/css",
      "element-plus/es/components/dialog/style/css",
      "element-plus/es/components/loading/style/css",
      "element-plus/es/components/radio/style/css",
      "element-plus/es/components/radio-group/style/css",
      "element-plus/es/components/popover/style/css",
      "element-plus/es/components/scrollbar/style/css",
      "element-plus/es/components/alert/style/css",
      "element-plus/es/components/radio-button/style/css",
      "element-plus/es/components/checkbox-group/style/css",
      "element-plus/es/components/checkbox/style/css",
      "element-plus/es/components/tabs/style/css",
      "element-plus/es/components/tab-pane/style/css",
      "element-plus/es/components/rate/style/css",
      "element-plus/es/components/date-picker/style/css",
      "element-plus/es/components/notification/style/css",
      "element-plus/es/components/image/style/css",
      "element-plus/es/components/statistic/style/css",
      "element-plus/es/components/watermark/style/css",
      "element-plus/es/components/config-provider/style/css",
      "element-plus/es/components/text/style/css",
      "element-plus/es/components/drawer/style/css",
      "element-plus/es/components/color-picker/style/css",
    ],
  },
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8081', // 后端地址
        changeOrigin: true,
      }
    }
  }
})
