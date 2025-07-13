<template>
  <el-dialog v-model="roleRes.visible" title="Role Resources" width="40%">
    <div class="card-header">
      <span>{{ appInfo.name }}</span>
      <ab-cust-dialog
        dialog-key="sysAppSelector"
        type="primary"
        @ok="confirmAppSelector"
      >
        Select Application
      </ab-cust-dialog>
    </div>
    <el-divider />
    <el-tree
      ref="resTreeRef"
      :check-on-click-node="true"
      :data="resTree.data"
      :default-expanded-keys="resTree.expandKeys"
      empty-text="Loading..."
      node-key="id"
      show-checkbox
      style="height: 500px; overflow: auto"
    >
      <template #default="{ data }">
        <span class="custom-tree-node">
          <span>
            <vab-icon v-if="data.icon" :icon="data.icon" />
            {{ data.name }}
          </span>
        </span>
      </template>
    </el-tree>
    <template #footer>
      <span class="dialog-footer">
        <el-space wrap>
          <el-button text type="primary" @click="roleRes.visible = false">
            Cancel
          </el-button>
          <el-button
            v-ab-btn-rights:resRole_grantRoleResource
            :loading="saveLoading"
            type="primary"
            @click="saveRoleRes"
          >
            Save
          </el-button>
        </el-space>
      </span>
    </template>
  </el-dialog>
</template>
<script type="ts" setup>
  import { defineEmits, reactive, ref, toRefs } from "vue";
  import { sysApi, authApi, abUtil,postData } from 'agilebpm'
  import { ElMessage } from "element-plus";

  const props = defineProps({
    roleRes: { required: true, type: Object },
    roleId: { required: true, type: String }
  });

  const { roleRes,roleId } = toRefs(props)

  const resTreeRef = ref()

  const resTree = reactive({
    data: [],
    setData(data) {
      resTree.data = data;
      // Select data item
      setTimeout(() => resTree.setCheckedKeys(), 0)
    }
    ,
    expandKeys: computed(() => {
      return abUtil.expandTwoLevels(resTree.data, 'id');
    }),
    setCheckedKeys() {
      if (resTree.data && resTree.data) {
        const items = [...resTree.data];
        while (items && items.length > 0) {
          const item = items.shift();
          if (item.checked) {
            resTreeRef.value.setChecked(item.id, true, false);
          }
          if (item.children) {
            items.push(...item.children);
          }
        }
      }
    }
  });

  const appInfo = reactive({
    id: "",
    name: "",
    code: ""
  });


  // Save progress bar
  const saveLoading = ref(false);

  /**
   * Refresh the character resource tree
   */
  const refreshRoleResTree = () => {
    // Get resource role tree
    authApi.getRoleResTreeData(roleId.value, appInfo.code)
      .then(res => {
        resTree.setData(res.data);
      });
  };

  watch(roleId, (newValue) => {
    resTree.setData([])
    if (newValue) {
      appInfo.code = 'bpmDevPlatform';
      // Get default application information
      postData(sysApi.authApplication.applicationListJson, { "queryParam": { "code$VEQ": appInfo.code } })
        .then(res => {
          const { id, name, code } = res.data.rows[0];
          appInfo.id = id;
          appInfo.name = name;
          appInfo.code = code;
        });
      refreshRoleResTree();
    }
  });

  /**
   * App selector confirmation
   * @param dataList Echo data list
   */
  const confirmAppSelector = (dataList) => {
    if (dataList) {
      const { id, code, name } = dataList[0];
      appInfo.id = id;
      appInfo.code = code;
      appInfo.name = name;
      refreshRoleResTree();
    }
  };

  /**
   * Save role resource
   */
  const saveRoleRes = () => {
    const resIds = resTreeRef.value.getCheckedKeys()
    const halfResIds = resTreeRef.value.getHalfCheckedKeys()
    authApi.grantRoleResource(roleId.value, appInfo.id, resIds, halfResIds).then(res => {
      ElMessage({
        showClose: true,
        message: "Save successfully",
        type: "success"
      });
      saveLoading.value = false;
      roleRes.value.visible = false
    });
  };
</script>
<style lang="scss" scoped>
  .card-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
</style>
