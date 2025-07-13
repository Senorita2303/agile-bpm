/**
 * AgileBPM module depends on state management, and key interfaces need to be implemented by yourself
 */
// Introduce existing functions. If it is on your own platform, you can introduce your own or implement related interfaces
import { useRouterMenuStore } from './routerMenusStore'
import { useUserStore } from './userStore'
import { defineStore } from 'pinia'
import router from '@/router'

export const useAbStoreAdapter = defineStore('abStoreAdapter', {
  getters: {
    // Get button permissions, used for permission control instructions
    getButtonPermission: () => {
      return useUserStore().getButtonPermission
    },
    getUserMenuList: () => {
      // Used for shortcut menu, can be omitted
      return []
    },
    getAbUser: () => {
      return useUserStore().getAbUsers
    },
    getCurrentOrg: () => {
      return useUserStore().getCurrentOrg
    },
  },
  actions: {
    // Modify the name of the route
    changeTabsMeta: (info: TabEditInfo) => {
      const tabsStore = useRouterMenuStore()
      tabsStore.updateTabTitle(info.fullPath, info.meta.title)
    },
    // Close the specified TAB
    closeTab: (fullPath: string) => {
      const tabsStore = useRouterMenuStore()
      tabsStore.removeTab(fullPath)
    },
    // Handle exceptions within the platform
    abErrorHandler: async (data: any) => {
      if (data.code === 'token_invalid') {
        router.replace({ path: '/login' })
      }
    },
  },
})
