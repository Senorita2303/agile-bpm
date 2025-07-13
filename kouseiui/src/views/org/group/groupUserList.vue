<template>
  <div ref="titleForm">
    <el-row class="vab-query-form">
      <el-col class="top-panel">
        <el-form
          ref="queryForm"
          v-ab-btn-rights:groupUser_search
          :inline="true"
          label-width="90px"
          :model="query"
        >
          <el-form-item label="Full Name" prop="fullname$VLK">
            <el-input
              v-model="query.fullname$VLK"
              clearable
              placeholder="Please enter your name"
              type="text"
            />
          </el-form-item>

          <el-form-item label="Account" prop="account$VLK">
            <el-input
              v-model="query.account$VLK"
              clearable
              placeholder="Please enter your account"
              type="text"
            />
          </el-form-item>
          <el-form-item>
            <el-button :icon="Search" type="primary" @click="search()">
              Search
            </el-button>
            <el-button :icon="RefreshRight" @click="reset()">Reset</el-button>
          </el-form-item>
        </el-form>
      </el-col>

      <el-col class="left-panel">
        <el-space>
          <el-button
            v-ab-btn-rights:orgRelation_addGroupUser
            :icon="Plus"
            type="primary"
            @click="info.showDialog = true"
          >
            Add new position user
          </el-button>
          <el-button
            v-ab-btn-rights:groupManager_batchDel
            :disabled="!selectedData || selectedData.length == 0"
            :icon="Delete"
            type="danger"
            @click="delBySeletedIds(orgApi.post.removeGroupUserUrl)"
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
    :url="orgApi.post.queryGroupUserUrl"
  >
    <ab-column label="Full Name" min-width="100" prop="userFullname" />

    <ab-column label="Account" min-width="100" prop="userAccount" />

    <ab-column label="Organization" min-width="120" prop="groupName" />

    <ab-column label="Position Title" min-width="130" prop="roleName" />

    <ab-column
      ab-tag-type="isMasterCss"
      label="Main Organization"
      min-width="150"
      prop="isMasterDesc"
    />

    <ab-column
      ab-tag-type="statusCss"
      label="Status"
      min-width="86"
      prop="statusDesc"
    />

    <ab-column
      date-format="yyyy-MM-dd"
      label="Creation Time"
      min-width="160"
      prop="createTime"
      sortable
    />

    <ab-column
      ab-template="edit"
      fixed="right"
      label="Manage"
      temp="action"
      width="350"
    />
    <template #edit="{ scope }">
      <el-button
        v-ab-btn-rights:orgRelation_updateStatus
        text
        type="primary"
        @click="
          sendAction(
            orgApi.post.updateGroupUserStatusUrl + scope.row.id,
            'Are you sure you want to ' +
              (scope.row.statusDesc === 'Enable' ? 'Disable ' : 'Enable ') +
              scope.row.userFullname +
              '?'
          )
        "
      >
        {{ scope.row.statusDesc === 'Enable' ? 'Disable' : 'Enable' }}
      </el-button>

      <el-button
        v-ab-btn-rights:orgRelation_setMaster
        text
        type="primary"
        @click="
          sendAction(
            orgApi.post.updateGroupUserMasterUrl + scope.row.id,
            'Are you sure you want to ' +
              (scope.row.isMasterDesc === 'Yes' ? 'Cancel main organization' : 'Set primary organization') +
              '?'
          )
        "
      >
        {{ scope.row.isMasterDesc === 'Yes' ? 'Cancel main organization' : 'Set primary organization' }}
      </el-button>
      <el-button
        v-ab-btn-rights:orgRelation_remove
        text
        type="primary"
        @click="
          sendAction(
            orgApi.post.removeGroupUserUrl + scope.row.id,
            `Confirm to delete ${scope.row.userFullname} ?`
          )
        "
      >
        Delete
      </el-button>
    </template>
  </ab-table>

  <addGroupUser
    :current-org="currentOrg"
    name="addGroupRoleUser"
    :show-dialog="info.showDialog"
    @close="closeSaveDialog"
    @reSearch="reSearchFn"
  />
</template>

<script lang="ts">
  import { reactive, defineComponent } from 'vue'
  import { abTableMix, orgApi } from 'agilebpm'
  import addGroupUser from '@/views/org/group/addGroupUser.vue'
  import { Delete, Plus, RefreshRight, Search } from '@element-plus/icons-vue'

  export default defineComponent({
    name: 'GroupUserList',
    components: { addGroupUser },
    mixins: [abTableMix],
    props: {
      orgId: {
        type: String,
        required: true,
      },
      currentOrg: {
        type: Object,
        required: true,
      },
    },
    setup(props) {
      // @ts-ignore
      const { proxy } = getCurrentInstance()

      const query: any = reactive({
        groupId: props.orgId,
      })

      const info: any = reactive({
        showDialog: false,
        selectedData: [],
      })

      const reSearchFn = () => {
        proxy.search()
      }

      const closeSaveDialog = (isAdd: any) => {
        info.showDialog = false
        if (isAdd) {
          // search();
        }
      }
      watch(
        () => props.orgId,
        (orgId) => {
          if (orgId) {
            query.groupId = orgId
            proxy.search()
          }
        }
      )
      onMounted(() => {
        query.groupId = props.orgId
      })
      return {
        query,
        info,
        reSearchFn,
        closeSaveDialog,
        orgApi,
        Delete,
        Plus,
        RefreshRight,
        Search,
      }
    },
  })
</script>
