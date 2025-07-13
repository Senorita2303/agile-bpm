<template>
  <div class="common-layout">
    <el-container>
      <el-aside class="left-tree">
        <flow-type-tree
          :tree-url="bpmApi.myTask.instanceListTypeTreeUrl"
          @node-click="searchData"
        />
      </el-aside>
      <el-main>
        <div class="comprehensive-table-container">
          <div ref="titleForm">
            <el-row class="vab-query-form">
              <el-col class="top-panel">
                <el-form
                  ref="queryForm"
                  :inline="true"
                  label-width="120px"
                  :model="query"
                  @submit.prevent
                >
                  <el-form-item label="Workflow Title" prop="title$VLK">
                    <el-input
                      v-model="query.title$VLK"
                      placeholder="Please enter the workflow title"
                    />
                  </el-form-item>
                  <el-form-item label="Workflow Code" prop="defKey$VLK">
                    <el-input
                      v-model="query.defKey$VLK"
                      placeholder="Please enter the process code"
                    />
                  </el-form-item>
                  <el-form-item>
                    <el-button :icon="Search" type="primary" @click="search()">
                      Search
                    </el-button>
                    <el-button :icon="RefreshRight" @click="reset()">
                      Reset
                    </el-button>
                  </el-form-item>
                </el-form>
              </el-col>
              <el-col class="left-panel">
                <el-button
                  v-ab-btn-rights:instance_del
                  :disabled="!selectedData || selectedData.length == 0"
                  :icon="Delete"
                  type="danger"
                  @click="delBySeletedIds(bpmApi.instance.bpmInstanceDeleteUrl)"
                >
                  Batch Delete
                </el-button>
              </el-col>
            </el-row>
          </div>
          <!-- Key fields are set to minwith, and specific enumeration fields are set to with (management columns, dates, status, dictionaries, etc.).
         Put unimportant fields at the end and manage column settings fixed="right"
    -->
          <ab-table
            ref="abTable"
            v-model="selectedData"
            :height="tableHeight"
            :query-param="query"
            row-key="id"
            :url="bpmApi.instance.bpmInstanceListUrl"
          >
            <ab-column label="Workflow Title" min-width="340" prop="title" />
            <ab-column label="Workflow Code" min-width="205" prop="defKey" />
            <ab-column
              ab-text-formatter="0-No-success-dark|1-Yes-danger-dark"
              label="Suspend"
              prop="suspensionState"
              width="90"
            />
            <ab-column
              ab-tag-type="statusCss"
              label="Status"
              prop="statusDesc"
              width="150"
            />
            <ab-column label="Creation Time" prop="createTime" width="160" />
            <ab-column label="Creator" prop="creator" width="120" />
            <ab-column label="End Time" prop="endTime" width="160" />
            <ab-column ab-template="durMs" label="Execution time" width="120" />
            <template #durMs="{ scope }">
              {{ abUtil.timeLag(scope.row.durationMs, 2) }}
            </template>
            <ab-column label="Update Time" prop="updateTime" width="160" />
            <ab-column
              ab-template="edit"
              fixed="right"
              label="Manage"
              width="330"
            />
            <template #edit="{ scope }">
              <router-link
                :to="{
                  name: 'BpmInstanceListDetail',
                  query: { id: scope.row.id },
                }"
              >
                <el-button text type="primary">Details</el-button>
              </router-link>
              <el-button
                text
                type="primary"
                @click="
                  sendAction(
                    bpmApi.instance.bpmInstanceDeleteUrl + scope.row.id,
                    `Are you sure you want to delete ${scope.row.title}?`
                  )
                "
              >
                Delete
              </el-button>
              <el-button
                text
                type="primary"
                @click="openImageDialog(scope.row.id)"
              >
                Flowchart
              </el-button>
              <el-button
                v-if="
                  scope.row.suspensionState === 0 &&
                  scope.row.status != 'completed' &&
                  scope.row.status != 'draft' &&
                  scope.row.status != 'manualEnd' &&
                  scope.row.status != 'revoke_end'
                "
                text
                type="primary"
                @click="forbidden(scope.row)"
              >
                Suspend
              </el-button>
              <el-button
                v-if="scope.row.suspensionState === 1"
                text
                type="primary"
                @click="forbidden(scope.row)"
              >
                Cancel Suspend
              </el-button>
            </template>
          </ab-table>
          <bpm-image-dialog
            v-if="flowImageInfo.showImageDialog"
            :bpm-data="flowImageInfo.bpmData"
            :is-show="flowImageInfo.showImageDialog"
            title="Flowchart"
            @dialog-cancel="dialogCancel"
          />
        </div>
      </el-main>
    </el-container>
  </div>
</template>
<script lang="ts">
  // eslint-disable-next-line no-unused-vars
  import { reactive, defineComponent, getCurrentInstance } from 'vue'
  import { abTableMix, bpmApi, abUtil, BpmImageDialog } from 'agilebpm'
  import { ElMessage, ElMessageBox } from 'element-plus'
  import flowTypeTree from '../myTask/components/flow-type-tree.vue'
  import {
    Delete,
    Edit,
    Search,
    Plus,
    RefreshRight,
  } from '@element-plus/icons-vue'

  export default defineComponent({
    name: 'BpmInstanceList',
    components: { BpmImageDialog, flowTypeTree },
    mixins: [abTableMix],

    setup() {
      // @ts-ignore
      const { proxy } = getCurrentInstance()
      const query = reactive({
        title$VLK: '',
        defKey$VLK: '',
        typeCode$VIN: '',
        defKey$VEQ: '',
      })
      const flowImageInfo = reactive({
        showImageDialog: false,
        bpmData: {
          actionData: {
            instanceId: '',
          },
        } as BpmData,
      })

      const openImageDialog = (instaceId: string) => {
        flowImageInfo.showImageDialog = true
        flowImageInfo.bpmData.actionData.instanceId = instaceId
      }

      const dialogCancel = (isShow: boolean) => {
        flowImageInfo.showImageDialog = false
      }

      const forbidden = (row: any) => {
        ElMessageBox.confirm(
          row.suspensionState == 1
            ? `Are you sure you want to cancel the suspended process ${row.title}`
            : `Are you sure you want to suspend process ${row.title}?`,
          'Message',
          {
            confirmButtonText: 'Confirm',
            cancelButtonText: 'Cancel',
            type: 'warning',
          }
        ).then(async () => {
          const forbiddenFlag = row.suspensionState == 1 ? false : true
          await bpmApi.instance.toForbidden(row.id, forbiddenFlag).then(
            (result: any) => {
              proxy.search()
              ElMessage({
                type: 'success',
                message: result.msg || 'Operation successful',
                onClose: () => {},
              })
            },
            () => {}
          )
        })
      }

      const searchData = (type: string, value: string) => {
        if (type === 'default') {
          query.defKey$VEQ = ''
          query.typeCode$VIN = ''
        }
        if (type === 'flow') {
          query.defKey$VEQ = value
          query.typeCode$VIN = ''
        } else if (type === 'tree') {
          query.typeCode$VIN = value
          query.defKey$VEQ = ''
        }
        proxy.search()
      }

      return {
        query,
        bpmApi,
        flowImageInfo,
        openImageDialog,
        dialogCancel,
        forbidden,
        Delete,
        Edit,
        Search,
        Plus,
        RefreshRight,
        abUtil,
        searchData,
      }
    },
  })
</script>
