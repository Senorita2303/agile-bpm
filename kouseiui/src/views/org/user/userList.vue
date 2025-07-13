<template>
  <div class="comprehensive-table-container">
    <div ref="titleForm">
      <el-row class="vab-query-form">
        <el-col class="top-panel">
          <el-form
            ref="queryForm"
            v-ab-btn-rights:userManager_search
            :inline="true"
            :model="query"
            label-width="90px"
            @submit.prevent
          >
            <el-form-item label="Full Name" prop="fullname$VLK">
              <el-input v-model="query.fullname$VLK" placeholder="Please enter your name" />
            </el-form-item>
            <el-form-item label="Account" prop="account$VLK">
              <el-input v-model="query.account$VLK" placeholder="Please enter your account" />
            </el-form-item>

            <el-form-item
              v-show="collapse"
              label="Status"
              prop="status$VEQ"
            >
              <el-select
                v-model="query.status$VEQ"
                clearable
                placeholder="All"
              >
                <el-option label="Disable" value="0" />
                <el-option label="Enable" value="1" />
              </el-select>
            </el-form-item>
            <el-form-item v-show="collapse" label="Mobile" prop="mobile$VLK">
              <el-input v-model="query.mobile$VLK" placeholder="Please enter your phone number" />
            </el-form-item>
            <el-form-item>
              <!--  abTableMix provides common methods such as search, reset, and handleCollapse -->
              <el-button :icon="Search" type="primary" @click="search()">
                Search
              </el-button>
              <el-button :icon="RefreshRight" @click="reset()">Reset</el-button>
              <el-button link type="primary" @click="handleCollapse()">
                <span v-if="!collapse" type="primary">Show Details</span>
                <span v-else type="primary">Collapse</span>
                <el-icon class="el-icon--right">
                  <ArrowUp v-if="!collapse" />
                  <arrow-down v-else />
                </el-icon>
              </el-button>
            </el-form-item>
          </el-form>
        </el-col>
        <el-col class="left-panel">
          <router-link to="/org/user/userEdit">
            <el-button
              v-ab-btn-rights:userManager_add
              :icon="Plus"
              type="primary"
            >
              Add
            </el-button>
          </router-link>
          <el-button
            v-ab-btn-rights:userManager_batchDel
            :disabled="!selectedData || selectedData.length == 0"
            :icon="Delete"
            style="margin-left: 12px"
            type="danger"
            @click="delBySeletedIds(orgApi.user.OrgUserRemoveUrl)"
          >
            Batch Delete
          </el-button>
        </el-col>
      </el-row>
    </div>

    <ab-table
      ref="abTable"
      v-model="selectedData"
      :height="tableHeight"
      :query-param="query"
      row-key="id"
      :url="orgApi.user.OrgUserListUrl"
    >
      <ab-column label="Full Name" min-width="80" prop="fullname" />
      <ab-column label="Account" min-width="100" prop="account" />
      <ab-column
        ab-effect="plain"
        ab-key="type"
        ab-text="desc"
        ab-type="success"
        label="Gender"
        min-width="60"
        prop="sex"
      />
      <ab-column label="Email" min-width="100" prop="email" />
      <ab-column label="Phone" min-width="100" prop="mobile" />
      <ab-column
        ab-tag-type="statusCss"
        label="Status"
        min-width="80"
        prop="statusDesc"
      />
      <ab-column label="Creation Time" min-width="120" prop="createTime" sortable />
      <ab-column ab-template="edit" fixed="right" label="Action" width="280" />
      <template #edit="{ scope }">
        <router-link :to="{ name: 'userEdit', query: { id: scope.row.id } }">
          <el-button v-ab-btn-rights:userManager_edit text type="primary">
            Edit
          </el-button>
        </router-link>

        <el-button
          v-ab-btn-rights:userManager_updateStatus
          text
          type="primary"
          @click="
            sendAction(
              orgApi.user.OrgUserUpdateStatusUrl + scope.row.id,
              'Confirm' +
                (scope.row.statusDesc === 'Enable' ? 'Disable' : 'Enable') +
                scope.row.fullname +
                ' ?'
            )
          "
        >
          {{ scope.row.statusDesc === 'Enable' ? 'Disable' : 'Enable' }}
        </el-button>
        <el-button
          v-ab-btn-rights:userManager_del
          text
          type="primary"
          @click="
            sendAction(
              orgApi.user.OrgUserRemoveUrl + scope.row.id,
              `Confirm to delete ${scope.row.account} ?`
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
  import { defineComponent, onMounted, reactive } from 'vue'
  import { abTableMix, orgApi } from 'agilebpm'
  import {
    ArrowDown,
    ArrowUp,
    Delete,
    Edit,
    Plus,
    RefreshRight,
    Search,
  } from '@element-plus/icons-vue'

  export default defineComponent({
    // Page Cache
    name: 'UserList',
    components: { ArrowUp, ArrowDown },
    mixins: [abTableMix],
    setup() {
      const query = reactive({
        fullname$VLK: '',
        account$VLK: '',
        sex$VEQ: '',
        mobile$VLK: '',
        status$VEQ: '',
      })
      // mounted timing
      onMounted(() => {})

      return {
        query,
        orgApi,
        Delete,
        Edit,
        Search,
        Plus,
        RefreshRight,
      }
    },
  })
</script>
