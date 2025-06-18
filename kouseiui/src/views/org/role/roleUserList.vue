<template>
  <div class="comprehensive-table-container">
    <div ref="titleForm">
      <el-row class="vab-query-form">
        <h3>Role [{{ roleName }}] User List</h3>
        <el-divider />
      </el-row>
      <el-row class="vab-query-form">
        <el-col class="top-panel">
          <el-form
            ref="queryForm"
            :inline="true"
            label-width="90px"
            :model="query"
            @submit.prevent
          >
            <el-form-item
              label="Full Name"
              prop="['tuser.fullname$VLK']"
            >
              <el-input
                v-model="query['tuser.fullname$VLK']"
                placeholder="Please enter your name"
              />
            </el-form-item>
            <el-form-item
              label="Account"
              prop="['tuser.account$VLK']"
            >
              <el-input
                v-model="query['tuser.account$VLK']"
                placeholder="Please enter your account"
              />
            </el-form-item>
            <el-form-item>
              <el-button
                v-ab-btn-rights:orgRelation_roleJson
                :icon="Search"
                type="primary"
                @click="search()"
              >
                Search
              </el-button>
              <el-button
                v-ab-btn-rights:orgRelation_roleJson
                :icon="RefreshRight"
                @click="reset()"
              >
                Reset
              </el-button>
            </el-form-item>
          </el-form>
        </el-col>
        <el-col class="left-panel">
          <el-space>
            <el-button :icon="Back" type="primary" @click="back">
              Back
            </el-button>
            <ab-cust-dialog
              dialog-key="userSelector"
              :icon="Plus"
              type="primary"
              @ok="userSelectorOk($event, search)"
            >
              Add User
            </ab-cust-dialog>
            <el-button
              v-ab-btn-rights:orgRelation_batchDel
              :disabled="!selectedData || selectedData.length == 0"
              :icon="Delete"
              type="danger"
              @click="delBySeletedIds(orgApi.post.orgRelationRemove)"
            >
              Batch Delete
            </el-button>
          </el-space>
        </el-col>
      </el-row>
    </div>

    <ab-table
      ref="abTable"
      v-model="selectedData"
      :height="tableHeight"
      :query-param="query"
      row-key="id"
      :url="orgApi.post.roleUserList"
    >
      <ab-column label="Full Name" min-width="120" prop="userFullname" />
      <ab-column label="Account" min-width="120" prop="userAccount" />
      <ab-column label="Role Name" min-width="150" prop="roleName" />
      <ab-column ab-template="groupName" label="Affiliated Department" min-idth="130" />
      <template #groupName="{ scope }">
        {{ scope.row.groupName ?? '-' }}
      </template>
      <ab-column
        ab-date-formatter="yyyy-MM-dd HH:mm"
        label="Creation Time"
        prop="createTime"
        sortable
        width="170"
      />
      <ab-column ab-template="edit" fixed="right" label="Action" width="120" />
      <template #edit="{ scope }">
        <el-button
          v-ab-btn-rights:orgRelation_del
          text
          type="primary"
          @click="
            sendAction(
              orgApi.post.orgRelationRemove + scope.row.id,
              `Confirm to delete【${scope.row.userFullname}】?`
            )
          "
        >
          Delete
        </el-button>
      </template>
    </ab-table>
  </div>
</template>

<script lang="ts">
  import { abTableMix, orgApi, abUtil, abTools, getData } from 'agilebpm'
  export default { mixins: [abTableMix] }
</script>

<script lang="ts" setup>
  import { reactive } from 'vue'
  import { ElMessage } from 'element-plus'
  import {
    Search,
    Delete,
    Plus,
    RefreshRight,
    Back,
  } from '@element-plus/icons-vue'
  import { useAbStoreAdapter } from '@/store/modules/abStoreAdapter'

  const { closeTab } = useAbStoreAdapter()
  const route = useRoute()

  const { proxy } = abTools.useCurrentInstance()
  const name = 'RoleUserList'
  const roleId = proxy.$route.query.roleId
  const roleName = ref(proxy.$route.params.roleName)

  // If the role name does not exist, get it from the backend
  if (!roleName.value) {
    getData(orgApi.role.orgRoleGetUrl, { id: roleId }).then(
      (res) => (roleName.value = res.data.name)
    )
  }

  // Query condition definition, if ts needs to define all parameters, here override the parent class, if not, you don't need to set it, the parent class has already defined the query object
  const query: any = reactive({
    roleId: proxy.$route.query.roleId,
  })

  const userSelectorOk = (dataList: [any], searchFn: any) => {
    if (!dataList || !dataList.length) {
      return
    }
    orgApi.post
      .saveRoleUsers(roleId, dataList.map((item) => item.id) as [string])
      .then((res) => {
        ElMessage.success(res.data)
        searchFn()
      })
  }

  const back = () => {
    closeTab(route.path)
    proxy?.$router.push({
      name: 'RoleList',
    })
  }
</script>
