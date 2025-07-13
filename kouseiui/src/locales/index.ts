
import { useDefaultConfig } from "@/config/defaultConfig"
import { createI18n } from 'vue-i18n'
// @ts-ignore
// import el_zh_cn from 'element-plus/es/locale/lang/zh-cn'
// import el_zh_cn from 'element-plus/dist/locale/zh-cn'
// @ts-ignore
import el_en from 'element-plus/es/locale/lang/en'
// import el_en from 'element-plus/dist/locale/en'
import el_vi from 'element-plus/es/locale/lang/vi'

import en from './lang/en'
import vi from './lang/vi'
const config = useDefaultConfig()
const messages = {
	'vi': {
		el: el_vi,
		...vi
	},
	'en': {
		el: el_en,
		...en
	}
}

const i18n = createI18n({
	locale: config.LANG,
	fallbackLocale: 'en',
	globalInjection: true,
	allowComposition: true,
	messages,
})
export default i18n;
