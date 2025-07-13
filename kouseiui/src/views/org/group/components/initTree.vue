<template>
  <div
    class="treeBoxHeight"
    style="margin: 20px"
    :style="{ height: state.height }"
  >
    <el-tree
      v-loading="state.loading"
      :data="state.treeData"
      :default-expand-all="true"
      empty-text="Loading..."
      :expand-on-click-node="false"
      :highlight-current="true"
      node-key="id"
      :operation="true"
      :props="state.defaultProps"
      @current-change="handleNodeClick"
    >
      <template #default="{ data }">
        <div
          class="custom-tree-node"
          @mouseenter="data.isshow = true"
          @mouseleave="data.isshow = false"
        >
          <p>
            <vab-icon :icon="data.icon" />
            {{ data.name }}
          </p>
          <div v-if="data.isshow" style="margin-left: -50px">
            <Plus
              v-ab-btn-rights:groupManager_add
              class="iconStyle"
              @click.stop="addClick(data)"
            />

            <Delete
              v-if="data.id !== '0'"
              v-ab-btn-rights:groupManager_del
              class="iconStyle"
              @click.stop="removeClick(data)"
            />

            <Refresh
              v-if="data.id !== '0'"
              v-ab-btn-rights:groupManager_refreshTree
              class="iconStyle"
              @click.stop="reloadClick(data)"
            />
          </div>
        </div>
      </template>
    </el-tree>
  </div>
</template>

<script setup lang="ts">
  import { ref, reactive, watch } from 'vue'
  import { Plus, Delete, Refresh } from '@element-plus/icons-vue'

  const props = defineProps({
    treeData: { required: true, type: Object },
  })
  watch(
    () => props.treeData,
    (v1, v2) => {
      state.treeData = v1
    },
    { deep: true }
  )
  const emit = defineEmits([
    'nodeClick',
    'addClick',
    'reloadClick',
    'removeClick',
  ])
  const state: any = reactive({
    loading: false,
    height: `${window.innerHeight - (110 + 32 + 20 + 60)}px`,
    treeData: props.treeData,
    id: null,
    defaultProps: { children: 'children', label: 'name' },
  })
  const handleNodeClick = (val: any) => {
    state.id = val.id
    emit('nodeClick', val)
  }
  const addClick = (val: any) => {
    emit('addClick', val)
  }

  const reloadClick = (val: any) => {
    emit('reloadClick', val)
  }
  const removeClick = (val: any) => {
    emit('removeClick', val)
  }
</script>
<style lang="scss" scoped>
  .custom-tree-node {
    display: flex;
    flex: 1;
    align-items: center;
    justify-content: space-between;
    padding-right: 8px;
    font-size: 14px;
  }

  .iconStyle {
    width: 1em;
    height: 1em;
    margin-right: 5px;
    color: #1890ff;
  }

  .treeBoxHeight {
    display: grid;
    overflow: auto;
  }
</style>
