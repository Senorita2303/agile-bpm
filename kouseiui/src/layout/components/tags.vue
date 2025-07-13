
<template>
	<div class="adminui-tags">
		<ul ref="tagsf">
			<li v-for="tag in tagList" :key="tag.name"
				:class="[isActive(tag) ? 'active' : '', tag.meta.affix ? 'affix' : '']"
				@contextmenu.prevent="openContextMenu($event, tag)">
				<a @click="handlerClick(tag)">
					<span>{{ tag.meta.title }}</span>
					<el-icon v-if="!tag.meta.affix" @click.prevent.stop='closeSelectedTag(tag)'>
						<Close />
					</el-icon>
				</a>
			</li>
		</ul>
	</div>

	<transition name="el-zoom-in-top">
		<ul v-if="contextMenuVisible" :style="{ left: left + 'px', top: top + 'px' }" class="contextmenu"
			id="contextmenu">
			<li @click="refreshTab()">
				<el-icon>
					<Refresh />
				</el-icon>Refresh
			</li>
			<hr>
			<li @click="closeTabs()" :class="contextMenuItem?.meta.affix ? 'disabled' : ''">
				<el-icon>
					<Close />
				</el-icon>Close tag
			</li>
			<li @click="closeOtherTabs()">
				<el-icon>
					<FolderDelete />
				</el-icon>Close other tabs
			</li>
			<hr>
			<li @click="maximize()">
				<el-icon>
					<FullScreen />
				</el-icon>Maximize
			</li>
			<li @click="openWindow()">
				<el-icon>
					<CopyDocument />
				</el-icon>Open in new window
			</li>
		</ul>
	</transition>
</template>

