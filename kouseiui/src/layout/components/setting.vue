<template>
	<el-form ref="form" label-width="130px" label-position="left" style="padding:0 20px;">
		<el-alert title="After completing the following configuration settings, you need to refresh the page to take effect!" type="error" :closable="false">
		</el-alert>
		<el-divider></el-divider>
		<el-form-item :label="$t('user.nightmode')">
			<el-switch v-model="dark"></el-switch>
		</el-form-item>
		<el-form-item :label="$t('user.language')">
			<el-select v-model="lang">
				<el-option label="Tiếng Việt" value="vi"></el-option>
				<el-option label="English" value="en"></el-option>
			</el-select>
		</el-form-item>
		<el-divider></el-divider>
		<el-form-item label="Theme Color">
			<el-color-picker v-model="colorPrimary" :predefine="colorList">></el-color-picker>
		</el-form-item>
		<el-divider></el-divider>
		<el-form-item label="Framework Layout">
			<el-select v-model="layout" placeholder="Please select">
				<el-option label="Default" value="default"></el-option>
				<el-option label="Full Column" value="header"></el-option>
				<el-option label="Classic" value="menu"></el-option>
				<el-option label="Function Dock" value="dock"></el-option>
			</el-select>
		</el-form-item>
		<el-form-item label="Collapse Menu">
			<el-switch v-model="menuIsCollapse"></el-switch>
		</el-form-item>
		<el-form-item label="Tab Bar">
			<el-switch v-model="layoutTags"></el-switch>
		</el-form-item>
		<el-divider></el-divider>
	</el-form>
</template>

<script lang="ts" setup>
import { useDefaultConfig } from '@/config/defaultConfig';
import {useGlobarStore} from '@/store/modules/globalStore';
import { useColor } from '@/utils/color'
import { ref, watch } from 'vue';
const store = useGlobarStore()
const config = useDefaultConfig()
const color = useColor()
const layout = ref<string>(store.GetLayOut)
watch(layout, (newvalue, oldvalue) => {
	store.setLayOut(newvalue)
})
const menuIsCollapse = ref<boolean>(store.GetColl)
watch( menuIsCollapse, (newvalue, oldvalue) => {
	store.setMenuCollapse(newvalue)
})
const layoutTags =ref<boolean>(store.GetTags)
watch(layoutTags, (newvalue, oldvalue) => {
	store.setEnableTags(newvalue)
})
const lang=ref<string>(store.GetLang)
	watch(lang, (newvalue, oldvalue) => {
	store.SETLang(newvalue)
})
const dark = ref<boolean>(store.GetDark)
watch(dark, (newvalue, oldvalue) => {
	store.SETDark(newvalue)
	if (newvalue) {
		document.documentElement.classList.add("dark")
		
	} else {
		document.documentElement.classList.remove("dark")	
	}
})
const colorList: Array<string> = ['#409EFF', '#009688', '#536dfe', '#ff5c93', '#c62f2f', '#fd726d']
const colorPrimary =ref<string>(store.GetColor) 
watch(colorPrimary, (newvalue, oldvalue) => {
	if(!newvalue)
	   newvalue ='#409EFF'
	document.documentElement.style.setProperty('--el-color-primary', newvalue);
	for (let i = 1; i <= 9; i++) {
		document.documentElement.style.setProperty(`--el-color-primary-light-${i}`, color.lighten(newvalue, i / 10));
	}
	for (let i = 1; i <= 9; i++) {
		document.documentElement.style.setProperty(`--el-color-primary-dark-${i}`, color.darken(newvalue, i / 10));
	}
	store.setColor(newvalue)
	
})
</script>

<style>

</style>
