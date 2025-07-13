/**
 * Operation routing menu
 */
import { defineStore } from "pinia";
import { IRouterActions, IRouterMenuGetter, IRouterMenuState } from "../models/routerMenuModel";
import { useKeys } from "@/utils/useKeys";
import { IRouteMenu } from "@/models/IRouterMenu";
import { useDefaultConfig } from "@/config/defaultConfig";
import { useLocalStorage } from "@/utils";
const key = useKeys()
const config = useDefaultConfig()
export const useRouterMenuStore = defineStore<string, IRouterMenuState, IRouterMenuGetter, IRouterActions>('routeMenu', {
    state: () => {
        return {
            menus: [],
            activateMenu: null,
            tabList: [],
            platMenus: [],
            topMenus: [],
            activePath: config.DASHBOARD_URL,
            pMenu: null,
            reload: true,
            defaultOpenNames: []
        }
    },
    getters: {
        getMenus() {
            if (this.menus && this.menus.length > 0) {
                return this.menus
            } else {
                const menus = useLocalStorage.getSecretObject<IRouteMenu[]>(key.MENU_KEY)
                this.setOpenNamesByMenu(menus)
                return menus
            }
        },
        getActivate() {
            if (this.activateMenu) {
                return this.activateMenu
            } else {
                return useLocalStorage.getSecretObject<IRouteMenu>(key.ACTIVATE_MENU)
            }
        },
        getTabList() {
            return this.tabList
        },
        getTopMenus() {
            if (this.topMenus && this.topMenus.length > 0) {
                return this.topMenus
            } else {
                return useLocalStorage.getSecretObject("topMenus") as any
            }
        },
        getOpenNames() {
            return this.defaultOpenNames
        },
        getPlatMenus() {
            if (this.platMenus && this.platMenus.length > 0) {
                return this.platMenus
            } else {
                return useLocalStorage.getSecretObject<IRouteMenu[]>(key.FLAT_MENUS)
            }
        }
    },
    actions: {
        setMenus(menus) {
            this.menus = menus
            this.setOpenNamesByMenu(menus)
            useLocalStorage.setSecretObject<IRouteMenu[]>(key.MENU_KEY, menus)
        },
        clearMenus() {
            useLocalStorage.remove(key.MENU_KEY)
            this.menus = []
        },
        setActivate(menu) {
            useLocalStorage.setSecretObject<IRouteMenu>(key.ACTIVATE_MENU, menu)
            this.$state.activateMenu = menu
        },
        clearActivate() {
            useLocalStorage.remove(key.ACTIVATE_MENU)
            this.$state.activateMenu = null
        },
        setTabList(menu) {
            let item = this.$state.tabList.find(x => x.name === menu.name)
            if (!item)
                this.$state.tabList.push(menu)
        },
        setPlatMenus(menus) {
            this.$state.platMenus = menus
            useLocalStorage.setSecretObject<IRouteMenu[]>(key.FLAT_MENUS, menus)
        },
        clearPlatMenus() {
            this.$state.platMenus = []
            useLocalStorage.remove(key.FLAT_MENUS)
        },
        setTopMenus(menus) {
            const topMenus = this.getTopMenus || []
            let item = topMenus.find(x => x.name === menus.name)
            if (!item) {
                this.$state.topMenus.push(menus)
                useLocalStorage.setSecretObject("topMenus", topMenus)
            }
        },
        clearTopMenus() {
            this.$state.topMenus = []
        },
        setActivePath(path) {
            this.$state.activePath = path
        },
        removeTab(path) {
            let tabs = this.$state.tabList
            let index = tabs.findIndex(x => x.path === path)
            if (index >= 0)
                tabs.splice(index, 1)
        },
        updateTabTitle(fullpath: string, title: string) {
            let tabs = this.$state.tabList
            let item = tabs.find(x => x.path === fullpath)
            if (item) {
                item.meta.title = title
            }
        },
        setPMenu(menu) {
            this.$state.pMenu = menu
        },
        setReload(load) {
            this.$state.reload = load
        },
        setOpenNamesByMenu(routers: any) {
            routers.forEach((item: any) => {
                if (item.path && item.isopen) {
                    this.defaultOpenNames.push(item.path)
                }
                if (item.children) {
                    this.setOpenNamesByMenu(item.children)
                }
            })
        },
    },
})