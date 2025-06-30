import { createApp } from 'vue'
import App from './App.vue'
import i18n from '@/locales/index'
import 'element-plus/dist/index.css'
import '@/style/style.scss'
import router from '@/router/index'
import { setupAbCore } from 'agilebpm'
// @ts-ignore
import { setupFormDesign } from 'abFormDesigner'
// @ts-ignore
import { setupLayoutComponents } from 'abLayoutDesigner'
import 'virtual:svg-icons-register'

import ElementPlus from 'element-plus'
import en from 'element-plus/es/locale/lang/en';
import pinia from './store'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
const app = createApp(App)
app.use(i18n)
app.use(pinia)
app.use(ElementPlus)
// app.use(ElementPlus, {
//   locale: en,
// })
app.use(router)
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

// Adapted to the agilebpm component state management, in order to decouple the component from the platform, but the component needs these methods
import { useAbStoreAdapter } from '@/store/modules/abStoreAdapter'
  ; (window as any).useAbStoreAdapter = useAbStoreAdapter

setupAbCore(app)
setupFormDesign(app)
setupLayoutComponents(app)

app.mount('#app')
