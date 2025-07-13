<template>
	<div v-if="props.navMenus.length<=0" style="padding:20px;">
		<el-alert title="No subset menu" center type="info" :closable="false"></el-alert>
	</div>
	<template v-for="navMenu in navMenus" v-bind:key="navMenu">
		<el-menu-item v-if="!hasChildren(navMenu)" :index="navMenu.path">
			<a v-if="navMenu.meta&&navMenu.meta.type=='link'" :href="navMenu.path" target="_blank" @click.stop='()=>{}'></a>
			<el-icon v-if="navMenu.meta&&navMenu.meta.icon">
				<component :is="navMenu.meta.icon || 'el-icon-menu'"/></el-icon>
			<template #title>
				<span>{{navMenu.meta.title}}</span>
				<span v-if="navMenu.meta.tag" class="menu-tag">{{navMenu.meta.tag}}</span>
			</template>
		</el-menu-item>
		<el-sub-menu v-else :index="navMenu.path">
			<template #title>
				<el-icon v-if="navMenu.meta&&navMenu.meta.icon"><component :is="navMenu.meta.icon || 'el-icon-menu'"/></el-icon>
				<span>{{navMenu.meta.title}}</span>
				<span v-if="navMenu.meta.tag" class="menu-tag">{{navMenu.meta.tag}}</span>
			</template>
			<NavMenu :navMenus="navMenu.children"></NavMenu>
		</el-sub-menu>
	</template>
</template>

<script lang="ts" setup>
import { IRouteMenu } from '@/models/IRouterMenu';
import { defineComponent } from 'vue';
defineComponent({name:'NavMenu'})
const props = defineProps(['navMenus'])
const hasChildren =(item:IRouteMenu)=> item.children && !item.children.every(item => item.meta.hidden)
	
</script>
