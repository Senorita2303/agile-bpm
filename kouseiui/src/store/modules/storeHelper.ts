import pinia from '@/store/index'
import {useGlobarStore} from "./globalStore"
import { useIframeStore } from "./iframeStore"
import { useMainStore } from "./mainStore"
import { useRouterMenuStore } from "./routerMenusStore"
import { useUserStore } from "./userStore"
const glbarStore = useGlobarStore(pinia)
const iframStore = useIframeStore(pinia)
const mainStore = useMainStore(pinia)
const routeStore = useRouterMenuStore(pinia)
const userStore  = useUserStore(pinia)
export const useHelper={
    resetStore() {
       glbarStore.$reset()
       iframStore.$reset()
       mainStore.$reset()
       routeStore.$reset()
       userStore.$reset()
    }
}