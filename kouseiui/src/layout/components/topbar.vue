<template>
  <div class="adminui-topbar">
    <div class="left-panel">
      <el-breadcrumb :separator-icon="ArrowRight" class="hidden-sm-and-down">
        <transition-group name="breadcrumb" mode="out-in">
          <template v-for="item in breadList" :key="item.title">
            <el-breadcrumb-item v-if="item.path != '/'" :key="item.meta.title">
              <el-icon class="icon" v-if="item.meta.icon">
                <component :is="item.meta.icon" />
              </el-icon>
              {{ item.meta.title }}
            </el-breadcrumb-item>
          </template>
        </transition-group>
      </el-breadcrumb>
    </div>
    <div class="center-panel"></div>
    <div class="right-panel">
      <slot></slot>
    </div>
  </div>
</template>

<script lang="ts" setup>
  import { computed } from 'vue'
  import { useRouterMenuStore } from '@/store/modules/routerMenusStore'
  import { ArrowRight } from '@element-plus/icons-vue'

  const store = useRouterMenuStore()
  const breadList = computed(() => store.getTopMenus)
</script>

<style scoped>
  .el-breadcrumb {
    margin-left: 15px;
  }
  .el-breadcrumb .el-breadcrumb__inner .icon {
    font-size: 14px;
    margin-right: 5px;
    float: left;
  }
  .breadcrumb-enter-active,
  .breadcrumb-leave-active {
    transition: all 0.3s;
  }
  .breadcrumb-enter-from,
  .breadcrumb-leave-active {
    opacity: 0;
    transform: translateX(20px);
  }
  .breadcrumb-leave-active {
    position: absolute;
  }
</style>
