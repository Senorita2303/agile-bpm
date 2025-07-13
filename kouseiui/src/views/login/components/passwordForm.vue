<template>
  <el-form
    ref="loginForm"
    :model="form"
    :rules="rules"
    label-width="0"
    size="large"
  >
    <el-form-item prop="user">
      <el-input
        v-model="form.user"
        prefix-icon="el-icon-user"
        clearable
        :placeholder="$t('login.userPlaceholder')"
      ></el-input>
    </el-form-item>
    <el-form-item prop="password">
      <el-input
        v-model="form.password"
        prefix-icon="el-icon-lock"
        clearable
        show-password
        :placeholder="$t('login.PWPlaceholder')"
      ></el-input>
    </el-form-item>
    <!-- <el-form-item style="margin-bottom: 10px;">
      <el-col :span="12">
        <el-checkbox :label="$t('login.rememberMe')" v-model="form.autologin"></el-checkbox>
      </el-col> -->
    <!-- <el-col :span="12" class="login-forgot">
        <router-link to="/reset_password">{{ $t('login.forgetPassword') }}？</router-link>
      </el-col> 
    </el-form-item>-->
    <el-form-item>
      <el-button
        type="primary"
        style="width: 100%"
        :loading="islogin"
        round
        @click="login"
      >
        {{ $t('login.signIn') }}
      </el-button>
    </el-form-item>
    <!-- <div class="login-reg">
      {{ $t('login.noAccount') }}
      <router-link to="/user_register">{{ $t('login.createAccount') }}</router-link>
    </div> -->
  </el-form>
</template>

<script setup lang="ts">
  import { defineComponent, reactive, ref } from 'vue'
  import i18n from '@/locales'
  import { ElMessage, FormInstance } from 'element-plus'
  import { useRouter } from 'vue-router'
  import { useMainStore } from '@/store/modules/mainStore'
  import { useUserStore } from '@/store/modules/userStore'
  import { useRouterMenuStore } from '@/store/modules/routerMenusStore'
  import { IUserInfo } from '@/store/models/userInfoModel'
  import { registerApi } from 'agilebpm'
  import { IRouteMenu } from '@/models/IRouterMenu'
  const routeStore = useRouterMenuStore()
  defineComponent({ name: 'passwordForm' })
  const router = useRouter()
  const loginForm = ref<FormInstance>()
  const userType = ref<string>('admin')
  const mainStore = useMainStore()
  const useStore = useUserStore()
  const form = reactive({
    user: 'admin',
    password: '1',
    autologin: false,
  })
  const rules = reactive({
    user: [
      {
        required: true,
        message: i18n.global.t('login.userError'),
        trigger: 'blur',
      },
    ],
    password: [
      {
        required: true,
        message: i18n.global.t('login.PWError'),
        trigger: 'blur',
      },
    ],
  })
  const islogin = ref<boolean>(false)
  const login = async () => {
    await loginForm.value?.validate((valid, fields) => {
      if (!valid) {
        return false
      }
    })
    islogin.value = true
    // Set token

    registerApi
      .login({
        userName: form.user,
        password: form.password,
        captcha: '',
        clientId: 'bpmDevPlatform',
        clientSecret: '1',
        grantType: 'password',
      })
      .then(
        async (res: any) => {
          localStorage.setItem('ab-token', res.data.access_token)
          let nowDate = new Date()
          let token: string = 'TS-' + nowDate.getTime()
          let createtime = nowDate.getTime()
          mainStore.setToken({ access_token: token, create_time: createtime })
          const { data } = await registerApi.getUserInfo()

          const menuList: any = []
          handleMenus(menuList, data.userMenuList, '', '')
          routeStore.setMenus(menuList)

          // Set platform menu
          let menu = [...menuList]
          var menuRouter = filterAsyncRouter(menu)
          menuRouter = flatAsyncRoutes(menuRouter)
          routeStore.setPlatMenus(menuRouter)
          console.info(menu)
          const user: IUserInfo = {
            userName: data.user.fullName,
            userNameF: data.user.fullName,
          }
          useStore.setUsers(user)

          useStore.setAbUsers(data.user)
          useStore.setCurrentOrg(data.currentOrg)
          useStore.setButtonPermission(data.buttonPermission)

          router.push({ path: '/' })
          ElMessage.success('Login successful')
          islogin.value = false
        },
        (res: any) => {
          islogin.value = false
          ElMessage.error(res.message)
        }
      )

    const handleMenus = (
      menuList: any,
      resoursList: any,
      topPath: string,
      parentPath: string
    ) => {
      let returnChildPathList: any[] = []
      resoursList.forEach(
        (item: {
          children: any[]
          name: string
          url: string
          code: string
          icon: string
          enable: number
          opened: number
        }) => {
          let routerMenu
          try {
            routerMenu = JSON.parse(
              `{"path":"${item.url || item.code}","name":"${
                item.code
              }","meta":{"title":"${item.name}","icon":"${
                item.icon
              }","hidden":${item.enable === 0}}}`
            )
          } catch (error) {
            console.error(item)
            return
          }

          const topMenuCodeStr = topPath || routerMenu.name
          routerMenu.meta.topMenuCode = topMenuCodeStr
          routerMenu.meta.parentPath = parentPath

          routerMenu.isopen = item.opened == 1

          // The existence of child means there is a subset, and the subset is processed here
          if (item.children && item.children.length > 0) {
            const children: never[] = []
            const childPathList = handleMenus(
              children,
              item.children,
              topMenuCodeStr,
              routerMenu.path || routerMenu.name
            )
            routerMenu.children = children
            routerMenu.childrenPathList = childPathList
          } else {
            // The last level defaults to giving him a childPathList
            routerMenu.childrenPathList = [routerMenu.path]
          }
          // Set childrenPathList
          returnChildPathList = returnChildPathList.concat(
            routerMenu.childrenPathList
          )
          menuList.push(routerMenu)
        }
      )
      return returnChildPathList
    }

    // let nowDate = new Date()
    // let token:string = "TS-"+nowDate.getTime()
    // let createtime = nowDate.getTime()
    // mainStore.setToken({access_token: token, create_time :createtime})
    // Get Menu
  }

  function flatAsyncRoutes(routes: Array<IRouteMenu>) {
    let res: Array<IRouteMenu> = []
    routes.forEach((route: IRouteMenu) => {
      if (route.children) {
        res.push(route)
        res.push(...flatAsyncRoutes(route.children))
      } else {
        res.push(route)
      }
    })
    return res
  }

  function filterAsyncRouter(routerMap: Array<IRouteMenu>) {
    const accessedRouters: any = []
    routerMap.forEach((item: any) => {
      item.meta = item.meta ? item.meta : {}
      // Handle special routing for external links
      if (item.meta.type == 'iframe') {
        item.meta.url = item.path
        item.path = `/i/${item.name}`
      }
      // MAP to routing object
      var route = {
        path: item.path,
        name: item.name,
        meta: item.meta,
        redirect: item.redirect,
        children: item.children ? filterAsyncRouter(item.children) : [],
        component: loadComponent(item.component),
      }
      accessedRouters.push(route)
    })
    return accessedRouters
  }

  const modules = import.meta.glob('../views/**/*.vue')

  function loadComponent(component: string) {
    if (component) {
      let newComponent = modules[component]
      return newComponent
    } else {
      return () => import(`@/layout/other/empty.vue`)
    }
  }
</script>

<style></style>
