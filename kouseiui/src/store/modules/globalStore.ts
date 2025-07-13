/**
 * Operation global configuration
 */
import { defineStore } from "pinia"
import { defaultActions, IglobalGetter, IglobalState } from '../models/globalModel'
import { useDefaultConfig } from "../../config/defaultConfig"
import { useKeys } from '@/utils/useKeys'
import { useUserStore } from "./userStore"
import { useRouterMenuStore } from "./routerMenusStore"
import { useMainStore } from "./mainStore"
import { useChangeLang } from "@/hooks/langHook"
import { useLocalStorage } from "@/utils"
const config = useDefaultConfig()
const key = useKeys()
const useGlobarStore = defineStore<string, IglobalState, IglobalGetter, defaultActions>('global', {
    state: (): IglobalState => {
        return {
            ismobile: false,
            // Layout
            layout: config.LAYOUT,
            // Is the menu folded toggle
            menuIsCollapse: config.MENU_IS_COLLAPSE,
            // Multiple tab bar
            layoutTags: config.LAYOUT_TAGS,
            // Theme
            theme: config.THEME,
            lang: config.LANG,
            dark: false,
            color: config.COLOR
        }
    },
    getters: {
        GetDark: (state) => {
            let value = useLocalStorage.getSecretObject<string>(key.APP_DARK)
            if (value) {
                return (value === 'true' ? true : false)
            } else {
                return state.dark
            }
        },
        GetLang: (state) => {
            let value = useLocalStorage.getSecretObject<string>(key.APP_LANG)
            if (value) {
                return value
            } else {
                return state.lang
            }
        },
        GetTags: (state) => {
            let value = useLocalStorage.getSecretObject<string>(key.APP_TAG)
            if (value) {
                return (value === 'true' ? true : false)
            } else {
                return state.layoutTags
            }
        },
        GetColl(state) {
            let value = useLocalStorage.getSecretObject<string>(key.APP_COLL)
            if (value) {
                return (value === 'true' ? true : false)
            } else {
                return state.menuIsCollapse
            }
        },
        GetLayOut(state) {
            let value = useLocalStorage.getSecretObject<string>(key.APP_LAYOUT)
            if (value) {
                return value
            } else {
                return state.layout
            }
        },
        GetColor(state) {
            let value = useLocalStorage.getSecretObject<string>(key.APP_COLOR)
            if (value) {
                return value
            } else {
                return state.color
            }
        },
    },
    actions: {
        SET_ismobile(key) {
            this.$state.ismobile = key
        },
        setColor(color) {
            this.$state.color = color
            useLocalStorage.setSecretObject<string>(key.APP_COLOR, color)
        },
        setLayOut(layout) {
            this.$state.layout = layout
            useLocalStorage.setSecretObject<string>(key.APP_LAYOUT, layout)
        },
        setMenuCollapse(coll) {

            this.$state.menuIsCollapse = coll
            useLocalStorage.setSecretObject<string>(key.APP_COLL, String(coll))
        },
        setEnableTags(tag) {
            this.$state.layoutTags = tag
            useLocalStorage.setSecretObject<string>(key.APP_TAG, String(tag))
        },
        SETLang(lang) {
            this.$state.lang = lang
            useChangeLang(lang)
        },
        SETDark(dark) {
            this.$state.dark = dark
            useLocalStorage.setSecretObject<string>(key.APP_DARK, String(dark))
        },
        clearDark() {
            useLocalStorage.remove(key.APP_DARK)
            this.$state.dark = false
        },
        clearLang() {
            useLocalStorage.remove(key.APP_LANG)
        },
        clearAll() {
            const userStore = useUserStore()
            const routeStore = useRouterMenuStore()
            const mainStore = useMainStore()
            userStore.clearUsers()
            routeStore.clearActivate()
            routeStore.clearMenus()
            routeStore.clearTopMenus()
            routeStore.clearPlatMenus()
            mainStore.clearToken()
            this.clearLang()
            this.clearDark()
            localStorage.clear()
        }
    }
})
export { useGlobarStore }