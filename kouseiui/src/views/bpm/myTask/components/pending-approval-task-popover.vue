<template>
  <el-popover
    placement="left"
    :width="600"
    trigger="click"
    v-if="data && data.length"
  >
    <template #reference>
      <el-link type="primary" :underline="true">
        {{ data[0].nodeName }}
      </el-link>
    </template>
    <el-table :data="data">
      <el-table-column prop="nodeName" label="Current session" />
      <el-table-column prop="assignNames" label="Candidate" />
    </el-table>
  </el-popover>
</template>
<script lang="ts">
  export default {
    name: 'PendingApprovalTaskPopover',
  }
</script>
<script setup lang="ts">
  const props = defineProps({
    // Receive data:
    data: { required: true, type: Array<any> },
  })

  const { data } = toRefs(props)
</script>
