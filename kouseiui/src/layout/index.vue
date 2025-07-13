<template>
  <!-- Full-width layout -->
   <div v-if="isIframe">
    <router-view v-slot="{ Component }">
      <component :is="Component" v-if="routeStore.reload" />
    </router-view>
   </div>
   <div v-else>
  <template v-if="layout == 'header'">
    <header class="adminui-header">
      <div class="adminui-header-left">
        <div class="logo-bar">
          <img class="logo" src="/img/logo.png" />
          <span>{{ config.APP_NAME }}</span>
        </div>
        <ul v-if="!ismobile" class="nav">
          <li
            v-for="item in menu"
            :key="item.path"
            :class="pmenu?.path == item.path ? 'active' : ''"
            @click="showMenu(item)"
          >
            <el-icon>
              <component :is="item.meta.icon || 'el-icon-menu'" />
            </el-icon>
            <span>{{ item.meta.title }}</span>
          </li>
        </ul>
      </div>
      <div class="adminui-header-right">
        <userbar></userbar>
      </div>
    </header>
    <section class="aminui-wrapper">
      <div
        v-if="(!ismobile && nextMenu.length > 0) || !pmenu?.component"
        :class="menuIsCollapse ? 'aminui-side isCollapse' : 'aminui-side'"
      >
        <div v-if="!menuIsCollapse" class="adminui-side-top">
          <h2>{{ config.APP_NAME }}</h2>
        </div>
        <div class="adminui-side-scroll">
          <el-scrollbar>
            <el-menu
              :default-active="active"
              :default-openeds="defaultOpenNames"
              @select="menuSelect"
              :collapse="menuIsCollapse"
              :unique-opened="config.MENU_UNIQUE_OPENED"
            >
              <NavMenu :navMenus="nextMenu"></NavMenu>
            </el-menu>
          </el-scrollbar>
        </div>
        <div class="adminui-side-bottom" @click="handlerSetColl">
          <el-icon>
            <Expand v-if="menuIsCollapse" />
            <Fold v-else />
          </el-icon>
        </div>
      </div>
      <Side-m v-if="ismobile"></Side-m>
      <div class="aminui-body">
        <Topbar v-if="!ismobile"></Topbar>
        <Tags v-if="!ismobile && layoutTags"></Tags>
        <div class="adminui-main" id="adminui-main">
          <router-view v-slot="{ Component }">
            <component :is="Component" v-if="routeStore.reload" />
          </router-view>
          <iframe-view></iframe-view>
        </div>
      </div>
    </section>
  </template>
  <!-- Classic layout -->
  <template v-else-if="layout == 'menu'">
    <header class="adminui-header">
      <div class="adminui-header-left">
        <div class="logo-bar">
          <img class="logo" src="/img/logo.png" />
          <span>{{ config.APP_NAME }}</span>
        </div>
      </div>
      <div class="adminui-header-right">
        <userbar></userbar>
      </div>
    </header>
    <section class="aminui-wrapper">
      <div
        v-if="!ismobile"
        :class="menuIsCollapse ? 'aminui-side isCollapse' : 'aminui-side'"
      >
        <div class="adminui-side-scroll">
          <el-scrollbar>
            <el-menu
              :default-active="active"
              :default-openeds="defaultOpenNames"
              @select="menuSelect"
              :collapse="menuIsCollapse"
              :unique-opened="config.MENU_UNIQUE_OPENED"
            >
              <NavMenu :navMenus="menu"></NavMenu>
            </el-menu>
          </el-scrollbar>
        </div>
        <div class="adminui-side-bottom" @click="handlerSetColl">
          <el-icon>
            <Expand v-if="menuIsCollapse" />
            <Fold v-else />
          </el-icon>
        </div>
      </div>
      <Side-m v-if="ismobile"></Side-m>
      <div class="aminui-body">
        <Topbar v-if="!ismobile"></Topbar>
        <Tags v-if="!ismobile && layoutTags"></Tags>
        <div class="adminui-main" id="adminui-main">
          <router-view v-slot="{ Component }">
            <component :is="Component" v-if="routeStore.reload" />
          </router-view>
          <iframe-view></iframe-view>
        </div>
      </div>
    </section>
  </template>

  <!-- Function dock layout -->
  <template v-else-if="layout == 'dock'">
    <header class="adminui-header">
      <div class="adminui-header-left">
        <div class="logo-bar">
          <img class="logo" src="/img/logo.png" />
          <span>{{ config.APP_NAME }}</span>
        </div>
      </div>
      <div class="adminui-header-right">
        <div v-if="!ismobile" class="adminui-header-menu">
          <el-menu
            mode="horizontal"
            :default-openeds="defaultOpenNames"
            @select="menuSelect"
            :default-active="active"
            background-color="#222b45"
            text-color="#fff"
            active-text-color="var(--el-color-primary)"
          >
            <NavMenu :navMenus="menu"></NavMenu>
          </el-menu>
        </div>
        <Side-m v-if="ismobile"></Side-m>
        <userbar></userbar>
      </div>
    </header>
    <section class="aminui-wrapper">
      <div class="aminui-body">
        <Tags v-if="!ismobile && layoutTags"></Tags>
        <div class="adminui-main" id="adminui-main">
          <router-view v-slot="{ Component }">
            <component :is="Component" v-if="routeStore.reload" />
          </router-view>
          <iframe-view></iframe-view>
        </div>
      </div>
    </section>
  </template>

  <!-- Default layout -->
  <template v-else>
    <section class="aminui-wrapper">
      <div v-if="!ismobile" class="aminui-side-split">
        <div class="aminui-side-split-top">
          <router-link :to="config.DASHBOARD_URL">
            <img class="logo" :title="config.APP_NAME" src="/img/logo-r.png" />
          </router-link>
        </div>
        <div class="adminui-side-split-scroll">
          <el-scrollbar>
            <ul>
              <li
                v-for="item in menu"
                :key="item.path"
                :class="pmenu?.path == item.path ? 'active' : ''"
                @click="showMenu(item)"
              >
                <el-icon>
                  <component :is="item.meta.icon" />
                </el-icon>
                <p>{{ item.meta.title }}</p>
              </li>
            </ul>
          </el-scrollbar>
        </div>
      </div>
      <div
        v-if="(!ismobile && nextMenu.length > 0) || !pmenu?.component"
        :class="menuIsCollapse ? 'aminui-side isCollapse' : 'aminui-side'"
      >
        <div v-if="!menuIsCollapse" class="adminui-side-top">
          <h2>{{ config.APP_NAME }}</h2>
        </div>
        <div class="adminui-side-scroll">
          <el-scrollbar>
            <el-menu
              :default-active="active"
              :default-openeds="defaultOpenNames"
              @select="menuSelect"
              :collapse="menuIsCollapse"
              :unique-opened="config.MENU_UNIQUE_OPENED"
            >
              <NavMenu :navMenus="nextMenu"></NavMenu>
            </el-menu>
          </el-scrollbar>
        </div>
        <div class="adminui-side-bottom" @click="handlerSetColl">
          <el-icon>
            <Expand v-if="menuIsCollapse" />
            <Fold v-else />
          </el-icon>
        </div>
      </div>
      <Side-m v-if="ismobile"></Side-m>
      <div class="aminui-body">
        <Topbar>
          <userbar></userbar>
        </Topbar>
        <Tags v-if="!ismobile && layoutTags"></Tags>
        <div class="adminui-main" id="adminui-main">
          <router-view v-slot="{ Component }">
            <component
              :is="Component"
              :key="$route.fullPath"
              v-if="routeStore.reload"
            />
            <!-- <component :is="Component" :key="$route.fullPath" />
						 <keep-alive :include="keepStore.$state.keepLiveRoute as any">
							<component :is="Component" :key="$route.fullPath" v-if="keepStore.$state.keepAlive.routeShow" />
						</keep-alive>  -->
          </router-view>
          <iframe-view></iframe-view>
        </div>
      </div>
    </section>
  </template>
  <div class="layout-setting" @click="openSetting">
    <el-icon>
      <BrushFilled />
    </el-icon>
  </div>
  <el-drawer
    title="Layout Live Demo"
    v-model="settingDialog"
    :size="400"
    append-to-body
    destroy-on-close
  >
    <setting></setting>
  </el-drawer>
