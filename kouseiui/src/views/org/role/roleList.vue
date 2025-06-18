<template>
  <div class="common-layout">
    <el-container>
      <el-aside class="left-tree">
        <ab-dict-tree
          v-model="query.typeCode$VEQ"
          type-code="jsfl"
          @node-click="searchResetPage()"
        />
      </el-aside>
      <el-main>
        <div class="comprehensive-table-container">
          <div ref="titleForm">
            <el-row class="vab-query-form">
              <el-col class="top-panel">
                <el-form
                  ref="queryForm"
                  v-ab-btn-rights:roleManager_search
                  :inline="true"
                  :model="query"
                  label-width="62px"
                  @submit.prevent
                >
                  <el-form-item label="Name" prop="name$VLK">
                    <el-input
                      v-model="query.name$VLK"
                      placeholder="Please enter a name"
                    />
                  </el-form-item>
                  <el-form-item label="Code" prop="code$VLK">
                    <el-input
                      v-model="query.code$VLK"
                      placeholder="Please enter the code"
                    />
                  </el-form-item>
                  <el-form-item
                    v-show="collapse"
                    label="Status"
                    prop="enabled$NEQ"
                  >
                    <el-select v-model="query.enabled$NEQ">
                      <el-option label="Please select" value="" />
                      <el-option label="Enable" value="1" />
                      <el-option label="Disable" value="0" />
                    </el-select>
                  </el-form-item>
                  <el-form-item>
                    <el-button :icon="Search" type="primary" @click="search()">
                      Search
                    </el-button>
                    <el-button :icon="RefreshRight" @click="reset()">
                      Reset
                    </el-button>
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
                <el-space>
                  <router-link
                    v-ab-btn-rights:roleManager_add
                    :to="{
                      name: 'RoleEdit',
                      query: { typeCode: query['typeCode$VEQ'] },
                    }"
                  >
                    <el-button :icon="Plus" type="primary">Add</el-button>
                  </router-link>
                  <el-button
                    v-ab-btn-rights:roleManager_batchDel
                    :disabled="!selectedData || selectedData.length == 0"
                    :icon="Delete"
                    type="danger"
                    @click="delBySeletedIds(orgApi.role.orgRoleRemoveUrl)"
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
            :url="orgApi.role.orgRoleListUrl"
          >
            <ab-column label="Name" min-width="120" prop="name" />
            <ab-column label="Code" prop="code" width="180" />
            <ab-column label="User Groups" prop="typeName" width="160" />
            <ab-column label="Level" prop="level" sortable width="90" />
            <ab-column
              ab-text-formatter="1-Enable-success-dark|0-Disable-danger-dark"
              label="Status"
              prop="enabled"
              width="90"
            />
            <ab-column
              ab-date-formatter="yyyy-MM-dd HH:mm"
              label="Creation Time"
              prop="createTime"
              sortable
              width="170"
            />
            <ab-column
              ab-template="edit"
              fixed="right"
              label="Action"
              width="410"
            />
            <template #edit="{ scope }">
              <router-link
                v-ab-btn-rights:roleManager_edit
                :to="{ name: 'RoleEdit', query: { id: scope.row.id } }"
              >
                <el-button text type="primary">Edit</el-button>
              </router-link>
              <el-button
                v-ab-btn-rights:roleManager_resource
                text
                type="primary"
                @click="openRoleResGrantDialog(scope.row)"
              >
                Role Resources
              </el-button>
              <router-link
                v-ab-btn-rights:roleManager_userList
                :to="{
                  name: 'RoleUserList',
                  query: { roleId: scope.row.id, roleName: scope.row.name },
                }"
              >
                <el-button text type="primary">Assign Users</el-button>
              </router-link>
              <el-button
                v-ab-btn-rights:roleManager_delete
                text
                type="primary"
                @click="
                  sendAction(
                    orgApi.role.orgRoleRemoveUrl + scope.row.id,
                    `Confirm to delete ${scope.row.name} ?`
                  )
                "
              >
                Delete
              </el-button>
            </template>
          </ab-table>
        </div>
      </el-main>
    </el-container>
    <!-- Role resource authorization -->
    <role-res-grant
      :roleRes="roleResGrantInfo"
      :role-id="roleResGrantInfo.roleId"
    />
  </div>
</template>

<script lang="ts">
  import { abTableMix, abDictTree, orgApi } from 'agilebpm'
  export default { name: 'RoleList', mixins: [abTableMix] }
</script>

<script lang="ts" setup>
  import { reactive } from 'vue'
  import {
    Search,
    Delete,
    Plus,
    RefreshRight,
    ArrowUp,
    ArrowDown,
  } from '@element-plus/icons-vue'
  import RoleResGrant from '@/views/org/role/roleResGrant.vue'

  const roleResGrantInfo = reactive({
    roleId: '',
    visible: false,
  })

  // Query condition definition, if ts needs to define all parameters, here override the parent class, if not, you don't need to set it, the parent class has already defined the query object
  const query: any = reactive({
    name$VLK: '',
    code$VLK: '',
    typeCode$VEQ: '',
    enabled$NEQ: '',
  })

  /**
   * Open the role resource authorization dialog box
   * @param role Role
   */
  const openRoleResGrantDialog = (role: any) => {
    roleResGrantInfo.visible = true
    roleResGrantInfo.roleId = role.id
  }
</script>
