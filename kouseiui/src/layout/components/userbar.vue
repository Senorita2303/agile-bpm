<template>
  <div class="user-bar">
    <div class="screen panel-item hidden-sm-and-down" @click="screen">
      <el-icon>
        <FullScreen />
      </el-icon>
    </div>
    <div class="msg panel-item" @click="showMsg">
      <el-badge
        :hidden="msgList.length == 0"
        :value="msgList.length"
        class="badge"
        type="danger"
      >
        <el-icon><ChatDotRound /></el-icon>
      </el-badge>
      <el-drawer
        title="News"
        v-model="msg"
        :size="400"
        append-to-body
        destroy-on-close
      >
        <el-container>
          <el-main class="nopadding">
            <el-scrollbar>
              <ul class="msg-list">
                <li v-for="item in msgList" v-bind:key="item.id">
                  <a :href="item.link" target="_blank">
                    <div class="msg-list__icon">
                      <el-badge is-dot type="danger">
                        <el-avatar :size="40" :src="item.avatar"></el-avatar>
                      </el-badge>
                    </div>
                    <div class="msg-list__main">
                      <h2>{{ item.title }}</h2>
                      <p>{{ item.describe }}</p>
                    </div>
                    <div class="msg-list__time">
                      <p>{{ item.time }}</p>
                    </div>
                  </a>
                </li>
                <el-empty
                  v-if="msgList.length == 0"
                  description="No new news"
                  :image-size="100"
                ></el-empty>
              </ul>
            </el-scrollbar>
          </el-main>
          <el-footer>
            <el-button type="primary">Message Center</el-button>
            <el-button @click="markRead">Set all as read</el-button>
          </el-footer>
        </el-container>
      </el-drawer>
    </div>
    <el-dropdown class="user panel-item" trigger="click" @command="handleUser">
      <div class="user-avatar">
        <el-avatar :size="30">{{ userNameF }}</el-avatar>
        <label>{{ name }}</label>
        <el-icon class="el-icon--right">
          <ArrowDown />
        </el-icon>
      </div>
      <template #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item command="clearCache">Clear Cache</el-dropdown-item>
          <el-dropdown-item divided command="outLogin">
            Log Out
          </el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </div>
</template>
<script lang="ts" setup>
  import { computed, onMounted, ref } from 'vue'
  import { IMsgModel } from '@/models/IMsgModel'
  import { useUserStore } from '@/store/modules/userStore'
  import { useRouter } from 'vue-router'
  import { ElLoading, ElMessageBox } from 'element-plus'
  import { useGlobarStore } from '@/store/modules/globalStore'
  import screenfull from 'screenfull'
  import { IUserInfo } from '@/store/models/userInfoModel'
  const userName = ref<string>('')
  const userNameF = ref<string>('')
  const msg = ref<boolean>(false)
  const store = useUserStore()
  const globarStore = useGlobarStore()

  const name = computed(() => {
    return store.GetUsers?.userName
  })

  const msgList = ref<Array<IMsgModel>>([
    {
      id: 1,
      type: 'user',
      avatar: 'img/avatar.jpg',
      title: 'Based on SCUI',
      describe: 'If you like it, please give it a star to support it.',
      link: 'https://gitee.com/agile-bpm/',
      time: '5 minutes ago',
    },
    {
      id: 2,
      type: 'user',
      avatar: 'img/avatar2.gif',
      title: 'Welcome to AgileBPM',
      describe: 'Click on Gitee to get the latest open source version',
      link: 'https://gitee.com/agile-bpm/agile-bpm-basic',
      time: '10 minutes ago',
    },
    {
      id: 3,
      type: 'system',
      avatar: '/img/logo.png',
      title: 'Internal messaging and other functions are under development',
      describe: 'Vue 3.0 + TypeScript + ElementPlus + Axios Backend management system.',
      link: 'https://gitee.com/agile-bpm/',
      time: 'July 06, 2023',
    },
  ])
  const router = useRouter()
  onMounted(() => {
    const userInfo = store.GetUsers as IUserInfo
    userName.value = userInfo.userName
    userNameF.value = userInfo.userName.substring(0, 1)
  })
  const handleUser = (command: string) => {
    if (command == 'uc') {
      router.push({ path: '/usercenter' })
    }
    if (command == 'cmd') {
      router.push({ path: '/cmd' })
    }
    if (command == 'clearCache') {
      ElMessageBox.confirm(
        'Clearing the cache will reset the system to its initial state, including login status, theme, language settings, etc. Do you want to continue?',
        'Message',
        {
          type: 'info',
        }
      )
        .then(() => {
          const loading = ElLoading.service()
          globarStore.clearAll()
          router.replace({ path: '/login' })
          setTimeout(() => {
            loading.close()
            location.reload()
          }, 1000)
        })
        .catch(() => {
          // Cancel
        })
    }
    if (command == 'outLogin') {
      ElMessageBox.confirm('Are you sure you want to log out of the current user?', 'Message', {
        type: 'warning',
        confirmButtonText: 'Quit',
        confirmButtonClass: 'el-button--danger',
      })
        .then(() => {
          globarStore.clearAll()
          router.replace({ path: '/login' })
        })
        .catch(() => {
          // Cancel exit
        })
    }
  }
  const screen = () => {
    var element = document.documentElement
    if (screenfull.isEnabled) screenfull.toggle(element)
  }
  const showMsg = () => {
    msg.value = true
  }
  const markRead = () => {
    msgList.value = []
  }
</script>

<style scoped>
  .user-bar {
    display: flex;
    align-items: center;
    height: 100%;
  }

  .user-bar .panel-item {
    padding: 0 10px;
    cursor: pointer;
    height: 100%;
    display: flex;
    align-items: center;
  }

  .user-bar .panel-item i {
    font-size: 16px;
  }

  .user-bar .panel-item:hover {
    background: rgba(0, 0, 0, 0.1);
  }

  .user-bar .user-avatar {
    height: 49px;
    display: flex;
    align-items: center;
  }

  .user-bar .user-avatar label {
    display: inline-block;
    margin-left: 5px;
    font-size: 12px;
    cursor: pointer;
  }

  .msg-list li {
    border-top: 1px solid #eee;
  }

  .msg-list li a {
    display: flex;
    padding: 20px;
  }

  .msg-list li a:hover {
    background: #ecf5ff;
  }

  .msg-list__icon {
    width: 40px;
    margin-right: 15px;
  }

  .msg-list__main {
    flex: 1;
  }

  .msg-list__main h2 {
    font-size: 15px;
    font-weight: normal;
    color: #333;
  }

  .msg-list__main p {
    font-size: 12px;
    color: #999;
    line-height: 1.8;
    margin-top: 5px;
  }

  .msg-list__time {
    width: 100px;
    text-align: right;
    color: #999;
  }

  .dark .msg-list__main h2 {
    color: #d0d0d0;
  }

  .dark .msg-list li {
    border-top: 1px solid #363636;
  }

  .dark .msg-list li a:hover {
    background: #383838;
  }
</style>