</div>
</template>

<script lang="ts" setup>
  import SideM from './components/sideM.vue'
  import Topbar from './components/topbar.vue'
  import Tags from './components/tags.vue'
  import NavMenu from './components/NavMenu.vue'
  import userbar from './components/userbar.vue'
  import setting from './components/setting.vue'
  import iframeView from './components/iframeView.vue'
  import {
    onMounted,
    ref,
    computed,
    nextTick,
    provide,
    inject,
    watch,
  } from 'vue'
  import { useGlobarStore } from '@/store/modules/globalStore'
  import { useDefaultConfig } from '@/config/defaultConfig'
  import {
    RouteLocationNormalizedLoaded,
    Router,
    useRoute,
    useRouter,
  } from 'vue-router'
  import { useRouterMenuStore } from '@/store/modules/routerMenusStore'
  import { IRouteMenu } from '@/models/IRouterMenu'
  import { storeToRefs } from 'pinia'
  import { resolveTag } from '@/utils'
  const store = useGlobarStore()
  const config = useDefaultConfig()
  const route = useRoute()
  const router = useRouter()
  const routeStore = useRouterMenuStore()
  const settingDialog = ref<boolean>(false)
  const menu = ref<Array<IRouteMenu>>([])
  const nextMenu = ref<Array<IRouteMenu>>([])
  const pmenu = computed<IRouteMenu>(() => {
    return routeStore.pMenu as IRouteMenu
  })
  const isIframe = computed(() => {
    if (route?.query?.abIframeType && route.query.abIframeType == 'iframe') {
      return true
    }
    return false
  })
  const defaultOpenNames = computed<IRouteMenu>(() => {
    return routeStore.getOpenNames
  })
  const { activePath: active } = storeToRefs(routeStore)
  onMounted(() => {
    onLayoutResize()
    menu.value = routeStore.getMenus!
  })
  const openSetting = () => {
    settingDialog.value = true
  }
  const onLayoutResize = () => {
    store.SET_ismobile(document.body.clientWidth < 992)
  }

  // Route monitoring TOP window settings
  const showThis = (route: RouteLocationNormalizedLoaded) => {
    const plats = routeStore.getPlatMenus
    if (!plats) return
    const item = plats.find((x: any) => x.path === route.fullPath)
    if (item) {
      const parent = item.meta.topMenuCode
      const menus = routeStore.getMenus!.find((x) => x.path === parent)!
      if (!menus || !menus.children) return
      nextMenu.value = menus.children
      routeStore.setPMenu(menus)
      routeStore.setActivePath(route.fullPath)
      // Set breadcrumbs
      const data = resolveTag(plats, item).reverse()
      routeStore.clearTopMenus()
      data.forEach((val) => {
        routeStore.setTopMenus(val)
      })
      nextTick(() => {
        //  routeStore.clearTopMenus();
        // routeStore.setTopMenus(menus);
      })
    }
  }

  watch(
    () => route.fullPath,
    () => {
      showThis(route)
    },
    {
      immediate: true,
    }
  )

  const menuSelect = (index: string) => {
    let menus = routeStore.platMenus
    let item = menus.find((x) => x.path === index)
    if (item) {
      if (layout.value === 'menu') {
        routeStore.setTabList(item)
      } else {
        routeStore.setTabList(item)
        routeStore.setActivePath(item.path)
      }
    }
    router.push({ path: index })
  }
  // Click to show
  const showMenu = (route: IRouteMenu) => {
    routeStore.setPMenu(route)
    nextMenu.value = filterUrl(route.children)
    routeStore.clearTopMenus()
    routeStore.setTopMenus(route)
    if ((!route.children || route.children.length == 0) && route.component) {
      router.push({ path: route.path })
      routeStore.setTabList(route)
    }
  }
  //Route to external links
  const filterUrl = (map: any) => {
    const newMap: any = []
    map &&
      map.forEach((item: any) => {
        item.meta = item.meta ? item.meta : {}
        // Handling hidden
        if (item.meta.hidden || item.meta.type == 'button') {
          return false
        }
        // Processing http
        if (item.meta.type == 'iframe') {
          item.path = `/i/${item.name}`
        }
        // Recursive loop
        if (item.children && item.children.length > 0) {
          item.children = filterUrl(item.children)
        }
        newMap.push(item)
      })
    return newMap
  }
  const ismobile = computed(() => store.$state.ismobile)
  const layout = computed(() => store.GetLayOut)
  const layoutTags = computed(() => store.GetTags)
  const menuIsCollapse = computed(() => store.menuIsCollapse)
  const handlerSetColl = () => {
    store.setMenuCollapse(!menuIsCollapse.value)
  }
</script>
