import { RouteRecordRaw } from 'vue-router'
import { useDefaultConfig } from '../config/defaultConfig'
import login from '@/views/login/index.vue'
import layout from '@/layout/index.vue'
const config = useDefaultConfig()
const routes: Array<RouteRecordRaw> = [

	{
		path: "/login",
		component: login,
		meta: {
			title: "Login"
		}
	}
]
export default routes
