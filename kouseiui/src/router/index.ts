import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import pinia from '@/store'
import { useRouterMenuStore } from '@/store/modules/routerMenusStore'
import { useMainStore } from '@/store/modules/mainStore'

import { IRouteMenu } from '@/models/IRouterMenu'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import { ElNotification } from 'element-plus'
import { useDefaultConfig } from '@/config/defaultConfig'
import { checkTokenExp } from '@/utils'
import { useHelper } from '@/store/modules/storeHelper'
import layout from '@/layout/index.vue'
import login from '@/views/login/index.vue'
import home from '@/views/home/index.vue'
const config = useDefaultConfig()
// System special routing
const routes_404 = {
  path: '/:pathMatch(.*)*',
  hidden: true,
  component: () => import('@/layout/other/404.vue'),
}

import Org from './modules/org'
import BPM from './modules/bpm'
import Sys from './modules/sys'
import Cms from './modules/cms'
import Biz from './modules/biz'

const router = createRouter({
  history: createWebHistory(), //createWebHashHistory(),
  routes: [
    Cms,
    Org,
    BPM,
    Sys,
    Biz,
    {
      name: 'layout',
      path: '/',
      component: layout,
      redirect: config.DASHBOARD_URL || '/dashboard',
      children: [
        {
          name: 'dashboard',
          path: '/dashboard',
          meta: {
            title: 'Dashboard',
            icon: 'Menu',
            affix: true,
            type: 'menu',
          },
          component: home,
        },
      ],
    },
    {
      path: '/login',
      component: login,
      meta: {
        title: 'Login',
      },
    },
    {
      path: '/formDesigner',
      name: 'FormDesigner',
      component: () => import('@/views/form-designer/index.vue'),
      meta: {
        title: 'Designer',
        // I have not yet implemented route caching, and will work on it in the next version.
        noKeepAlive: true,
      },
    },
    {
      path: '/bpmDesigner',
      name: 'BpmDesigner',
      component: () => import('@/views/bpm/definition/bpmDesign.vue'),
      meta: {
        title: 'Process Designer',
        noKeepAlive: true,
      },
    },
    {
      path: '/bizForm/preview/:code',
      name: 'BizFormPreview',
      component: () => import('@/views/biz/bizForm/formPreview.vue'),
      meta: {
        title: 'Form Preview',
        icon: 'dashboard-line',
      },
    },
    {
      path: '/bpmInstancePrint',
      name: 'BpmInstancePrintHome',
      component: () => import('@/views/bpm/instance/bpmInstancePrint.vue'),
      meta: {
        title: 'Process Printing',
        noKeepAlive: false,
      },
    },
    {
      path: '/bizForm/bizDetail/:code',
      name: 'bizDetail',
      component: () => import('@/views/biz/bizForm/formPreview.vue'),
      meta: {
        title: 'Form Details',
        icon: 'dashboard-line',
        dynamicNewTab: true,
      },
    },
  ],
})
// Determine whether dynamic/static routing has been loaded
const defaultLogin = '/login'
router.beforeEach(async (to, from, next) => {
  NProgress.start()
  document.title = to.meta.title
    ? `${to.meta.title} - ${config.APP_NAME}`
    : `${config.APP_NAME}`
  if (checkTokenExp()) {
    localStorage.clear()
    useHelper.resetStore()
    if (to.path === defaultLogin) {
      next()
    } else {
      ElNotification.error({
        title: 'Authentication expiration reminder',
        message: 'The identity authentication time has expired and the system has automatically logged out. Please log in again!',
        type: 'error',
      })
      next(defaultLogin)
    }
  } else {
    const routeLength = router.getRoutes().length
    // Full page routing processing
    if (to.meta.fullpage) {
      to.matched = [to.matched[to.matched.length - 1]]
    }
    // Load dynamic/static routing
    if (routeLength === 3) {
      next({ path: config.DASHBOARD_URL, replace: true })
    } else {
      next()
    }
  }
})

router.afterEach((to, from) => {
  // afterEach(to)
  NProgress.done()
})

router.onError((error) => {
  NProgress.done()
  ElNotification.error({
    title: 'Routing Error',
    message: error.message,
  })
})

export default router
