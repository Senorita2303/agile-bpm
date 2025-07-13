<template>
  <el-config-provider :locale="locale" :size="config.size" :zIndex="config.zIndex" :button="config.button">
    <router-view/>
  </el-config-provider>
  <!-- <router-view /> -->
</template>

<script setup lang="ts">
import { useColor } from '@/utils/color'
import { computed, onMounted, reactive } from 'vue';
import i18n from '@/locales/index'
import {useGlobarStore} from './store/modules/globalStore';
const store = useGlobarStore()
const { lighten } = useColor()
onMounted(() => {
  const app_color = store.GetColor
  if (app_color) {
    document.documentElement.style.setProperty('--el-color-primary', app_color);
    for (let i = 1; i <= 9; i++) {
      document.documentElement.style.setProperty(`--el-color-primary-light-${i}`, lighten(app_color, i / 10));
    }
    for (let i = 1; i <= 9; i++) {
      document.documentElement.style.setProperty(`--el-color-primary-dark-${i}`, lighten(app_color, i / 10));
    }
  }

})
const config = reactive({
  size: "default",
  zIndex: 2000,
  button: {
    autoInsertSpace: false
  }
})
const locale = computed(() => i18n.global.messages[i18n.global.locale].el)
</script>

<style lang="scss" scoped>
</style>
