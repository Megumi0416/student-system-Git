<template>
  <div class="container">
    <el-row :gutter="20">
      <!-- 用户列表列 -->
      <el-col :xs="24" :sm="8" :md="6" :lg="5" :xl="4">
        <el-card class="user-card">
          <!-- 搜索区域 -->
          <div class="search-wrapper">
            <el-input
                v-model="searchKeyword"
                placeholder="搜索用户"
                clearable
                @clear="handleSearch"
                @input="handleSearch"
            >
              <template #prefix>
                <el-icon class="search-icon"><Search /></el-icon>
              </template>
            </el-input>
          </div>

          <!-- 用户列表 -->
          <div class="card-section">
            <div class="section-title">全部用户（在线：{{ onlineCount }} / {{ allUsers.length }}）</div>
            <div class="user-list-container">
              <div
                  v-for="user in filteredUsers"
                  :key="user.username"
                  class="user-item"
                  :class="{ active: user.username === chatUser }"
                  @click="selectUser(user)"
              >
                <div class="user-info">
                  <div class="avatar-wrapper">
                    <el-avatar :size="36" :src="user.avatar || defaultAvatar" class="user-avatar"/>
                    <div class="status-indicator" :class="{ online: user.online }"></div>
                  </div>
                  <div class="user-meta">
                    <div class="username">{{ user.name || user.username }}
                      <span class="user-role" :class="getRoleClass(user.role)">{{ getRoleName(user.role) }}</span>
                    </div>
                    <div class="user-status">
                      <span class="status-text">{{ getUserStatus(user) }}</span>
                    </div>
                  </div>
                </div>
                <div class="chat-indicator">
                  <el-icon v-if="user.username === chatUser" class="chatting-icon">
                    <ChatDotRound />
                  </el-icon>
                </div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- 聊天主区域 -->
      <el-col :xs="24" :sm="16" :md="18" :lg="19" :xl="20">
        <div class="chat-container">
          <!-- 聊天头部 -->
          <div class="chat-header">
            <div class="header-content">
              <div class="title-wrapper">
                <span class="main-title">Web聊天室</span>
                <span class="sub-title">{{ currentChatUser?.name || currentChatUser?.username || '请选择聊天对象' }}</span>
              </div>
              <div class="online-status">
                <span class="status-dot" :class="{ online: currentChatUser?.online, offline: !currentChatUser?.online }"></span>
                <span v-if="currentChatUser" class="status-text">
                  {{ currentChatUser.online ? '在线' : '离线' }}
                </span>
              </div>
            </div>
          </div>

          <!-- 消息区域 -->
          <div class="message-area" ref="messageContainer">

            <div
                v-for="(message, index) in messages"
                :key="index"
                class="message-bubble"
                :class="{ 'self-message': message.from === user.username }"
            >
              <el-avatar
                  v-if="message.from !== user.username"
                  :size="40"
                  :src="message.fromAvatar || defaultAvatar"
                  class="message-avatar"
              />

              <div v-if="message.from !== user.username" class="message-content">
                <div class="message-meta">
                  <div class="meta-wrapper">
                    <span class="sender-name">{{ message.fromName }}</span>
                    <span class="message-time">{{ formatTime(message.timestamp) }}</span>
                  </div>
                </div>
                <div class="message-text">{{ message.text }}</div>
              </div>

              <div v-if="message.from === user.username" class="message-content">
                <div class="message-meta">
                  <div class="meta-wrapper">
                    <span class="sender-name">我</span>
                    <span class="message-time">{{ formatTime(message.timestamp) }}</span>
                  </div>
                </div>
                <div class="message-text">{{ message.text }}</div>
              </div>

              <el-avatar
                  v-if="message.from === user.username"
                  :size="40"
                  :src="user.avatar"
                  class="message-avatar self-avatar"
              />
            </div>
          </div>

          <!-- 输入区域 -->
          <div class="input-area">
            <div class="text-editor">
              <el-input
                  v-model="text"
                  type="textarea"
                  :rows="3"
                  placeholder="输入消息..."
                  resize="none"
                  @keyup.enter.native="send"
                  @focus="closeEmojiPicker"
              />
            </div>
            <div class="action-bar">
              <div class="action-buttons">
                <el-button
                    type="primary"
                    size="small"
                    circle
                    @click="toggleEmojiPicker"
                    class="emoji-button"
                >
                  <el-icon><Picture /></el-icon>
                </el-button>
                <el-button
                    type="primary"
                    size="small"
                    @click="send"
                    class="send-button"
                >
                  发送
                  <el-icon class="ml-2"><Promotion /></el-icon>
                </el-button>
              </div>
              <!-- 表情面板 -->
              <div v-show="showEmojiPicker" class="emoji-panel" ref="emojiPanel">
                <span
                    v-for="(emoji, index) in emojis"
                    :key="index"
                    class="emoji-item"
                    @click="insertEmoji(emoji)"
                >
                  {{ emoji }}
                </span>
              </div>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { ChatDotRound, Picture, Promotion, Search } from '@element-plus/icons-vue'
