<template>
  <div class="common-layout">
    <el-container>
      <el-aside class="left-tree">
        <flow-type-tree
          :tree-url="bpmApi.myTask.taskListTypeTreeUrl"
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
                  label-width="100px"
                  :model="query"
                  @submit.prevent
                >
                  <el-form-item label="Workflow Title" prop="title$VLK">
                    <el-input
                      v-model="query.title$VLK"
                      placeholder="Please enter the workflow title"
                    />
                  </el-form-item>
                  <el-form-item label="Task Name" prop="name$VLK">
                    <el-input
                      v-model="query.name$VLK"
                      placeholder="Please enter the task name"
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
            </el-row>
          </div>

          <ab-table
            ref="abTable"
            :checkable="false"
            :height="tableHeight"
            :query-param="query"
            :url="bpmApi.task.bpmTaskListUrl"
          >
            <ab-column label="Workflow Title" min-width="250" prop="title" />
            <ab-column label="Task Name" min-width="200" prop="name" />
            <ab-column label="Candidate" min-width="200" prop="assigneeNames" />
            <ab-column
              ab-tag-effect="plain"
              ab-tag-type="typeCss"
              label="Type"
              prop="typeDesc"
              width="110"
            />
            <ab-column
              ab-tag-effect="dark"
              ab-tag-type="statusCss"
              label="Status"
              prop="statusDesc"
              width="100"
            />
            <ab-column
              ab-text-formatter="0-No-warning-plain|1-Support-success-plain"
              label="Mobile"
              prop="supportMobile"
              width="80"
            />
            <ab-column label="Creation Time" prop="createTime" width="180" />
            <ab-column
              ab-template="edit"
              fixed="right"
              label="Manage"
              width="280"
            />
            <template #edit="{ scope }">
              <el-button
                v-if="scope.row.status == 'designate'"
                v-ab-btn-rights:task_cancelAssign
                text
                type="primary"
                @click="
                  sendAction(
                    bpmApi.task.cancelAssignURL + scope.row.id,
                    `Are you sure you want to unassign ${scope.row.title}?`
                  )
                "
              >
                Unassign
              </el-button>
              <ab-cust-dialog
                v-else
                dialog-key="userSelector"
                :dialog-setting="{ multiple: 0 }"
                :extra-data="scope.row.id"
                style="display: inline-block"
                text
                type="primary"
                @ok="assignTaskFn"
              >
                Assignment Candidates
              </ab-cust-dialog>
              <router-link
                :to="{
                  name: 'BpmTaskComplate',
                  query: { id: scope.row.id, backR: 'BpmTaskList' },
                }"
              >
                <el-button text type="primary">Process</el-button>
              </router-link>
              <el-button
                v-ab-btn-rights:task_imageInfo
                text
                type="primary"
                @click="openImageDialog(scope.row)"
              >
                Flowchart
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
  import { reactive, defineComponent, onMounted, getCurrentInstance } from 'vue'
  import { ElMessage } from 'element-plus'
  import { abTableMix, bpmApi, BpmImageDialog } from 'agilebpm'
  import { Search, RefreshRight } from '@element-plus/icons-vue'
  import flowTypeTree from '../myTask/components/flow-type-tree.vue'
  export default defineComponent({
    name: 'BpmTaskList',
    components: { BpmImageDialog, flowTypeTree },
    mixins: [abTableMix],
    setup() {
      const query = reactive({
        name$VLK: '',
        title$VLK: '',
        typeCode$VIN: '',
        defId$VEQ: '',
      })
      const flowImageInfo = reactive({
        showImageDialog: false,
        bpmData: {
          actionData: {
            instanceId: '',
            taskId: '',
          },
        },
      })

      // @ts-ignore
      const { proxy } = getCurrentInstance()

      const assignTaskFn = (userList: any, taskId: any) => {
        if (!userList || userList.length == 0) {
          ElMessage.warning('Please designate someone!')
        }
        bpmApi.task.assignTask(taskId, userList[0].id).then((reset: any) => {
          ElMessage.success('Assignment successful!')
          proxy.search()
        })
      }

      const openImageDialog = (row: any) => {
        flowImageInfo.showImageDialog = true
        flowImageInfo.bpmData.actionData = {
          instanceId: row.instId,
          taskId: row.id,
        }
      }

      const dialogCancel = (isShow: boolean) => {
        flowImageInfo.showImageDialog = false
      }

      const searchData = (type: string, value: string) => {
        if (type === 'default') {
          query.defId$VEQ = ''
          query.typeCode$VIN = ''
        }
        if (type === 'flow') {
          query.defId$VEQ = value
        } else if (type === 'tree') {
          query.typeCode$VIN = value
        }
        proxy.search()
        query.defId$VEQ = ''
        query.typeCode$VIN = ''
      }

      return {
        query,
        bpmApi,
        Search,
        RefreshRight,
        assignTaskFn,
        flowImageInfo,
        openImageDialog,
        dialogCancel,
        searchData,
      }
    },
  })
</script>
