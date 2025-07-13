
<template>
	<div v-show="$route.meta.type == 'iframe'" class="iframe-pages">
		<iframe v-for="item in iframeList" :key="item.meta.url" v-show="$route.meta.url==item.meta.url"
			:src="item.meta.url" frameborder='0'></iframe>
	</div>
</template>

<script lang="ts" setup>
import {useGlobarStore} from '@/store/modules/globalStore';
import { useIframeStore } from '@/store/modules/iframeStore';
import { computed, watch } from 'vue';
import { useRoute } from 'vue-router';
const globalStore = useGlobarStore()
const iframeStore = useIframeStore()
const iframeList = computed(() => iframeStore.iframeList)
const ismobile = computed(() => globalStore.ismobile)
const layoutTags = computed(() => globalStore.layoutTags)
const route = useRoute()

const push = (route: any) => {
	if (route.meta.type == 'iframe') {
		if (ismobile || !layoutTags) {
			iframeStore.setIframeList(route)

		} else {
			iframeStore.pushIframeList(route)

		}
	} else {
		if (ismobile || layoutTags) {
			iframeStore.clearIframeList()

		}
	}
}
watch(() => route, (val, old) => {
	push(val)
})
</script>

<style scoped>
.iframe-pages {
	width: 100%;
	height: 100%;
	background: #fff;
}

iframe {
	border: 0;
	width: 100%;
	height: 100%;
	display: block;
}
</style>
 