<script lang="ts" setup>
import { useDefaultConfig } from '@/config/defaultConfig';
import { IRouteMenu } from '@/models/IRouterMenu';
import { IRouterModel } from '@/router/models/routeModel';
import { useIframeStore } from '@/store/modules/iframeStore';
import { useRouterMenuStore } from '@/store/modules/routerMenusStore';
import { ElLoading, ElMessage } from 'element-plus';
import { computed, nextTick, onMounted, ref, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { resolveTag } from "@/utils/index";
onMounted(() => {
	// var menu = tool.data.get('MENU')
	// var dashboardRoute = treeFind(menu, (node: IRouterModel) => node.path == config.DASHBOARD_URL)
	// if (dashboardRoute) {
	// 	dashboardRoute.fullPath = dashboardRoute.path
	// 	addViewTags(dashboardRoute)
	// 	const model: IRouterModel = {
	// 		name: route.name?.toString() as string,
	// 		path: route.path as string,
	// 		meta: route.meta
	// 	}
	// 	addViewTags(model)
	// 	scrollInit()
	//}
})
const iframeStore = useIframeStore()
const routerMenusStore = useRouterMenuStore()
const contextMenuVisible = ref<boolean>(false)
const contextMenuItem = ref<IRouteMenu>()
const left = ref<number>(0)
const top = ref<number>(0)
const tagList = computed(() => routerMenusStore.tabList)
console.log('tagList', tagList.value)
const tipDisplayed = ref<boolean>(false)
const route = useRoute()
const tagsf = ref<HTMLInputElement | null>(null)
const config = useDefaultConfig()
const router = useRouter()
// watch(route, (val, old) => {
// 	const model: IRouterModel = {
// 		name: val.name?.toString() as string,
// 		path: val.path as string,
// 		meta: val.meta
// 	}
// 	addViewTags(model)
// 	nextTick(() => {
// 		const tags = tagsf.value
// 		if (tags && tags.scrollWidth > tags.clientWidth) {
// 			// Make sure the current tab is in the visible range
// 			let targetTag = tags.querySelector(".active") as Element
// 			targetTag.scrollIntoView()
// 			// Show hints
// 			if (!tipDisplayed.value) {
// 				ElMessage({
// 					type: 'warning',
// 					center: true,
// 					message: 'There are too many tabs at the moment. You can use the mouse wheel to scroll the tab bar. Closing the tabs can reduce system performance consumption.',
// 				})
// 			}

// 		}
// 	})
// })
watch(contextMenuVisible, (val, old) => {
	var cm = function (e: any) {
		let sp = document.getElementById("contextmenu");
		if (sp && !sp.contains(e.target)) {
			closeMenu()
		}
	}
	if (val) {
		document.body.addEventListener('click', e => cm(e))
	} else {
		document.body.removeEventListener('click', e => cm(e))
	}
})
const closeMenu = () => {
	//contextMenuItem.value = null;
	contextMenuVisible.value = false
}
const treeFind = (tree: any, func: Function): any => {
	// for (const data of tree) {
	// 	if (func(data)) return data
	// 	if (data.children) {
	// 		const res = treeFind(data.children, func)
	// 		if (res) return res
	// 	}
	// }
	return null
}
const isActive = (item: IRouteMenu): boolean => {
	return item.path === route.path
}
const handlerClick = (val: IRouteMenu) => {
	// Changed to watch and processed
	const menus = routerMenusStore.platMenus
	/* let data = resolveTag(menus, val)
	routerMenusStore.clearTopMenus()
	data.forEach(key => {
		routerMenusStore.setTopMenus(key)
	}) */
	routerMenusStore.setActivePath(val.path)
	//routerMenusStore.setPMenu(data[0])
	router.push({ path: val.path, replace: true })
}
const closeSelectedTag = (tag: IRouterModel, autoPushLatestView = true) => {
	routerMenusStore.removeTab(tag.path)
	let count = tagList.value.length
	let index = count - 1
	let item = tagList.value[index]
	contextMenuVisible.value = false
	handlerClick(item)
}
const openContextMenu = (e: any, tag: IRouteMenu) => {
	contextMenuItem.value = tag;
	contextMenuVisible.value = true;
	left.value = e.clientX + 1;
	top.value = e.clientY + 1;
	//FIX Right-click menu margin position processing
	nextTick(() => {
		let sp = document.getElementById("contextmenu") as HTMLElement;
		if (document.body.offsetWidth - e.clientX < sp.offsetWidth) {
			left.value = document.body.offsetWidth - sp.offsetWidth + 1;
			top.value = e.clientY + 1;
		}
	})
}
const refreshTab = () => {
	var nowTag = contextMenuItem.value as IRouteMenu
	contextMenuVisible.value = false
	// Determine whether it is the current route, if not, jump to it.
	if (route.fullPath != nowTag?.path) {
		router.push({
			path: nowTag?.path as string
		})
	}
	const loading = ElLoading.service({ text: 'Refreshing....' })
	routerMenusStore.setReload(false)
	setTimeout(() => {
		loading.close()
		nextTick(() => {
			routerMenusStore.setReload(true)
		})
	}, 500)
}
const closeTabs = () => {
	var nowTag = contextMenuItem.value as IRouteMenu;
	closeSelectedTag(nowTag)
	contextMenuVisible.value = false
}
const closeOtherTabs = () => {
	var nowTag = contextMenuItem.value as IRouteMenu;
	const list = tagList.value
	list.forEach(item => {
		if (!item.meta.affix && item.path !== nowTag.path)
			routerMenusStore.removeTab(item.path)
	})
	contextMenuVisible.value = false
	if (route.path !== nowTag.path) {
		router.push({ path: nowTag.path, replace: true })
	}
}
// TAB maximized
const maximize = () => {
	var nowTag = contextMenuItem.value as IRouteMenu;
	contextMenuVisible.value = false
	// Determine whether it is the current route, if not, jump to it.
	if (route.fullPath != nowTag.path) {
		router.push({
			path: nowTag.path,
		})
	}
	document.getElementById('app')?.classList.add('main-maximize')
}
const openWindow = () => {
	var nowTag = contextMenuItem.value as IRouteMenu;
	if (nowTag.meta.type !== 'iframe') {
	   ElMessage.warning('Non-iframe pages or link addresses cannot open new windows')
	   return 
	}
	window.open(nowTag.path);
		contextMenuVisible.value = false
}
const scrollInit = () => {
	const scrollDiv = tagsf.value
	scrollDiv?.addEventListener('mousewheel', handler, false) || scrollDiv?.addEventListener("DOMMouseScroll", handler, false)
	function handler(event: any) {
		const detail = event.wheelDelta || event.detail;
		// Firefox scroll up key value -3 scroll down key value 3, other kernel scroll up key value 120 scroll down key value -120
		const moveForwardStep = 1;
		const moveBackStep = -1;
		let step = 0;
		if (detail == 3 || detail < 0 && detail != -3) {
			step = moveForwardStep * 50;
		} else {
			step = moveBackStep * 50;
		}
		scrollDiv!.scrollLeft += step;
	}
}
</script>

<style>
.contextmenu {
	position: fixed;
	width: 200px;
	margin: 0;
	border-radius: 0px;
	background: var(--el-bg-color-overlay);
	border: 1px solid var(--el-border-color-light);
	box-shadow: 0 2px 12px 0 rgba(0, 0, 0, .1);
	z-index: 3000;
	list-style-type: none;
	padding: 10px 0;
}

.contextmenu hr {
	margin: 5px 0;
	border: none;
	height: 1px;
	font-size: 0px;
	background-color: var(--el-border-color-light);
}

.contextmenu li {
	display: flex;
	align-items: center;
	margin: 0;
	cursor: pointer;
	line-height: 30px;
	padding: 0 17px;
	color: #606266;
}

.contextmenu li i {
	font-size: 14px;
	margin-right: 10px;
}

.contextmenu li:hover {
	background-color: #ecf5ff;
	color: #66b1ff;
}

.contextmenu li.disabled {
	cursor: not-allowed;
	color: #bbb;
	background: transparent;
}

.tags-tip {
	padding: 5px;
}

.tags-tip p {
	margin-bottom: 10px;
}

.dark .contextmenu li {
	color: var(--el-text-color-primary);
}
</style> -->
