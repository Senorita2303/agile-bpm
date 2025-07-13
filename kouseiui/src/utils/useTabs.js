import { nextTick } from 'vue'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import router from '@/router'
import store from '@/store'

export default {
	// Refresh tags
	refresh() {
		NProgress.start()
		const route = router.currentRoute.value
		store.commit("removeKeepLive", route.name)
		store.commit("setRouteShow", false)
		nextTick(() => {
			store.commit("pushKeepLive", route.name)
			store.commit("setRouteShow", true)
			NProgress.done()
		})
	},
	// Close tab
	close(tag) {
		const route = tag || router.currentRoute.value
		store.commit("removeViewTags", route)
		store.commit("removeIframeList", route)
		store.commit("removeKeepLive", route.name)
		const tagList = store.state.viewTags.viewTags
		const latestView = tagList.slice(-1)[0]
		if (latestView) {
			router.push(latestView)
		} else {
			router.push('/')
		}
	},
	// Post-processing after closing the tab
	closeNext(next) {
		const route = router.currentRoute.value
		store.commit("removeViewTags", route)
		store.commit("removeIframeList", route)
		store.commit("removeKeepLive", route.name)
		if (next) {
			const tagList = store.state.viewTags.viewTags
			next(tagList)
		}
	},
	// Close other
	closeOther() {
		const route = router.currentRoute.value
		const tagList = [...store.state.viewTags.viewTags]
		tagList.forEach(tag => {
			if (tag.meta && tag.meta.affix || route.fullPath == tag.fullPath) {
				return true
			} else {
				this.close(tag)
			}
		})
	},
	// Set title
	setTitle(title) {
		store.commit("updateViewTagsTitle", title)
	}
}