import request from "@/utils/request";

let socket;
export default {
  name: "Im",
  components: {
    ChatDotRound,
    Picture,
    Promotion,
    Search
  },
  data() {
    return {
      defaultAvatar: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
      user: {},
      users: [],
      filteredUsers: [],
      searchKeyword: '',
      chatUser: '',
      currentChatUser: null,
      chatName: '',
      text: "",
      messages: [],
      emojis: ['😊','😍','😎','😂','😢','😡','😱','😨','😵','😳','😤','😐','😶','😷','🤒','🤕','😴','💤','😈','👿','👹','👺','💩','👻','💀','☠️','👽','🤖','🎃','😺','😸','😹','😻','😼','😽','🙀','😿','😾'],
      showEmojiPicker: false,
      clickOutsideListener: null,
      allUsers: [],        // 所有用户数据（包含在线状态）
      onlineUsernames: [], // 当前在线用户的用户名数组
    }
  },
  computed: {
    filteredUsers() {
      const keyword = this.searchKeyword.toLowerCase()
      return this.allUsers.filter(user =>
          (user.name?.toLowerCase().includes(keyword) ||
              user.username?.toLowerCase().includes(keyword))
      )
    },
    onlineCount() {
      return this.onlineUsernames.length
    }
  },
  watch: {
    messages() {
      this.$nextTick(() => {
        this.scrollToBottom()
      })
    }
  },
  created() {
    this.init()
  },
  mounted() {
    // 添加全局点击监听
    this.clickOutsideListener = (e) => {
      const panel = this.$refs.emojiPanel
      if (this.showEmojiPicker && panel && !panel.contains(e.target)) {
        this.showEmojiPicker = false
      }
    }
    document.addEventListener('click', this.clickOutsideListener)
  },

  beforeUnmount() {
    // 移除监听
    if (this.clickOutsideListener) {
      document.removeEventListener('click', this.clickOutsideListener)
    }
  },
  methods: {
    // 初始化WebSocket连接
    async init() {
      this.user = JSON.parse(localStorage.getItem("xm-user") || '{}')
      if (!this.user.username) {
        this.$message.error('请先登录')
        return this.$router.push('/login')
      }

      const baseUrl = import.meta.env.VITE_BASE_URL?.replace('http', 'ws') || 'ws://localhost:8081'
      const socketUrl = `${baseUrl}/imserver/${this.user.username}`

      // 获取所有用户
      const res = await request.get('/api/all')
      if (res.code === '200') {
        this.allUsers = res.data
            .filter(u => u.username !== this.user.username) // 过滤自己
            .map(user => ({
              ...user,
              online: false
            }))
      }

      if (typeof WebSocket === 'undefined') {
        return this.$message.error('您的浏览器不支持WebSocket')
      }

      try {
        socket = new WebSocket(socketUrl)

        socket.onopen = () => {
          console.log("WebSocket连接已建立")
        }

        socket.onmessage = (msg) => {
          const data = JSON.parse(msg.data)
          if (data.users) {
            // 更新在线用户名单（过滤自己）
            this.onlineUsernames = data.users
                .filter(u => u.username !== this.user.username)
                .map(u => u.username)

            // 更新所有用户的在线状态
            this.allUsers = this.allUsers.map(user => ({
              ...user,
              online: this.onlineUsernames.includes(user.username)
            }))
          } else {
            this.handleIncomingMessage(data)
          }
        }

        socket.onclose = () => {
          console.log("WebSocket连接已关闭")
        }

        socket.onerror = (error) => {
          console.error("WebSocket错误:", error)
        }

      } catch (error) {
        console.error("连接失败:", error)
        this.$message.error('连接服务器失败')
      }
    },

    getRoleName(role) {
      const roleMap = {
        'STUDENT': '学生',
        'TEACHER': '教师',
        'ADMIN': '管理员'
      }
      return roleMap[role] || '未知角色'
    },
    // 获取角色对应的样式类
    getRoleClass(role) {
      return {
        'student': role === 'STUDENT',
        'teacher': role === 'TEACHER',
        'admin': role === 'ADMIN'
      }
    },

    // 处理接收消息
    handleIncomingMessage(message) {
      if (message.from === this.chatUser) {
        this.messages.push({
          ...message,
          timestamp: new Date().getTime()
        })
      }
    },

    // 根据用户名获取显示名称
    getSenderName(username) {
      const targetUser = this.users.find(u => u.username === username)
          || {username}; // 找不到用户时返回基础对象

      return targetUser.name || targetUser.username // 优先显示名称
    },

    // 获取发送者头像
    getSenderAvatar(username) {
      const targetUser = this.users.find(u => u.username === username)
      return targetUser?.avatar || this.defaultAvatar
    },

    // 发送消息
    send() {
      if (!this.validateMessage()) return

      const message = {
        from: this.user.username,
        to: this.chatUser,
        text: this.text.trim(),
        timestamp: new Date().getTime()
      }

      socket.send(JSON.stringify(message))
      this.messages.push({...message})
      this.text = ''
    },

    // 消息验证
    validateMessage() {
      if (!this.chatUser) {
        this.$message.warning('请先选择聊天对象')
        return false
      }
      if (!this.text.trim()) {
        this.$message.warning('消息内容不能为空')
        return false
      }
      return true
    },

    // 选择用户
    async selectUser(user) {
      // 清空当前消息
      this.messages = []

      // 更新当前聊天用户
      this.chatUser = user.username
      this.currentChatUser = user
      this.$message.success(`正在与 ${user.name || user.username} 聊天`)
      try {
        const res = await request.get('/message/list', {
          params: {
            from: this.user.username,
            to: user.username
          }
        });
        if (res.code === '200') {
          this.messages = res.data.map(msg => {
            // 判断消息方向
            const isSelf = msg.senderUsername === this.user.username;

            return {
              from: msg.senderUsername,  // 使用后端返回的发送者标识
              text: msg.text,
              timestamp: msg.timestamp,
              fromName: isSelf ? '我' : msg.senderName, // 动态显示称谓
              fromAvatar: isSelf ? this.user.avatar : msg.senderAvatar // 区分头像来源
            };
          });
          this.$nextTick(() => {
            this.scrollToBottom();
          });
        }
      } catch (error) {
        console.error('获取历史消息失败', error);
      }
    },

    // 获取用户状态
    getUserStatus(user) {
      return user.online ? '在线' : '离线'
    },

    // 处理搜索
    handleSearch() {
      const keyword = this.searchKeyword.toLowerCase()
      this.filteredUsers = this.users.filter(user =>
          user.name?.toLowerCase().includes(keyword) ||
          user.username?.toLowerCase().includes(keyword))
    },

    insertEmoji(emoji) {
      this.text += emoji
    },
    formatTime(timestamp) {
      const date = new Date(timestamp)
      const year = date.getFullYear()
      const month = (date.getMonth() + 1).toString().padStart(2, '0')
      const day = date.getDate().toString().padStart(2, '0')
      const hours = date.getHours().toString().padStart(2, '0')
      const minutes = date.getMinutes().toString().padStart(2, '0')
      return `${year}-${month}-${day} ${hours}:${minutes}`
    },
    scrollToBottom() {
      const container = this.$refs.messageContainer
      if (container) {
        container.scrollTop = container.scrollHeight
      }
    },
    closeEmojiPicker() {
      this.showEmojiPicker = false
    },
    toggleEmojiPicker(e) {
      e.stopPropagation()
      this.showEmojiPicker = !this.showEmojiPicker
    },
  }
}
</script>

<style scoped>
@import "@/assets/css/manger-message.css";
</style>