<template>
  <div class="comprehensive-table-container">
    <el-tag
      v-for="item in info.data.user"
      :key="item"
      closable
      @close="closeUser(item)"
    >
      {{ item['account'] }}
    </el-tag>
    <ab-cust-dialog
      v-model="info.data.user"
      dialog-key="postSelector"
      style="display: inline-block; margin-left: 10px"
      type="primary"
      :value-mapping="{ key: 'code', name: 'account' }"
      @ok="okBtn"
    >
      Select
    </ab-cust-dialog>
  </div>
</template>
<script lang="ts">
  export default { name: 'CustomDemo' }
</script>
<script lang="ts" setup>
  // Common variables
  const info: any = reactive({
    data: {
      user: [],
    },
  })
  const okBtn = (list: any) => {
    console.log(info.data.user)
  }

  const closeUser = (item: string) =>
    info.data.user.splice(info.data.user.indexOf(item), 1)
</script>
